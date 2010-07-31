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
package org.openmrs.module.conceptmanagement;

import java.util.List;

import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.openmrs.ConceptDescription;
import org.openmrs.ConceptName;

/**
 * Class to store all the needed information about a concept. By storing all data in this class we
 * avoid Hibernate's lazy loading error when not displaying all concepts at once and we can connect
 * some other information with a certain concept (like number of obs)
 */
public class ConceptSearchResult {
	
	private String conceptName;
	
	private String conceptDescription;
	
	private String conceptClass;
	
	private String conceptDatatype;
	
	private List<String> otherNames;
	
	//private String numberOfObs;
	
	//private String usedInForms;
	
	public ConceptSearchResult(ConceptName name, ConceptDescription description, ConceptClass cclass,
	    ConceptDatatype datatype) {
		this.conceptName = name.getName();
		this.conceptDescription = description.getDescription();
		this.conceptClass = cclass.getName();
		this.conceptDatatype = datatype.getName();
	}
	
	public ConceptSearchResult(Concept con) {
		this.conceptName = con.getName().getName();
		if(con.getDescription()!=null)
			this.conceptDescription = con.getDescription().getDescription();
		this.conceptClass = con.getConceptClass().getName();
		this.conceptDatatype = con.getDatatype().getName();
	}
	
	/**
	 * @return the conceptName
	 */
	public String getConceptName() {
		return conceptName;
	}
	
	/**
	 * @param conceptName the conceptName to set
	 */
	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}
	
	/**
	 * @return the conceptDescription
	 */
	public String getConceptDescription() {
		return conceptDescription;
	}
	
	/**
	 * @param conceptDescription the conceptDescription to set
	 */
	public void setConceptDescription(String conceptDescription) {
		this.conceptDescription = conceptDescription;
	}
	
	/**
	 * @return the conceptClass
	 */
	public String getConceptClass() {
		return conceptClass;
	}
	
	/**
	 * @param conceptClass the conceptClass to set
	 */
	public void setConceptClass(String conceptClass) {
		this.conceptClass = conceptClass;
	}
	
	/**
	 * @return the conceptDatatype
	 */
	public String getConceptDatatype() {
		return conceptDatatype;
	}
	
	/**
	 * @param conceptDatatype the conceptDatatype to set
	 */
	public void setConceptDatatype(String conceptDatatype) {
		this.conceptDatatype = conceptDatatype;
	}
	
	/**
	 * @return the otherNames
	 */
	public List<String> getOtherNames() {
		return otherNames;
	}
	
	/**
	 * @param otherNames the otherNames to set
	 */
	public void setOtherNames(List<String> otherNames) {
		this.otherNames = otherNames;
	}
	
}
