<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="Manage Concept Name Tags"
	otherwise="/login.htm"
	redirect="/module/conceptsearch/conceptNameTagList.list" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="localHeader.jsp"%>

<openmrs:htmlInclude
	file='${pageContext.request.contextPath}/moduleResources/@MODULE_ID@/scripts/dojoConfig.js' />
<openmrs:htmlInclude
	file='${pageContext.request.contextPath}/moduleResources/@MODULE_ID@/scripts/dojo/dojo.js' />



<h2>
	<spring:message code="conceptsearch.conceptnametaglistheading" />
</h2>

<a href="conceptNameTagForm.form"><spring:message
		code="conceptsearch.addtag" /></a>

<openmrs:extensionPoint
	pointId="org.openmrs.admin.concepts.conceptNameTagList.afterAdd"
	type="html" />

<br />
<br />

<form method="post">
	<div class="box">
		<table id="conceptNameTagTable" cellpadding="2" cellspacing="0">
			<tr>
				<th></th>
				<th><spring:message code="general.name" /></th>
				<th><spring:message code="general.description" /></th>
			</tr>
			<c:forEach var="conceptNameTag" items="${conceptNameTagList}">
				<tr>

					<td valign="top"><input type="checkbox"
						name="conceptNameTagId${conceptNameTag.conceptNameTagId}"
						value="${conceptNameTag.conceptNameTagId}"></td>
					<td valign="top"><a
						href="conceptNameTagForm.form?conceptNameTagId=${conceptNameTag.conceptNameTagId}">
							${conceptNameTag.tag} </a></td>
					<td valign="top">${conceptNameTag.description}</td>
				</tr>
			</c:forEach>
		</table>

		<openmrs:extensionPoint
			pointId="org.openmrs.admin.concepts.conceptNameTagList.inForm"
			type="html" />

		<input type="submit"
			value="<spring:message code="conceptsearch.deletetag"/>"
			name="action">
	</div>
</form>

<openmrs:extensionPoint
	pointId="org.openmrs.admin.concepts.conceptNameTagList.footer"
	type="html" />

<%@ include file="/WEB-INF/template/footer.jsp"%>