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

import java.util.Comparator;
import org.openmrs.Concept;

/**
 * This class is made for a sorting-routine to determine which concept has a lower alphanumeric
 * value. Can also be used on concepts datatype and class.
 */
public class ConceptComparator implements Comparator<Concept> {
	
	private static final int SORT_FOR_NAME = 0;
	
	private static final int SORT_FOR_DATATYPE = 1;
	
	private static final int SORT_FOR_CLASS = 2;
	
	private int ascSort;
	
	private int descSort;
	
	private int sortFor;
	
	public ConceptComparator(String sortCrit, boolean asc) {
		if (sortCrit.equals("class"))
			this.sortFor = SORT_FOR_CLASS;
		else if (sortCrit.equals("datatype"))
			this.sortFor = SORT_FOR_DATATYPE;
		else
			this.sortFor = SORT_FOR_NAME;
		
		if (asc) {
			this.ascSort = 1;
			this.descSort = 0;
		} else {
			this.ascSort = 0;
			this.descSort = 1;
		}
		
	}
	
	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Concept arg0, Concept arg1) {
		switch (sortFor) {
			case SORT_FOR_DATATYPE:
				if ((arg0.getDatatype().getName().toString().compareToIgnoreCase(arg1.getDatatype().getName().toString())) > 0)
					return ascSort;
				else
					return descSort;
			case SORT_FOR_CLASS:
				if ((arg0.getConceptClass().getName().toString().compareToIgnoreCase(arg1.getConceptClass().getName()
				        .toString())) > 0)
					return ascSort;
				else
					return descSort;
			default:
				if ((arg0.getDisplayString().compareToIgnoreCase(arg1.getDisplayString())) > 0)
					return ascSort;
				else
					return descSort;
		}
	}
	
}
