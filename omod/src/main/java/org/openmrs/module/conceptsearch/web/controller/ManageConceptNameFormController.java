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
 * Copyright (C) OpenMRS, LLC. All Rights Reserved.
 */
package org.openmrs.module.conceptsearch.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpSession;
import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.ConceptNameTag;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.conceptsearch.ConceptSearchResult;
import org.openmrs.module.conceptsearch.ConceptSearchService;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

/**
 * Controller to handle edit of concept names (only tags for now).
 */
@Controller
public class ManageConceptNameFormController extends AbstractSearchFormController {
	
	@ModelAttribute("tagnameQuery")
	public String getTagnameQuery(@RequestParam(value = "tagnameQuery", required = false) String tagnameQuery) {
		return (tagnameQuery == null ? "" : tagnameQuery);
	}
	
	@InitBinder("tagnameQuery")
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.setAllowedFields(new String[] { "tagnameQuery" });
		dataBinder.setRequiredFields(new String[] { "tagnameQuery" });
		
	}
	
	@RequestMapping(value = "/module/conceptsearch/manageConceptName", method = RequestMethod.POST)
	protected void performSaveorDelete(@ModelAttribute("tagnameQuery") String searchQuery, BindingResult errors,
	                                   ModelMap model, WebRequest request, HttpSession session) {
		
		ConceptService cs = Context.getConceptService();
		ConceptSearchService searchService = (ConceptSearchService) Context.getService(ConceptSearchService.class);
		
		String id = request.getParameter("conceptId");
		int cid = Integer.parseInt(id);
		Concept concept = searchService.getConcept(cid);
		
		if (request.getParameter("deleteOrSave") != null && request.getParameter("deleteOrSave").contains("save")) {
			int i = 0;
			for (ConceptName cn : concept.getNames()) {
				if (request.getParameter("saveTagWithConceptName" + cn) != null) {
					String tagNameQueryString = "tagnameQuery" + cn;
					if (request.getParameter(tagNameQueryString).length() > 0) {
						String tagName = request.getParameter(tagNameQueryString);
						ConceptNameTag tag = cs.getConceptNameTagByName(tagName);
						if (tag != null) {
							cn.addTag(tag);
							cs.saveConcept(concept);
							
						} else {
							errors.reject("Tag not found", "Tag not found");
						}
					}
				}
				i++;
			}
			
		} else if (request.getParameter("deleteOrSave") != null && request.getParameter("deleteOrSave").contains("delete")) {
			for (ConceptName cn : concept.getNames()) {
				int i = 0;
				boolean del = false;
				List<String> tags = new Vector<String>();
				for (ConceptNameTag cnt : cn.getTags()) {
					if (request.getParameter("hiddenAction" + cn.getName() + i).length() > 0) {
						del = true;
						String tagName = request.getParameter("hiddenAction" + cn.getName() + i);
						tags.add(tagName);
						
					}
					i++;
					
				}
				if (del) {
					for (String cntName : tags) {
						ConceptNameTag tag = cs.getConceptNameTagByName(cntName);
						cn.removeTag(tag);
					}
					cs.saveConcept(concept);
				}
			}
			
		}
		if (request.getParameter("sort") != null && request.getParameter("order") != null) {
			String sort = request.getParameter("sort");
			String order = request.getParameter("order");
			String conceptId = request.getParameter("conceptId");
			displaySortedConceptEditPage(sort, order, conceptId, model, request, session);
			
		} else {
			displayConceptEditPage(model, request, session);
		}
		
	}
	
	@RequestMapping(value = "/module/conceptsearch/manageConceptName", method = { RequestMethod.GET }, params = { "sort",
	        "order", "conceptId" })
	public void sortResultsView(@RequestParam("sort") String sort, @RequestParam("order") String order,
	                            @RequestParam("conceptId") String conceptId, ModelMap model, WebRequest request,
	                            HttpSession session) {
		super.sortResultsView(sort, order, conceptId, model, request, session);
		
	}
	
	public void displaySortedConceptEditPage(@RequestParam("sort") String sort, @RequestParam("order") String order,
	                                         @RequestParam("conceptId") String conceptId, ModelMap model,
	                                         WebRequest request, HttpSession session) {
		ConceptSearchService searchService = (ConceptSearchService) Context.getService(ConceptSearchService.class);
		String id = request.getParameter("conceptId");
		int cid = Integer.parseInt(id);
		
		Concept concept = searchService.getConcept(cid);
		List<ConceptSearchResult> resList = new ArrayList<ConceptSearchResult>();
		
		if (concept != null) {
			for (ConceptName cn : concept.getNames()) {
				ConceptSearchResult res = new ConceptSearchResult(cn);
				resList.add(res);
			}
			
		}
		sortResultsView(sort, order, conceptId, model, request, session);
		// add results to ListHolder
		PagedListHolder resListHolder = new PagedListHolder(resList);
		resListHolder.setPageSize(DEFAULT_RESULT_PAGE_SIZE);
		
		model.addAttribute("searchResult", resListHolder);
		session.setAttribute("sortResults", resListHolder);
		model.addAttribute("concept", concept);
		sortResultsView(sort, order, conceptId, model, request, session);
	}
	
	@RequestMapping(value = "/module/conceptsearch/manageConceptName", method = RequestMethod.GET, params = "conceptId")
	public void displayConceptEditPage(ModelMap model, WebRequest request, HttpSession session) {
		ConceptSearchService searchService = (ConceptSearchService) Context.getService(ConceptSearchService.class);
		String id = request.getParameter("conceptId");
		int cid = Integer.parseInt(id);
		
		Concept concept = searchService.getConcept(cid);
		List<ConceptSearchResult> resList = new ArrayList<ConceptSearchResult>();
		
		if (concept != null) {
			for (ConceptName cn : concept.getNames()) {
				ConceptSearchResult res = new ConceptSearchResult(cn);
				resList.add(res);
			}
			
		}
		// add results to ListHolder
		PagedListHolder resListHolder = new PagedListHolder(resList);
		resListHolder.setPageSize(DEFAULT_RESULT_PAGE_SIZE);
		
		model.addAttribute("searchResult", resListHolder);
		session.setAttribute("sortResults", resListHolder);
		model.addAttribute("concept", concept);
		
	}
	
	@RequestMapping(value = "/module/conceptsearch/autocompletenametag", method = RequestMethod.GET)
	public void doAutocomplete(ModelMap model, WebRequest request, HttpSession session) {
		
		// -- Autocompletehelper is used to avoid some problems --
		log.debug("Accessing autocomplete");
	}
	
}
