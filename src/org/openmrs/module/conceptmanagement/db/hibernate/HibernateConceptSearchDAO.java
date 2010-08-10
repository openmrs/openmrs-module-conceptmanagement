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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.openmrs.Obs;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.conceptmanagement.ConceptSearch;
import org.openmrs.module.conceptmanagement.ConceptSearchDAO;

/**
 *
 */
public class HibernateConceptSearchDAO implements ConceptSearchDAO {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * Hibernate session factory
	 */
	protected SessionFactory sessionFactory;
	
	/**
	 * Set session factory
	 * @param sessionFactory session factory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * @see org.openmrs.module.conceptmanagement.ConceptSearchDAO#getConcept(java.lang.Integer)
	 */
	public Concept getConcept(Integer conceptId) throws DAOException {
		return (Concept) sessionFactory.getCurrentSession().get(Concept.class, conceptId);
	}
	
	/**
	 * @see org.openmrs.module.conceptmanagement.ConceptSearchDAO#getAllConceptClasses()
	 */
	@SuppressWarnings("unchecked")
	public List<ConceptClass> getAllConceptClasses() throws DAOException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ConceptClass.class);
		
		return crit.list();
	}
	
	/**
	 * @see org.openmrs.module.conceptmanagement.ConceptSearchDAO#getAllConceptDatatypes()
	 */
	@SuppressWarnings("unchecked")
	public List<ConceptDatatype> getAllConceptDatatypes() throws DAOException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ConceptDatatype.class);
		
		return crit.list();
	}
	
	/**
	 * @see org.openmrs.module.conceptmanagement.ConceptSearchDAO#getConceptDatatypeById(int)
	 */
	public ConceptDatatype getConceptDatatypeById(int id) throws DAOException {
		return (ConceptDatatype) sessionFactory.getCurrentSession().get(ConceptDatatype.class, id);
	}
	
	/**
	 * @see org.openmrs.module.conceptmanagement.ConceptSearchDAO#getConceptClassById(int)
	 */
	public ConceptClass getConceptClassById(int id) throws DAOException {
		return (ConceptClass) sessionFactory.getCurrentSession().get(ConceptClass.class, id);
	}
	
	/**
	 * @see org.openmrs.module.conceptmanagement.ConceptSearchDAO#getNumberOfObsForConcept(java.lang.Integer)
	 */
	public Long getNumberOfObsForConcept(Integer conceptId) throws DAOException {
		return (Long) sessionFactory.getCurrentSession().createQuery("SELECT COUNT(*) FROM Obs WHERE concept_id = :cid")
		        .setString("cid", String.valueOf(conceptId)).uniqueResult();
	}
	
	/**
	 * @see org.openmrs.module.conceptmanagement.ConceptSearchDAO#getNumberOfFormsForConcept(java.lang.Integer)
	 */
	public Long getNumberOfFormsForConcept(Integer conceptId) throws DAOException {
		return (Long) sessionFactory.getCurrentSession().createQuery("SELECT COUNT(*) FROM Forms WHERE concept_id = :cid")
		        .setString("cid", String.valueOf(conceptId)).uniqueResult();
	}
	
	/**
	 * @see org.openmrs.module.conceptmanagement.ConceptSearchDAO#getConcepts(org.openmrs.module.conceptmanagement.ConceptSearch)
	 */
	@SuppressWarnings("unchecked")
	public List<Concept> getConcepts(ConceptSearch cs) throws DAOException {
		Criteria crit = createGetConceptsCriteria(cs);
		
		return crit.list();
	}
	
	/**
	 * Method to create the criteria from the ConceptSearch object
	 * @param cs ConceptSearch object that contains all criteria
	 * @return search criteria
	 */
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
	
	/**
	 * @see org.openmrs.module.conceptmanagement.ConceptSearchDAO#isConceptUsedAs(org.openmrs.Concept, org.openmrs.module.conceptmanagement.ConceptSearch)
	 */
	public boolean isConceptUsedAs(Concept concept, ConceptSearch cs) throws DAOException {
		List<String> usedAs = cs.getConceptUsedAs();
		
		if (usedAs==null)
			return true;
		
		if (usedAs.contains("formQuestion")) {

		}
		if (usedAs.contains("formAnswer")) {
			/*System.out.println("anser");
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(FieldAnswer.class);
			crit.add(Restrictions.eq("concept", concept));
			crit.setProjection(Projections.rowCount());
			if ((Integer) crit.list().get(0) == 0)
				return false;*/
		}
		if (usedAs.contains("obsQuestion")) {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(Obs.class);
			crit.add(Restrictions.eq("concept", concept));
			crit.setProjection(Projections.rowCount());
			if ((Integer) crit.list().get(0) == 0)
				return false;
		}
		if (usedAs.contains("obsValue")) {
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(Obs.class);
			crit.add(Restrictions.eq("valueCoded", concept));
			crit.setProjection(Projections.rowCount());
			if ((Integer) crit.list().get(0) == 0)
				return false;
		}
		
		return true;
	}
	
}
