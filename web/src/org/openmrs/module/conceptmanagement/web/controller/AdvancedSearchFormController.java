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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.openmrs.api.context.Context;
import org.openmrs.module.conceptmanagement.ConceptComparator;
import org.openmrs.module.conceptmanagement.ConceptSearch;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

@Controller
public class AdvancedSearchFormController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	@ModelAttribute("dataTypes")
	public List<ConceptDatatype> populateDataTypes() {
		return Context.getConceptService().getAllConceptDatatypes();
	}
	
	@ModelAttribute("conceptClasses")
	public List<ConceptClass> populateConceptClasses() {
		return Context.getConceptService().getAllConceptClasses();
	}
	
	@RequestMapping(value = "/module/conceptmanagement/advancedSearch", method = RequestMethod.GET)
	public void showAdvancedSearch(ModelMap model, WebRequest request, HttpSession session) {
		//display advancedSearch.jsp	
		session.removeAttribute("sortResults");
		session.removeAttribute("conceptSearch");
		System.out.println("show page");
	}
	
	@RequestMapping(value = "/module/conceptmanagement/advancedSearch", method = RequestMethod.GET, params = "sort")
	public void sortResultsView(ModelMap model, WebRequest request, HttpSession session) {
		//display advancedSearch.jsp	
		System.out.println("sort");
		String sortFor = request.getParameter("sort");
		boolean asc = true;
		
		if (request.getParameter("order") != null && request.getParameter("order").equals("desc"))
			asc = false;
		
		Collection<Concept> conList = (Collection<Concept>) session.getAttribute("sortResults");
		ConceptSearch cs = (ConceptSearch) session.getAttribute("conceptSearch");
		if (cs != null)
			model.addAttribute("conceptSearch", cs);
		
		if (conList != null) {
			Collections.sort((List<Concept>) conList, new ConceptComparator(sortFor, asc));
			model.addAttribute("searchResult", conList);
		}
	}
	
	@RequestMapping(value = "/module/conceptmanagement/advancedSearch", method = RequestMethod.POST)
	public void performAdvancedSearch(ModelMap model, WebRequest request, HttpSession session) {
		Collection<Concept> rslt = new Vector<Concept>();
		ConceptSearch cs = new ConceptSearch("");
		
		//get all parameters
		String searchName = request.getParameter("conceptQuery");
		String searchDescription = request.getParameter("conceptDescription");
		String[] searchDatatypes = request.getParameterValues("conceptDatatype");
		String[] searchClassesString = request.getParameterValues("conceptClasses");
		String searchIsSet = request.getParameter("conceptIsSet");
		
		//check for correct selections
		if (searchDatatypes != null && searchDatatypes[0].equals("-1")) {
			searchDatatypes = null;
			cs.setDataTypes(new Vector<ConceptDatatype>());
		}
		
		if (searchClassesString != null && searchClassesString[0].equals("-1")) {
			searchClassesString = null;
			cs.setConceptClasses(new Vector<ConceptClass>());
		}
		
		if (searchIsSet != null && searchIsSet.equals("-1")) {
			searchIsSet = null;
			cs.setIsSet(-1);
		} else {
			cs.setIsSet(Integer.parseInt(searchIsSet));
		}
		
		//add elements to it, other elements are added below
		cs.setSearchQuery(searchName);
		
		//search
		rslt = Context.getConceptService().getConceptsByName(searchName);
		
		if (searchDescription != null) {
			String[] searchTerms = searchDescription.split(" ");
			List<String> searchTermsList = Arrays.asList(searchTerms);
			
			//add all concepts found by search-term (=one word of the entered description) here
			/*for (String s: searchTermsList) {
				rslt.addAll();
			}*/

			cs.setSearchTerms(searchTermsList);
		}
		
		if (searchIsSet != null) {
			Collection<Concept> newRslt = new Vector<Concept>();
			
			for (Concept c : rslt) {
				if (c.isSet() == searchIsSet.equals("1")) {
					newRslt.add(c);
				}
			}
			rslt = newRslt;
		}
		
		if (searchDatatypes != null) {
			Collection<Concept> newRslt = new Vector<Concept>();
			List<String> searchDatatypesList = Arrays.asList(searchDatatypes);
			List<ConceptDatatype> dataTypesList = new Vector<ConceptDatatype>();
			
			for (Concept c : rslt) {
				if (searchDatatypesList.contains(c.getDatatype().getName())) {
					newRslt.add(c);
				}
			}
			rslt = newRslt;
			
			for (String s : searchDatatypesList) {
				dataTypesList.add(Context.getConceptService().getConceptDatatypeByName(s));
			}
			
			cs.setDataTypes(dataTypesList);
		}
		
		if (searchClassesString != null) {
			Collection<Concept> newRslt = new Vector<Concept>();
			List<String> searchClassesList = Arrays.asList(searchClassesString);
			List<ConceptClass> classesList = new Vector<ConceptClass>();
			
			for (Concept c : rslt) {
				if (searchClassesList.contains(c.getConceptClass().getName())) {
					newRslt.add(c);
				}
			}
			rslt = newRslt;
			
			for (String s : searchClassesList) {
				classesList.add(Context.getConceptService().getConceptClassByName(s));
			}
			cs.setConceptClasses(classesList);
		}
		
		model.addAttribute("searchResult", rslt);
		model.addAttribute("conceptSearch", cs);
		//add search results to session to make them sortable
		session.setAttribute("sortResults", rslt);
		session.setAttribute("conceptSearch", cs);
	}
	
}
