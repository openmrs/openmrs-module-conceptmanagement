<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:require privilege="Manage Concept Name Tags" otherwise="/login.htm" redirect="/module/conceptsearch/conceptNameTag.form" />

<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="localHeader.jsp" %>
<style>
	#table { width: 100%; }
	#table th { text-align: left; }
	#table input[name=name], input#concept_selection { width: 99%; }
h1, h2, h3, h4, h5 {
	margin-top: 5px;
	margin-bottom: 7px;
}
* {
	font-family: Verdana, 'Lucida Grande', 'Trebuchet MS', Arial, Sans-Serif;
    -moz-box-sizing: border-box;  /* Use IE-like Border-Box model */
}
</style>

<h2><spring:message code="conceptsearch.conceptnametaglistheading"/></h2>

<openmrs:extensionPoint pointId="org.openmrs.admin.concepts.conceptNameTagForm.afterTitle" type="html" parameters="conceptNameTagId=${conceptNameTag.conceptNameTagId}" />

<c:if test="${conceptNameTag.voided}">
<form action="" method="post">
	<div class="retiredMessage">
	<div>
	<openmrs:message code="conceptsearch.retiredNameTagMessage"/>
	${conceptNameTag.voidedBy.personName}
				<openmrs:formatDate date="${conceptNameTag.dateVoided}" type="medium" />
				-
				${conceptNameTag.voidReason}
				<input type="submit" value='<openmrs:message code="conceptsearch.unretireNameTag"/>' name="unretireNameTag"/>
			
	</div>
	</div>
	</form>
</c:if>

<form method="post">
<fieldset>
<table>
	<tr>
		<td><spring:message code="general.name"/></td>
		<td>
		<spring:bind path="conceptNameTag.tag">
				<input type="text" name="tag" value="${status.value}" size="35" />
			<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>	
		</spring:bind>
		</td>
	</tr>
	<tr>
		<td valign="top"><spring:message code="general.description"/></td>
		<td>
			<spring:bind path="conceptNameTag.description">
				<textarea name="description" rows="3" cols="40">${status.value}</textarea>
				<c:if test="${status.errorMessage != ''}"><span class="error">${status.errorMessage}</span></c:if>
			</spring:bind>
		</td>
	</tr>
	<c:if test="${!(conceptNameTag.creator == null)}">
		<tr>
			<td><spring:message code="general.createdBy" /></td>
			<td>
				${conceptNameTag.creator.personName} -
				<openmrs:formatDate date="${conceptNameTag.dateCreated}" type="long" />
			</td>
		</tr>
	</c:if>
</table>
<openmrs:extensionPoint pointId="org.openmrs.admin.concepts.conceptNameTagForm.inForm" type="html" parameters="conceptNameTagId=${conceptNameTag.conceptNameTagId}" />
<br />
<input type="submit" value="<spring:message code="conceptsearch.savetag"/>">
&nbsp;
<input type="button" value='<openmrs:message code="general.cancel"/>' onclick="history.go(-1); return; document.location='index.htm?autoJump=false&phrase=<request:parameter name="phrase"/>'">
</fieldset>
</form>
<br/>
<br/>
<c:if test="${not conceptNameTag.voided && not empty conceptNameTag.conceptNameTagId}">
	<form action="" method="post">
		<fieldset>
			<h4><openmrs:message code="conceptsearch.retiretag"/></h4>
			
			<b><openmrs:message code="general.reason"/></b>
			<input type="text" value="" size="40" name="retireReason" />
			<spring:hasBindErrors name="conceptNameTag.tag">
				<c:forEach items="${errors.allErrors}" var="error">
					<c:if test="${error.code == 'retireReason'}">
					s<span class="error"><openmrs:message code="${error.defaultMessage}" text="${error.defaultMessage}"/></span></c:if>
				</c:forEach>
			</spring:hasBindErrors>
			<br/>
			<input type="submit" value='<openmrs:message code="conceptsearch.retireNametag"/>' name="retireNameTag"/>
		</fieldset>
	</form>
</c:if>
<openmrs:extensionPoint pointId="org.openmrs.admin.concepts.conceptNameTagForm.footer" type="html" parameters="conceptNameTagId=${conceptNameTag.conceptNameTagId}" />

<%@ include file="/WEB-INF/template/footer.jsp" %>