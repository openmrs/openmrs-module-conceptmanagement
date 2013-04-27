<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Concept Name Tags"
	otherwise="/login.htm"
	redirect="/module/conceptsearch/conceptNameTag.list" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="localHeader.jsp"%>

<openmrs:htmlInclude
	file='${pageContext.request.contextPath}/moduleResources/@MODULE_ID@/scripts/dojoConfig.js' />
<openmrs:htmlInclude
	file='${pageContext.request.contextPath}/moduleResources/@MODULE_ID@/scripts/dojo/dojo.js' />

<style>
#table {
	width: 100%;
}

#table th {
	text-align: left;
}

#table input[name=name],input#concept_selection {
	width: 99%;
}

h1,h2,h3,h4,h5 {
	margin-top: 5px;
	margin-bottom: 7px;
}

* {
	font-family: Verdana, 'Lucida Grande', 'Trebuchet MS', Arial, Sans-Serif;
	-moz-box-sizing: border-box; /* Use IE-like Border-Box model */
}

.retired,.voided,.retired *,.voided * {
	text-decoration: line-through;
}
</style>
<script type="text/javascript">
	dojo.addOnLoad(function() {
		toggleRowVisibilityForClass("conceptNameTagTable", "retired", false);
	})
</script>

<h2>
	<spring:message code="conceptsearch.conceptnametaglistheading" />
</h2>

<a href="conceptNameTag.form"><spring:message
		code="conceptsearch.addtag" /></a>

<openmrs:extensionPoint
	pointId="org.openmrs.admin.concepts.conceptNameTagList.afterAdd"
	type="html" />

<br />
<br />
<b class="boxHeader"> <a style="display: block; float: right"
	href="#"
	onClick="return toggleRowVisibilityForClass('conceptNameTagTable', 'retired', false);">
		<openmrs:message code="general.toggle.retired" />
</a> <spring:message code="conceptsearch.taglisttitle" /> 
</b>
<form method="post">
<div class="box">
	<table id="conceptNameTagTable" cellpadding="2" cellspacing="0">
		<tr>
			<th></th>
			<th><spring:message code="general.name" /></th>
			<th><spring:message code="general.description" /></th>
		</tr>
			<c:forEach var="conceptNameTag" items="${conceptNameTagList}">
		<tr class="<c:if test="${conceptNameTag.voided}">retired </c:if>">
				
				<td valign="top"><input type="checkbox" name="conceptNameTagId"
					value="${conceptNameTag.conceptNameTagId}"></td>
				<td valign="top"><a
					href="conceptNameTag.form?conceptNameTagId=${conceptNameTag.conceptNameTagId}">
						${conceptNameTag.tag} </a></td>
				<td valign="top">${conceptNameTag.description}</td>
		</tr>
		</c:forEach>
	</table>

	<openmrs:extensionPoint
		pointId="org.openmrs.admin.concepts.conceptNameTagList.inForm"
		type="html" />

	<input type="submit"
		value="<spring:message code="conceptsearch.deletetag"/>" name="action">
</div>
</form>

<openmrs:extensionPoint
	pointId="org.openmrs.admin.concepts.conceptNameTagList.footer"
	type="html" />

<%@ include file="/WEB-INF/template/footer.jsp"%>