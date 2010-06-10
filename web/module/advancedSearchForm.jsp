<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="View Concepts" otherwise="/login.htm" />

<h2><spring:message code="conceptmanagement.advancedheading" /></h2>

<br />
<form method="post" class="box">
<table>
	<tr>
		<td>
			Name:
		</td>
		<td>
			<input type="text" name="conceptQuery" size="20" value="${conceptSearch.searchQuery}">
		</td>
	</tr>
	<tr>
		<td>
			Description:
		</td>
		<td>
			<input type="text" name="conceptDescription" size="20" value="${conceptSearch.searchTerms}">
		</td>
	</tr>
	<tr>
		<td>
			Datatype:
		</td>
		<td>
			<select multiple="yes" name="conceptDatatype" size="5">
				<option value="-1"></option>
				<c:forEach var="dataType" items="${dataTypes}">
					<option <c:if test="${fn:contains(conceptSearch.dataTypes, dataType)}"> selected </c:if> value="${dataType.name}">${dataType.name}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td>
			Classes:
		</td>
		<td>
			<select multiple="yes" name="conceptClasses" size="5">
				<option value="-1"></option>
				<c:forEach var="class" items="${conceptClasses}">
					<option <c:if test="${fn:contains(conceptSearch.conceptClasses, class)}"> selected </c:if> value="${class.name}">${class.name}</option>
				</c:forEach>
			</select>
		</td>
	</tr>
	<tr>
		<td>
			Is set?
		</td>
		<td>
			<select name="isSet" size="1">
				<option value="null"></option>
				<option value="yes">Yes</option>
				<option value="no">No</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>
		</td>
		<td>
			<input type="submit" value="<spring:message code="conceptmanagement.go" />">
		</td>
	</tr>
</table>
</form>
<c:if test="${!(searchResult == null)}">
	<dir>
		returned ${fn:length(searchResult)} results
	</dir>
</c:if>
<div class="boxHeader"><b><spring:message
	code="conceptmanagement.concepts" /></b></div>
<div class="box">
<table>
	<tr>
		<th><spring:message code="conceptmanagement.actions" /></th>
		<th><spring:message code="conceptmanagement.concept" /></th>
		<th><spring:message code="conceptmanagement.class" /></th>
		<th><spring:message code="conceptmanagement.datatype" /></th>
	</tr>
	<c:forEach var="concept" items="${searchResult}">
		<tr>
			<td><a
				href="../../dictionary/concept.htm?conceptId=${concept.conceptId}"><spring:message
				code="conceptmanagement.view" /></a></td>
			<td>${concept.names}</td>
			<td>${concept.conceptClass.name}</td>
			<td>${concept.datatype.name}</td>
		</tr>
	</c:forEach>
</table>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>
