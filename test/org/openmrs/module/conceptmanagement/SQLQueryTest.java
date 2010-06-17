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

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseContextSensitiveTest;
import org.openmrs.test.Verifies;

/**
 * TestCase to test the SQL-query command
 */
public class SQLQueryTest extends BaseContextSensitiveTest {
	
	@Test
	@Verifies(value = "should find a concept in the db", method = "executeSQL(sql, false)")
	public void testConceptQuery() {
		AdministrationService adminService = Context.getAdministrationService();
		
		Assert.assertNotNull(adminService);
		
		List<List<Object>> conc = adminService.executeSQL("SELECT count(*) FROM obs", false);
		
		Assert.assertNotNull(conc);
		
	}
	
}
