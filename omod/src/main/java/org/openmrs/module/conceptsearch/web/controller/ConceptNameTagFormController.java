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
package org.openmrs.module.conceptsearch.web.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.ConceptNameTag;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.web.WebConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

@Controller
public class ConceptNameTagFormController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	/** Validator for this controller */
	private ConceptNameTagValidator validator;
	
	@Autowired
	public ConceptNameTagFormController(ConceptNameTagValidator validator) {
		this.validator = validator;
	}
	
	@ModelAttribute("tag")
	public String getTag(@RequestParam(value = "tag", required = false) String tag) {
		return (tag == null ? "" : tag);
	}
	
	@InitBinder("tag")
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.setAllowedFields(new String[] { "tag" });
		dataBinder.setRequiredFields(new String[] { "tag" });
	}
	
	@RequestMapping(value = "/module/conceptsearch/conceptNameTagForm", method = RequestMethod.POST)
	protected String onSubmit(@ModelAttribute("conceptNameTag") ConceptNameTag conceptNameTag, BindingResult errors,
	                          ModelMap model, WebRequest request, HttpSession session) {
		
		validator.validate(conceptNameTag, errors);
		if (errors.hasErrors()) {
			session.setAttribute("conceptNameTag", conceptNameTag);
			model.addAttribute("conceptNameTag", conceptNameTag);
			return "/module/conceptsearch/conceptNameTagForm";
		} else {
			ConceptNameTag newTag = new ConceptNameTag();
			String tagDescription = request.getParameter("description");
			String tagName = request.getParameter("tag");
			
			newTag.setTag(tagName);
			newTag.setDescription(tagDescription);
			Context.getConceptService().saveConceptNameTag(newTag);
			session.setAttribute(WebConstants.OPENMRS_MSG_ATTR, "ConceptNameTag.saved");
			session.setAttribute("conceptNameTag", newTag);
			model.addAttribute("conceptNameTag", newTag);
			return "redirect:/module/conceptsearch/conceptNameTagList.list";
			
		}
		
	}
	
	@RequestMapping(value = "/module/conceptsearch/conceptNameTagForm", method = RequestMethod.GET, params = "conceptId")
	public void displayConceptNameTagFormWithId(ModelMap model, WebRequest request, HttpSession session) {
		
		ConceptNameTag conceptNameTag = null;
		ConceptService cs = Context.getConceptService();
		String conceptNameTagId = request.getParameter("conceptNameTagId");
		if (conceptNameTagId != null)
			conceptNameTag = cs.getConceptNameTag(Integer.valueOf(conceptNameTagId));
		
		if (conceptNameTag == null)
			conceptNameTag = new ConceptNameTag();
		
		session.setAttribute("conceptNameTag", conceptNameTag);
		model.addAttribute("conceptNameTag", conceptNameTag);
	}
	
	@RequestMapping(value = "/module/conceptsearch/conceptNameTagForm", method = RequestMethod.GET)
	public void displayConceptNameTagForm(ModelMap model, WebRequest request, HttpSession session) {
		
		ConceptNameTag conceptNameTag = null;
		ConceptService cs = Context.getConceptService();
		String conceptNameTagId = request.getParameter("conceptNameTagId");
		if (conceptNameTagId != null)
			conceptNameTag = cs.getConceptNameTag(Integer.valueOf(conceptNameTagId));
		
		if (conceptNameTag == null)
			conceptNameTag = new ConceptNameTag();
		
		session.setAttribute("conceptNameTag", conceptNameTag);
		model.addAttribute("conceptNameTag", conceptNameTag);
	}
	
}
