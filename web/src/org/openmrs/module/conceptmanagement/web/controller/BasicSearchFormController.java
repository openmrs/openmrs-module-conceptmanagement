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
package org.openmrs.module.conceptmanagement.web.controller;

import java.util.Collection;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.conceptmanagement.ConceptPageCount;
import org.openmrs.module.conceptmanagement.ConceptSearch;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

/**
 * Controller to handle all searches performed by basicsearch.jsp.
 */
@Controller
public class BasicSearchFormController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/module/conceptmanagement/basicSearch", method = RequestMethod.GET)
	public void showBasicSearch(ModelMap model, WebRequest request, HttpSession session) {
		//display advancedSearch.jsp	
		session.removeAttribute("searchResult");
		session.removeAttribute("sortResults");
		session.removeAttribute("conceptSearch");
		session.removeAttribute("countConcept");
		
		ConceptPageCount conCount = new ConceptPageCount();
		session.setAttribute("countConcept", conCount);
	}
	
	@RequestMapping(value = "/module/conceptmanagement/basicSearch", method = RequestMethod.GET, params = "count")
	public void setConceptsPerPage(ModelMap model, WebRequest request, HttpSession session) {
		ConceptPageCount conCount = new ConceptPageCount();
		
		//set count
		String count = request.getParameter("count");
		
		if (session.getAttribute("countConcept") == null) {
			session.setAttribute("countConcept", conCount);
			conCount.setConceptsPerPage(Integer.parseInt(count));
		} else {
			conCount = (ConceptPageCount) session.getAttribute("countConcept");
			int cCount = Integer.parseInt(count);
			if (cCount == -1)
				cCount = 10000;
			conCount.setConceptsPerPage(cCount);
			conCount.setCurrentPage(1);
		}
		model.addAttribute("countConcept", conCount);
		
		//add other elements (search words and results) to the view, so they are displayed
		ConceptSearch cs = (ConceptSearch) session.getAttribute("conceptSearch");
		if (cs != null) {
			model.addAttribute("conceptSearch", cs);
		}
		Collection<Concept> conList = (Collection<Concept>) session.getAttribute("sortResults");
		if (conList != null) {
			model.addAttribute("searchResult", conList);
		}
	}
	
	@RequestMapping(value = "/module/conceptmanagement/basicSearch", method = RequestMethod.GET, params = "page")
	public void switchToPage(ModelMap model, WebRequest request, HttpSession session) {
		//set page
		String page = request.getParameter("page");
		
		ConceptPageCount conCount = (ConceptPageCount) session.getAttribute("countConcept");
		if (conCount != null) {
			conCount.setCurrentPage(Integer.parseInt(page));
			model.addAttribute("countConcept", conCount);
		}
		
		//add other elements (search words and results) to the view, so they are displayed
		ConceptSearch cs = (ConceptSearch) session.getAttribute("conceptSearch");
		if (cs != null) {
			model.addAttribute("conceptSearch", cs);
		}
		Collection<Concept> conList = (Collection<Concept>) session.getAttribute("sortResults");
		if (conList != null) {
			model.addAttribute("searchResult", conList);
		} else {
			System.err.println("Results are gone");
		}
	}
	
	@RequestMapping(value = "/module/conceptmanagement/basicSearch", method = RequestMethod.POST)
	public void performBasicSearch(ModelMap model, WebRequest request, HttpSession session) {
		Collection<Concept> rslt = new Vector<Concept>();
		ConceptSearch cs = new ConceptSearch("");
		
		String searchQuery = (String) request.getParameter("conceptQuery");
		
		if (searchQuery != null) {
			cs.setSearchQuery(searchQuery);
			rslt = Context.getConceptService().getConceptsByName(searchQuery);
			
			model.addAttribute("searchResult", rslt);
			model.addAttribute("sortResults", rslt);
			model.addAttribute("conceptSearch", cs);
			
			session.setAttribute("sortResults", rslt);
			session.setAttribute("conceptSearch", cs);
		}
	}
	
}
