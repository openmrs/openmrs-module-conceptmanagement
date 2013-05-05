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

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.ConceptNameTag;
import org.openmrs.api.APIException;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.conceptsearch.ConceptSearchResult;
import org.openmrs.module.conceptsearch.ConceptSearchService;
import org.openmrs.web.WebConstants;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controller to handle edit of concept names (only tags for now).
 */
@Controller
public class ConceptNameTagListController {
	
	/** Logger for this class and subclasses */
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/module/conceptsearch/conceptNameTagList", method = RequestMethod.POST)
	protected void onSubmit(ModelMap model, WebRequest request, HttpSession session) {
		String error = "";
		ConceptService cs = Context.getConceptService();
		ConceptSearchService css = (ConceptSearchService) Context.getService(ConceptSearchService.class);
		
		try {
			
			boolean del = false;
			List<String> tags = new Vector<String>();
			for (ConceptNameTag cnt : cs.getAllConceptNameTags()) {
				if (request.getParameter("conceptNameTagId" + cnt.getId()) != null
				        && request.getParameter("conceptNameTagId" + cnt.getId()).length() > 0) {
					del = true;
					String tagId = request.getParameter("conceptNameTagId" + cnt.getId());
					tags.add(tagId);
					
				}
			}
			
			if (del) {
				del = false;
				for (String cntId : tags) {
					ConceptNameTag tag = cs.getConceptNameTag(Integer.valueOf(cntId));
					for (Concept concept : cs.getAllConcepts()) {
						for (ConceptName cn : concept.getNames()) {
							List<ConceptNameTag> tagsToRemove = new Vector<ConceptNameTag>();
							for (ConceptNameTag nameTag : cn.getTags()) {
								if (nameTag.getId().intValue() == tag.getId().intValue()) {
									tagsToRemove.add(tag);
									del = true;
								}
							}
							if (del) {
								for (ConceptNameTag tempTag : tagsToRemove) {
									cn.removeTag(tempTag);
								}
							}
						}
						if (del) {
							cs.saveConcept(concept);
							del = false;
						}
					}
					
					log.info("Deleting conceptnametag Uuid: " + tag.getUuid() + " tagName " + tag.getTag() + " user id: "
					        + Context.getAuthenticatedUser().getUserId() + " username:  "
					        + Context.getAuthenticatedUser().getUsername());
					css.purgeConceptNameTag(tag);
					
				}
				
			} else {
				error = "ConceptNameTag.select";
			}
			
		}
		
		catch (DataIntegrityViolationException e) {
			error = handleConceptNameTagIntegrityException(e, error, "not deleted");
		}
		catch (APIException e) {
			error = handleConceptNameTagIntegrityException(e, error, "not deleted");
		}
		//default empty Object
		List<ConceptNameTag> conceptNameTagList = new Vector<ConceptNameTag>();
		conceptNameTagList = cs.getAllConceptNameTags();
		model.addAttribute("conceptNameTagList", conceptNameTagList);
		session.setAttribute("conceptNameTagList", conceptNameTagList);
	}
	
	/**
	 * Logs a concept name tag delete data integrity violation exception and returns a user friedly
	 * message of the problem that occured.
	 * 
	 * @param e the exception.
	 * @param error the error message.
	 * @param notDeleted the not deleted error message.
	 * @return the formatted error message.
	 */
	private String handleConceptNameTagIntegrityException(Exception e, String error, String notDeleted) {
		log.warn("Error deleting concept class", e);
		if (!error.equals(""))
			error += "<br/>";
		error += notDeleted;
		return error;
	}
	
	@RequestMapping(value = "/module/conceptsearch/conceptNameTagList", method = RequestMethod.GET)
	public void displayConceptNameTagListPage(ModelMap model, WebRequest request, HttpSession session) {
		//default empty Object
		List<ConceptNameTag> conceptNameTagList = new Vector<ConceptNameTag>();
		
		ConceptService cs = Context.getConceptService();
		conceptNameTagList = cs.getAllConceptNameTags();
		model.addAttribute("conceptNameTagList", conceptNameTagList);
		session.setAttribute("conceptNameTagList", conceptNameTagList);
	}
	
}
