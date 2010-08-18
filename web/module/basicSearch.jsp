<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="View Concepts" otherwise="/login.htm" />

<link rel="stylesheet" type="text/css" href="/openmrs/scripts/jquery/autocomplete/jquery.autocomplete.css" />

<script src="/openmrs/scripts/jquery/jquery-1.3.2.min.js" type="text/javascript"></script>
<script src="/openmrs/scripts/jquery/autocomplete/jquery.autocomplete.js" type="text/javascript"></script>

<h2><spring:message code="conceptmanagement.basicheading" /></h2>

<br />
<form method="post" class="box">
<table>
	<tr>
		<td><input id="conceptQuery" type="text" name="conceptQuery" size="20"
			value="${conceptSearch.searchQuery}">
			<script>
				$("#conceptQuery").autocomplete("/openmrs/module/conceptmanagement/autocomplete.form");
			</script></td>
		<td><input type="submit"
			value="<spring:message code="general.search" />"></td>
	</tr>
</table>
</form>
<c:if test="${!(searchResult == null)}">
	<dir>
		${fn:length(searchResult)} <spring:message code="conceptmanagement.resultsreturned" />
	</dir>
</c:if>
<form name="frmConceptCount"><div style="position:absolute; right:40px;">Show <select name="conceptsPerPage" size="1" OnChange="location.href='basicSearch.form?count='+frmConceptCount.conceptsPerPage.options[selectedIndex].value";>
							<option <c:if test="${countConcept.conceptsPerPage==25}"> selected </c:if> value="25">25</option>
							<option <c:if test="${countConcept.conceptsPerPage==50}"> selected </c:if> value="50">50</option>
							<option <c:if test="${countConcept.conceptsPerPage==100}"> selected </c:if> value="100">100</option>
							<option <c:if test="${countConcept.conceptsPerPage==10000}"> selected </c:if> value="-1"><spring:message code="conceptmanagement.all" /></option>
						</select><spring:message code="conceptmanagement.conceptsperpage" /></div></form>
<div class="boxHeader"><b><spring:message
	code="conceptmanagement.concepts" /></b></div>
<div class="box">
<table>
	<tr>
		<th><spring:message code="conceptmanagement.actions" /></th>
		<th><spring:message code="Concept" /></th>
		<th><spring:message code="Concept.conceptClass" /></th>
		<th><spring:message code="Concept.datatype" /></th>
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
		<tr>
			<td><a
				href="viewConcept.form?conceptId=${concept.conceptId}"><spring:message
				code="general.view" /></a></td>
			<td>${concept.names}</td>
			<td>${concept.conceptClass.name}</td>
			<td>${concept.datatype.name}</td>
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

<%@ include file="/WEB-INF/template/footer.jsp"%>
