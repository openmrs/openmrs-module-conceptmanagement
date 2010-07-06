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
import org.hibernate.criterion.Restrictions;
import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.conceptmanagement.ConceptSearch;

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
	
	public Integer getNumberOfObsForConcept(Integer conceptId) throws DAOException {
		return (Integer) sessionFactory.getCurrentSession().createQuery(
		    "SELECT COUNT(*) FROM obs o WHERE o.concept_id = :cid").setString("cid", String.valueOf(conceptId))
		        .uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Concept> getConcepts(ConceptSearch cs) throws DAOException {
		Criteria crit = createGetConceptsCriteria(cs);
		
		return crit.list();
	}
	
	private Criteria createGetConceptsCriteria(ConceptSearch cs) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Concept.class);
		
		if (!cs.getSearchQuery().isEmpty()) {
			crit.add(Restrictions.sqlRestriction("lower({alias}.short_name) like lower(?)", "%" + cs.getSearchQuery() + "%",
			    Hibernate.STRING));
		}
		
		/*		if (CollectionUtils.isNotEmpty(cs.getSearchTermsList())) {
					crit.add(Restrictions.in("description", cs.getSearchTermsList())); //TODO: contains? like?
				}*/

		if (CollectionUtils.isNotEmpty(cs.getDataTypes())) {
			List<Integer> dataList = new Vector<Integer>();
			for (ConceptDatatype c : cs.getDataTypes()) {
				dataList.add(c.getConceptDatatypeId());
			}
			crit.add(Restrictions.in("concept_datatype_id", dataList));
		}
		
		if (CollectionUtils.isNotEmpty(cs.getConceptClasses())) {
			List<Integer> classList = new Vector<Integer>();
			for (ConceptClass c : cs.getConceptClasses()) {
				classList.add(c.getConceptClassId());
			}
			crit.add(Restrictions.in("concept_datatype_id", classList));
		}
		
		if (cs.getIsSet() != -1) {
			if (cs.getIsSet() == 0) {
				crit.add(Restrictions.eq("is_set", Boolean.FALSE));
			} else {
				crit.add(Restrictions.eq("is_set", Boolean.TRUE));
			}
		}
		
		if ((cs.getDateFromAsDate() != null) && (cs.getDateToAsDate() != null)) {
			crit.add(Restrictions.between("date_created", cs.getDateFromAsDate(), cs.getDateToAsDate()));
		} else if (cs.getDateFromAsDate() != null) {
			crit.add(Restrictions.gt("date_created", cs.getDateFromAsDate()));
		} else if (cs.getDateToAsDate() != null) {
			crit.add(Restrictions.le("date_created", cs.getDateToAsDate()));
		}
		
		return crit;
		
	}
	
}
