/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.conceptmanagement.db.hibernate;

import java.util.List;
import java.util.Vector;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.conceptmanagement.ConceptSearch;
import org.openmrs.module.conceptmanagement.ConceptSearchDAO;

/**
 *
 */
public class HibernateConceptSearchDAO implements ConceptSearchDAO {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	protected SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public Concept getConcept(Integer conceptId) throws DAOException {
		return (Concept) sessionFactory.getCurrentSession().get(Concept.class, conceptId);
	}
	
	public Long getNumberOfObsForConcept(Integer conceptId) throws DAOException {
		return (Long) sessionFactory.getCurrentSession().createQuery("SELECT COUNT(*) FROM Obs WHERE concept_id = :cid")
		        .setString("cid", String.valueOf(conceptId)).uniqueResult();
	}
	
	public Long getNumberOfFormsForConcept(Integer conceptId) throws DAOException {
		return (Long) sessionFactory.getCurrentSession().createQuery("SELECT COUNT(*) FROM Forms WHERE concept_id = :cid")
		        .setString("cid", String.valueOf(conceptId)).uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Concept> getConcepts(ConceptSearch cs) throws DAOException {
		Criteria crit = createGetConceptsCriteria(cs);
		
		return crit.list();
	}
	
	private Criteria createGetConceptsCriteria(ConceptSearch cs) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Concept.class);
		
		if (!cs.getSearchQuery().isEmpty()) {
			crit.createAlias("names", "names");
			crit.add(Restrictions.like("names.name", "%" + cs.getSearchQuery() + "%"));
		}
		
		/*		if (CollectionUtils.isNotEmpty(cs.getSearchTermsList())) {
					crit.add(Restrictions.in("description", cs.getSearchTermsList())); //TODO: contains? like?
				}*/

		if (CollectionUtils.isNotEmpty(cs.getDataTypes())) {
			crit.add(Restrictions.in("datatype", cs.getDataTypes()));
		}
		
		if (CollectionUtils.isNotEmpty(cs.getConceptClasses())) {
			crit.add(Restrictions.in("conceptClass", cs.getConceptClasses()));
		}
		
		if (cs.getIsSet() != -1) {
			if (cs.getIsSet() == 0) {
				crit.add(Restrictions.eq("set", Boolean.FALSE));
			} else {
				crit.add(Restrictions.eq("set", Boolean.TRUE));
			}
		}
		
		if ((cs.getDateFrom() != null) && (cs.getDateTo() != null)) {
			crit.add(Restrictions.between("dateCreated", cs.getDateFrom(), cs.getDateTo()));
		} else if (cs.getDateFrom() != null) {
			crit.add(Restrictions.gt("dateCreated", cs.getDateFrom()));
		} else if (cs.getDateTo() != null) {
			crit.add(Restrictions.le("dateCreated", cs.getDateTo()));
		}
		
		return crit;
		
	}
	
}
