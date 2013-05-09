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
package org.openmrs.module.conceptsearch.extension.html;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.module.Extension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * This class defines the links that will appear on the administration page under the Concept
 * Section. file.
 */
public class ConceptFormHeaderExt extends Extension {
	
	/**
	 * @see org.openmrs.module.web.extension.LinkExt#getMediaType()
	 */
	public Extension.MEDIA_TYPE getMediaType() {
		return Extension.MEDIA_TYPE.html;
	}
	
	/**
	 * If this method returns a non-null value then the return value will be used as the default
	 * content for this extension at this extension point
	 * 
	 * @return override content
	 */
	public String getOverrideContent(String bodyContent) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String conceptId = request.getParameter("conceptId");
		if (conceptId != null) {
			return ("<div><a href=\"/openmrs/module/conceptsearch/manageConceptName.form?conceptId=" + conceptId + "\" >Edit Name Tags</a></div>");
		} else
			return ("");
	}
	
}
