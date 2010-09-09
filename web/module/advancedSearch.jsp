<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<link rel="stylesheet" type="text/css" href="/openmrs/scripts/jquery/autocomplete/jquery.autocomplete.css" />

<script src="/openmrs/scripts/calendar/calendar.js?v=1.6.0.12685" type="text/javascript"></script>
<script src="/openmrs/scripts/jquery/jquery-1.3.2.min.js" type="text/javascript"></script>
<script src="/openmrs/scripts/jquery/autocomplete/jquery.autocomplete.js" type="text/javascript"></script>



<openmrs:require privilege="View Concepts" otherwise="/login.htm" />

<h2><spring:message code="conceptmanagement.advancedheading" /></h2>
<br />
<div style="float:left; width:20%; overflow:auto;">
<form method="post" class="box" name="frmSearch">
<table>
	<tr>
		<td><spring:message code="general.name" />:</td>
		<td><input id="conceptQuery" type="text" name="conceptQuery" size="20"
			value="${conceptSearch.searchQuery}">
			<script>
				jQuery(document).ready(function() {
					$("#conceptQuery").autocomplete("/openmrs/module/conceptsearch/autocomplete.form");
				});
			</script>
		</td>
	</tr>
	<tr>
		<td><spring:message code="general.description" />:</td>
		<td><input type="text" name="conceptDescription" size="20"
			value="${conceptSearch.searchTerms}"></td>
	</tr>
	<tr>
		<td valign="top"><spring:message code="Concept.datatype" />:</td>
		<td>
			<c:forEach var="dataType" items="${dataTypes}">
				<input type="checkbox" <c:if test="${fn:contains(conceptSearch.dataTypes, dataType)}"> checked </c:if> name="conceptDatatype" value="${dataType.id}">${dataType.name}<br />
			</c:forEach>
		</td>
	</tr>
	<tr>
		<td valign="top"><spring:message code="Concept.conceptClass" />:</td>
		<td>
			<c:forEach var="class" items="${conceptClasses}">
				<input type="checkbox" <c:if test="${fn:contains(conceptSearch.conceptClasses, class)}"> checked </c:if> name="conceptClasses" value="${class.id}">${class.name}<br />
			</c:forEach>
		</td>
	</tr>
	<tr>
		<td valign="top"><spring:message code="conceptmanagement.isset" /></td>
		<td>
			<input type="checkbox" <c:if test="${conceptSearch.isSet == 1}"> checked </c:if> name="conceptIsSet" value="1" onClick="document.frmSearch.conceptIsSet[1].checked = false";><spring:message code="general.yes" /><br />
			<input type="checkbox" <c:if test="${conceptSearch.isSet == 0}"> checked </c:if> name="conceptIsSet" value="0" onClick="document.frmSearch.conceptIsSet[0].checked = false";><spring:message code="general.no" />
		</td>
	</tr>
	<tr>
		<td><spring:message code="conceptmanagement.createdbetween" />:</td>
		<fmt:formatDate var="datFrom" value="${conceptSearch.dateFrom}" pattern="dd/MM/yyyy"/>
		<fmt:formatDate var="datTo" value="${conceptSearch.dateTo}" pattern="dd/MM/yyyy"/>
		<td><input type="text" name="dateFrom" size="10" value="${datFrom}" onClick="showCalendar(this)"> <spring:message code="conceptmanagement.todate" /> <input type="text" name="dateTo" size="10" value="${datTo}" onClick="showCalendar(this)"></td>
	</tr>
	<tr>
	<td valign="top"><spring:message code="conceptmanagement.conceptsusedas" />:</td>
	<td>
		<input type="checkbox" name="conceptUsedAs" <c:if test="${fn:contains(conceptSearch.conceptUsedAs, 'formQuestion')}"> checked </c:if> value="formQuestion"><spring:message code="conceptmanagement.asformquestion" /><br />
		<input type="checkbox" name="conceptUsedAs" <c:if test="${fn:contains(conceptSearch.conceptUsedAs, 'formAnswer')}"> checked </c:if> value="formAnswer"><spring:message code="conceptmanagement.asformanswer" /><br />
		<input type="checkbox" name="conceptUsedAs" <c:if test="${fn:contains(conceptSearch.conceptUsedAs, 'obsQuestion')}"> checked </c:if> value="obsQuestion"><spring:message code="conceptmanagement.asobsquestion" /><br />
		<input type="checkbox" name="conceptUsedAs" <c:if test="${fn:contains(conceptSearch.conceptUsedAs, 'obsValue')}"> checked </c:if> value="obsValue"><spring:message code="conceptmanagement.asobsvalue" /><br />
	</td>
	</tr>
	<tr>
		<td></td>
		<td><input type="submit" name="action" value="<spring:message code="general.search" />"></td>
	</tr>
</table>
</form>
</div>
<div style="width:80%; margin:0px 0px 0px 0px; overflow:auto">
<form name="frmConceptCount"><div style="position:absolute; right:40px;">Show <select name="conceptsPerPage" size="1" OnChange="location.href='advancedSearch.form?count='+frmConceptCount.conceptsPerPage.options[selectedIndex].value";>
							<option <c:if test="${countConcept.conceptsPerPage==25}"> selected </c:if> value="25">25</option>
							<option <c:if test="${countConcept.conceptsPerPage==50}"> selected </c:if> value="50">50</option>
							<option <c:if test="${countConcept.conceptsPerPage==100}"> selected </c:if> value="100">100</option>
							<option <c:if test="${countConcept.conceptsPerPage==10000}"> selected </c:if> value="-1"><spring:message code="conceptmanagement.all" /></option>
						</select><spring:message code="conceptmanagement.conceptsperpage" /></div></form>
<div class="boxHeader"><b><spring:message code="conceptmanagement.searchresults" /></b>&nbsp;<c:if test="${!(searchResult == null)}">(${fn:length(searchResult)} <spring:message code="conceptmanagement.resultsreturned" />)</c:if></div>
<div class="box">
<table>
	<tr>
		<th><spring:message code="conceptmanagement.actions" /></th>
		<th><spring:message code="Concept" /><a href="?sort=name&order=desc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/movedown.gif"></a><a href="?sort=name&order=asc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/moveup.gif"></a></th>
		<th><spring:message code="Concept.conceptClass" /><a href="?sort=class&order=desc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/movedown.gif"></a><a href="?sort=class&order=asc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/moveup.gif"></a></th>
		<th><spring:message code="Concept.datatype" /><a href="?sort=datatype&order=desc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/movedown.gif"></a><a href="?sort=datatype&order=asc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/moveup.gif"></a></th>
		<!-- <th>Other Names</th> -->
		<th><spring:message code="conceptmanagement.numberofobs" /><a href="?sort=obs&order=desc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/movedown.gif"></a><a href="?sort=obs&order=asc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/moveup.gif"></a></th>
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
				code="general.view" /></a></td>
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
<spring:message code="conceptmanagement.page" />:
<c:forEach var="i" begin="1" end="${(fn:length(searchResult) div countConcept.conceptsPerPage)+1}" step="1">
	<a href="?page=${i}">${i}</a>&nbsp;
</c:forEach>
</c:if>
</div>
</div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>
