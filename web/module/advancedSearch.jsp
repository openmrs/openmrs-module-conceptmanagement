<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<script src="/openmrs/scripts/calendar/calendar.js?v=1.6.0.12685" type="text/javascript" ></script>


<openmrs:require privilege="View Concepts" otherwise="/login.htm" />

<h2><spring:message code="conceptmanagement.advancedheading" /></h2>
<br />
<div style="float:left; width:20%; overflow:auto;">
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
				<input type="checkbox" <c:if test="${fn:contains(conceptSearch.dataTypes, dataType)}"> checked </c:if> name="conceptDatatype" value="${dataType.id}">${dataType.name}<br />
			</c:forEach>
		</td>
	</tr>
	<tr>
		<td valign="top">Classes:</td>
		<td>
			<c:forEach var="class" items="${conceptClasses}">
				<input type="checkbox" <c:if test="${fn:contains(conceptSearch.conceptClasses, class)}"> checked </c:if> name="conceptClasses" value="${class.id}">${class.name}<br />
			</c:forEach>
		</td>
	</tr>
	<tr>
		<td valign="top">Is set?</td>
		<td>
			<input type="checkbox" <c:if test="${conceptSearch.isSet == 1}"> checked </c:if> name="conceptIsSet" value="1" onClick="document.frmSearch.conceptIsSet[1].checked = false";>Yes<br />
			<input type="checkbox" <c:if test="${conceptSearch.isSet == 0}"> checked </c:if> name="conceptIsSet" value="0" onClick="document.frmSearch.conceptIsSet[0].checked = false";>No
		</td>
	</tr>
	<tr>
		<td>Created between:</td>
		<fmt:formatDate var="datFrom" value="${conceptSearch.dateFrom}" pattern="dd/MM/yyyy"/>
		<fmt:formatDate var="datTo" value="${conceptSearch.dateTo}" pattern="dd/MM/yyyy"/>
		<td><input type="text" name="dateFrom" size="10" value="${datFrom}" onClick="showCalendar(this)"> to <input type="text" name="dateTo" size="10" value="${datTo}" onClick="showCalendar(this)"></td>
	</tr>
	<tr>
	<td valign="top">Concepts used as:</td>
	<td>
		<input type="checkbox" name="conceptUsedAs" <c:if test="${fn:contains(conceptSearch.conceptUsedAs, 'formQuestion')}"> checked </c:if> value="formQuestion">as a question in forms<br />
		<input type="checkbox" name="conceptUsedAs" <c:if test="${fn:contains(conceptSearch.conceptUsedAs, 'formAnswer')}"> checked </c:if> value="formAnswer">as an answer to questions<br />
		<input type="checkbox" name="conceptUsedAs" <c:if test="${fn:contains(conceptSearch.conceptUsedAs, 'obsQuestion')}"> checked </c:if> value="obsQuestion">as an observation question<br />
		<input type="checkbox" name="conceptUsedAs" <c:if test="${fn:contains(conceptSearch.conceptUsedAs, 'obsValue')}"> checked </c:if> value="obsValue">as an observation value<br />
	</td>
	</tr>
	<tr>
		<td></td>
		<td><input type="submit" name="action" value="Search"></td>
	</tr>
</table>
</form>
</div>
<div style="width:80%; margin:0px 0px 0px 0px; overflow:auto">
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
		<th><spring:message code="conceptmanagement.concept" /><a href="?sort=name&order=desc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/movedown.gif"></a><a href="?sort=name&order=asc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/moveup.gif"></a></th>
		<th><spring:message code="conceptmanagement.class" /><a href="?sort=class&order=desc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/movedown.gif"></a><a href="?sort=class&order=asc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/moveup.gif"></a></th>
		<th><spring:message code="conceptmanagement.datatype" /><a href="?sort=datatype&order=desc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/movedown.gif"></a><a href="?sort=datatype&order=asc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/moveup.gif"></a></th>
		<!-- <th>Other Names</th> -->
		<th># Obs <a href="?sort=obs&order=desc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/movedown.gif"></a><a href="?sort=obs&order=asc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/moveup.gif"></a></th>
	</tr>
	<c:choose>
	<c:when test="${countConcept.currentPage*countConcept.conceptsPerPage ge fn:length(searchResult)}">
		<c:set var="lastConcept" value="${fn:length(searchResult)}"></c:set>
	</c:when>
	<c:otherwise>
		<c:set var="lastConcept" value="${countConcept.currentPage*countConcept.conceptsPerPage}"></c:set>
	</c:otherwise>
	</c:choose>
	<c:forEach var="concept" begin="${(countConcept.currentPage-1)*countConcept.conceptsPerPage}" end="${lastConcept}" items="${searchResult}">
		<tr bgcolor="#F5F5F5">
			<td><a
				href="viewConcept.form?conceptId=${concept.conceptId}"><spring:message
				code="conceptmanagement.view" /></a></td>
			<td>${concept.conceptName}</td>
			<td>${concept.conceptClass}</td>
			<td>${concept.conceptDatatype}</td>
			<!--  <td>${concept.otherNames}</td> -->
			<td>${concept.numberOfObs}</td>
		</tr>
		<tr>
			<td></td>
			<td colspan="4"><em>${concept.conceptDescription}</em></td>
		</tr>
	</c:forEach>
</table>
<c:if test="${!(searchResult == null || countConcept.conceptsPerPage == -1)}">
<div style="position:relative; left:30px; font-size11px;">
Page:
<c:forEach var="i" begin="1" end="${(fn:length(searchResult) div countConcept.conceptsPerPage)+1}" step="1">
	<a href="?page=${i}">${i}</a>&nbsp;
</c:forEach>
</c:if>
</div>
</div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>
