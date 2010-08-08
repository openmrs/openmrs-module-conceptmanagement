package org.openmrs.module.conceptmanagement;

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

import java.util.List;

import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Transactional
public interface ConceptSearchService {
	
	public void setConceptSearchDAO(ConceptSearchDAO dao);
	
	@Transactional(readOnly = true)
	public List<Concept> getConcepts(ConceptSearch cs);
	
	@Transactional(readOnly = true)
	public Concept getConcept(Integer conceptId);
	
	@Transactional(readOnly = true)
	public Long getNumberOfObsForConcept(Integer conceptId);
	
	@Transactional(readOnly = true)
	public Long getNumberOfFormsForConcept(Integer conceptId);
	
	@Transactional(readOnly = true)
	public List<ConceptClass> getAllConceptClasses();
	
	@Transactional(readOnly = true)
	public List<ConceptDatatype> getAllConceptDatatypes();
	
	@Transactional(readOnly = true)
	public ConceptDatatype getConceptDatatypeById(int id);
	
	@Transactional(readOnly = true)
	public ConceptClass getConceptClassById(int id);
	
	@Transactional(readOnly = true)
	public boolean isConceptUsedAs(Concept concept, ConceptSearch cs);
	
}
