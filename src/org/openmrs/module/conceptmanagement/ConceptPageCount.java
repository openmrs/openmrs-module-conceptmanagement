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


/**
 *
 */
public class ConceptPageCount {
	
	private int currentPage;
	private int conceptsPerPage;
	
	
	public ConceptPageCount() {
		this.currentPage = 1;
		this.conceptsPerPage = 25;
	}


	
    /**
     * @return the currentPage
     */
    public int getCurrentPage() {
    	return currentPage;
    }


	
    /**
     * @param currentPage the currentPage to set
     */
    public void setCurrentPage(int currentPage) {
    	this.currentPage = currentPage;
    }


	
    /**
     * @return the conceptsPerPage
     */
    public int getConceptsPerPage() {
    	return conceptsPerPage;
    }


	
    /**
     * @param conceptsPerPage the conceptsPerPage to set
     */
    public void setConceptsPerPage(int conceptsPerPage) {
    	this.conceptsPerPage = conceptsPerPage;
    }
	
	

}
