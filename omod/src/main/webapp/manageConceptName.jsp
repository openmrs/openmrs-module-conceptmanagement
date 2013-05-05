<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="MANAGE_CONCEPTS" otherwise="/login.htm"
	redirect="/module/conceptsearch/manageConceptName.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>


<!-- Tell 1.7+ versions of core to not include JQuery themselves. Also, on 1.7+ we may get different jquery and jquery-ui versions than 1.3.2 and 1.7.2 -->
<c:set var="DO_NOT_INCLUDE_JQUERY" value="true" />

<!-- Include css from conceptmanagement module -->
<openmrs:htmlInclude
	file="${pageContext.request.contextPath}/moduleResources/@MODULE_ID@/scripts/jquery/autocomplete/css/jquery.autocomplete.css" />

<!-- Include javascript from conceptmanagement module -->
<openmrs:htmlInclude
	file='${pageContext.request.contextPath}/moduleResources/@MODULE_ID@/scripts/jquery/autocomplete/jquery.autocomplete.min.js' />
<openmrs:htmlInclude
	file='${pageContext.request.contextPath}/moduleResources/@MODULE_ID@/scripts/jquery/autocomplete/jquery.js' />
<openmrs:htmlInclude
	file="${pageContext.request.contextPath}/moduleResources/@MODULE_ID@/scripts/jquery-ui/js/jquery-ui-1.7.2.custom.min.js" />
<openmrs:htmlInclude
	file='${pageContext.request.contextPath}/moduleResources/@MODULE_ID@/scripts/jquery/autocomplete/jquery.autocomplete.js' />

<script type="text/javascript" charset="utf-8">
$(document).ready(function(){
    
	$(".saveTagWithConceptName").click( function(e) {
		$("#deleteOrSave").val("saveTag");
		var id = $(this).attr('id').replace('saveTagWithConceptName',''); 
		var tagnameQueryId = '#tagnameQuery'+id;
        var tagnameQueryId2=$(tagnameQueryId);
		var tagString = tagnameQueryId2.val();
		$("#tagnameQuery").val(tagString);
		
	 });
	
    $(".deleteCN-conceptName").click( function(e) {
        var id = $(this).attr('id').replace('deleteCN-conceptName','');    
        var myTag = $(this).attr('name');
        var hiddenField = '#hiddenAction'+id;
        var hiddenField2=$(hiddenField);
        hiddenField2.val(myTag);
        $("#deleteOrSave").val("deleteTag");
        $("#tagnameQuery").val("deleteTag");
        $("#form1").submit();
           
	});
 
});
</script>
<style>
.name-tag {
	border: 1px black solid;
	padding: 2px 4px;
	border-radius: 4px;
	background-color: #f0f0f0;
}

.add-nametag {
	padding: 10px 0px;
}
</style>

<%--
	passed from controller:
	* searchResult: concept with list of names tags and locales
--%>

<h2>
	<spring:message code="conceptsearch.manageconceptnameheading" />
</h2>
<br />
<spring:hasBindErrors name="tagnameQuery">
	<spring:message code="fix.error" />
	<div class="error">
		<c:forEach items="${errors.allErrors}" var="error">
			<spring:message code="${error.code}" text="${error.code}" />
			<br />
		</c:forEach>
	</div>
	<br />
</spring:hasBindErrors>
<br />
<br />


<div class="boxHeader">
	<b>${concept.name} 
</div>
<div class="box">
	<form name="form1" id="form1" method="post">
		<span class="openmrsSearchDiv">
			<table class="openmrsSearchTable">


				<tr>

					<th><openmrs:message code="general.locale" /><a
						href="?sort=locale&order=desc&conceptId=${concept.conceptId}"><img
							style="width: 14px; height: 14px;" border="0"
							src="<%=request.getContextPath()%>/images/movedown.gif"></a><a
						href="?sort=locale&order=asc&conceptId=${concept.conceptId}"><img
							style="width: 14px; height: 14px;" border="0"
							src="<%=request.getContextPath()%>/images/moveup.gif"></a></th>
					<th><spring:message code="conceptsearch.nameLabel" /><a
						href="?sort=conceptName&order=desc&conceptId=${concept.conceptId}"><img
							style="width: 14px; height: 14px;" border="0"
							src="<%=request.getContextPath()%>/images/movedown.gif"></a><a
						href="?sort=conceptName&order=asc&conceptId=${concept.conceptId}"><img
							style="width: 14px; height: 14px;" border="0"
							src="<%=request.getContextPath()%>/images/moveup.gif"></a></th>
					<th><spring:message code="conceptsearch.tags" /></th>


				</tr>

				<c:forEach var="conceptName" items="${searchResult.pageList}"
					varStatus="rowStatus">
					<tr class='${rowStatus.index % 2 == 0 ? "evenRow" : "oddRow"}'>
						<td>${conceptName.locale}</td>
						<td>${conceptName.conceptName}</td>
						<td><c:forEach var="tag"
								items="${conceptName.conceptNameTags}" varStatus="rowTagStatus">
				&nbsp;
				<span class="name-tag">${tag} <span
									id="deleteCN${rowTagStatus.index}" class="deleteCN"> <span
										id="deleteCN-conceptName${conceptName.conceptName}${rowTagStatus.index}"
										class="deleteCN-conceptName" name="${tag}"> <a href="#"
											title="Delete">x</a>
									</span> <input
										class="hiddenAction${conceptName.conceptName}${rowTagStatus.index}"
										id="hiddenAction${conceptName.conceptName}${rowTagStatus.index}"
										type="hidden"
										name="hiddenAction${conceptName.conceptName}${rowTagStatus.index}"
										value="">

								</span>
								</span>
							</c:forEach>
							</br>
							<div class="add-nametag">
								&nbsp; Add : &nbsp; <input id="tagnameQuery${rowStatus.index}"
									type="text" name="tagnameQuery${conceptName.conceptName}"
									size="25" value=""> &nbsp;&nbsp; <input type="submit"
									id="saveTagWithConceptName${rowStatus.index}"
									class="saveTagWithConceptName"
									name="saveTagWithConceptName${conceptName.conceptName}"
									value="<spring:message code="conceptsearch.savetags" />">
								<script>
	        var tagnameQuery = '#tagnameQuery'+${rowStatus.index};
	        var tagnameQuery1=$(tagnameQuery);
			tagnameQuery1.autocomplete("<%=request.getContextPath()%>/module/conceptsearch/autocompletenametag.form");
								</script>
							</div></td>
					</tr>
				</c:forEach>
			</table>
		</span>
		<div>
			<input type="hidden" id="deleteOrSave" name="deleteOrSave" value="">
			<input type="hidden" id="tagnameQuery" name="tagnameQuery" value="">
		</div>

	</form>
</div>
<%@ include file="/WEB-INF/template/footer.jsp"%>