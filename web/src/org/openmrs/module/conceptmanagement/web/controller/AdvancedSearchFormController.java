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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.openmrs.api.context.Context;
import org.openmrs.module.conceptmanagement.ConceptSearch;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 */
public class AdvancedSearchFormController extends SimpleFormController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * Returns any extra data in a key-->value pair kind of way
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest,
	 *      java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request, Object obj, Errors err) throws Exception {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<ConceptDatatype> dataTypes = new Vector<ConceptDatatype>();
		List<ConceptClass> conceptClasses = new Vector<ConceptClass>();
		
		dataTypes = Context.getConceptService().getAllConceptDatatypes();
		conceptClasses = Context.getConceptService().getAllConceptClasses();
		
		map.put("dataTypes", dataTypes);
		map.put("conceptClasses", conceptClasses);
		
		return map;
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object object,
	                                BindException exceptions) throws Exception {
		
		if (!Context.isAuthenticated()) {
			return new ModelAndView(getFormView());
		}
		
		HttpSession session = request.getSession();
		
		Map model = exceptions.getModel();
		model.putAll(referenceData(request, object, null));
		ModelAndView mav = new ModelAndView(getSuccessView(), model);

		//redirecting
		//ModelAndView mav = new ModelAndView(new RedirectView(getSuccessView()));
		
		Collection<Concept> rslt = new Vector<Concept>();
		ConceptSearch cs = new ConceptSearch("");
		
		//exists object conceptsearch? 
		if (session.getAttribute("conceptSearch") != null) {
			cs = (ConceptSearch) session.getAttribute("conceptSearch");
		} else {
			session.setAttribute("conceptSearch", cs);
		}
		
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
		
		mav.addObject("searchResult", rslt);
		
		return mav;
	}
	
	/**
	 * This class returns the form backing object. This can be a string, a boolean, or a normal java
	 * pojo. The type can be set in the /config/moduleApplicationContext.xml file or it can be just
	 * defined by the return type of this method
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		
		HttpSession session = request.getSession();
		
		//Maintain the conceptSearch object
		try {
			ConceptSearch cs = (ConceptSearch) session.getAttribute("conceptSearch");
		}
		catch (ClassCastException ex) {
			session.removeAttribute("conceptSearch");
		}
		
		return "";
	}
	
}
