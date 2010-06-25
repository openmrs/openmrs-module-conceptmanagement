<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<script src="/openmrs/scripts/calendar/calendar.js?v=1.6.0.12685" type="text/javascript" ></script>

<openmrs:require privilege="View Concepts" otherwise="/login.htm" />

<h2><spring:message code="conceptmanagement.advancedheading" /></h2>

<br />
<form method="post" class="box" name="frmSearch">
<table>
	<tr>
		<td>Name:</td>
		<td><input type="text" name="conceptQuery" size="20"
			value="${conceptSearch.searchQuery}"></td>
	</tr>
	<tr>
		<td>Description:</td>
		<td><input type="text" name="conceptDescription" size="20"
			value="${conceptSearch.searchTerms}"></td>
	</tr>
	<tr>
		<td valign="top">Datatype:</td>
		<td>
			<c:forEach var="dataType" items="${dataTypes}">
				<input type="checkbox" <c:if test="${fn:contains(conceptSearch.dataTypes, dataType)}"> checked </c:if> name="conceptDatatype" value="${dataType.name}">${dataType.name}<br />
			</c:forEach>
		</td>
	</tr>
	<tr>
		<td valign="top">Classes:</td>
		<td>
			<c:forEach var="class" items="${conceptClasses}">
				<input type="checkbox" <c:if test="${fn:contains(conceptSearch.conceptClasses, class)}"> checked </c:if> name="conceptClasses" value="${class.name}">${class.name}<br />
			</c:forEach>
		</td>
	</tr>
	<tr>
		<td valign="top">Is set?</td>
		<td>
			<input type="radio" name="conceptIsSet" value="-1">Deselect<br />
			<input type="radio" <c:if test="${conceptSearch.isSet == 1}"> checked </c:if> name="conceptIsSet" value="1">Yes<br />
			<input type="radio" <c:if test="${conceptSearch.isSet == 0}"> checked </c:if> name="conceptIsSet" value="0">No
		</td>
	</tr>
	<tr>
		<td>Created between:</td>
		<td><input type="text" name="dateFrom" size="8" onClick="showCalendar(this)"> to <input type="text" name="dateTo" size="8" onClick="showCalendar(this)"></td>
	</tr>
	<tr>
	<td valign="top">Concepts used as:</td>
	<td>
		<input type="checkbox" name="conceptUsedAs" value="formQuestion">as a question in forms<br />
		<input type="checkbox" name="conceptUsedAs" value="formAnswer">as an answer to questions<br />
		<input type="checkbox" name="conceptUsedAs" value="ObsQuestion">as an observation question<br />
		<input type="checkbox" name="conceptUsedAs" value="ObsValue">as an observation value<br />
	</td>
	</tr>
	<tr>
		<td></td>
		<td><input type="submit" name="action" value="Search"></td>
	</tr>
</table>
</form>
<form name="frmConceptCount"><div style="position:absolute; right:40px;">Show <select name="conceptsPerPage" size="1" OnChange="location.href='advancedSearch.form?count='+frmConceptCount.conceptsPerPage.options[selectedIndex].value";>
							<option <c:if test="${countConcept.conceptsPerPage==25}"> selected </c:if> value="25">25</option>
							<option <c:if test="${countConcept.conceptsPerPage==50}"> selected </c:if> value="50">50</option>
							<option <c:if test="${countConcept.conceptsPerPage==100}"> selected </c:if> value="100">100</option>
							<option <c:if test="${countConcept.conceptsPerPage==10000}"> selected </c:if> value="-1">All</option>
						</select>concepts per page</div></form>
<div class="boxHeader"><b>Search Results</b>&nbsp;<c:if test="${!(searchResult == null)}">(${fn:length(searchResult)} results returned)</c:if></div>
<div class="box">
<table>
	<tr>
		<th><spring:message code="conceptmanagement.actions" /></th>
		<th><spring:message code="conceptmanagement.concept" /><a href="?sort=name&order=asc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/movedown.gif"></a><a href="?sort=name&order=desc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/moveup.gif"></a></th>
		<th><spring:message code="conceptmanagement.class" /><a href="?sort=class&order=asc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/movedown.gif"></a><a href="?sort=class&order=desc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/moveup.gif"></a></th>
		<th><spring:message code="conceptmanagement.datatype" /><a href="?sort=datatype&order=asc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/movedown.gif"></a><a href="?sort=datatype&order=desc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/moveup.gif"></a></th>
		<th>Other Names</th>
	</tr>
	<c:choose>
	<c:when test="${countConcept.currentPage*countConcept.conceptsPerPage ge fn:length(searchResult)}">
		<c:set var="lastConcept" value="${fn:length(searchResult)}"></c:set>
		${(countConcept.currentPage-1)*countConcept.conceptsPerPage} to ${lastConcept}
	</c:when>
	<c:otherwise>
		<c:set var="lastConcept" value="${countConcept.currentPage*countConcept.conceptsPerPage}"></c:set>
		${(countConcept.currentPage-1)*countConcept.conceptsPerPage} to ${lastConcept}
	</c:otherwise>
	</c:choose>
	<c:forEach var="concept" begin="${(countConcept.currentPage-1)*countConcept.conceptsPerPage}" end="${lastConcept}" items="${searchResult}">
		<tr>
			<td><a
				href="../../dictionary/concept.htm?conceptId=${concept.conceptId}"><spring:message
				code="conceptmanagement.view" /></a></td>
			<td>${concept.name}</td>
			<td>${concept.conceptClass.name}</td>
			<td>${concept.datatype.name}</td>
			<td>${concept.names}</td>
		</tr>
	</c:forEach>
</table>
</div>
<c:if test="${!(searchResult == null || countConcept.conceptsPerPage == -1)}">
<div style="position:relative; left:30px; font-size11px;">
Page:
<c:forEach var="i" begin="1" end="${(fn:length(searchResult) div countConcept.conceptsPerPage)+1}" step="1">
	<a href="?page=${i}">${i}</a>&nbsp;
</c:forEach>
</c:if>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>
