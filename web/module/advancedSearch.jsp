<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="View Concepts" otherwise="/login.htm" />

<h2><spring:message code="conceptmanagement.advancedheading" /></h2>

<script type="text/javascript">
<!--
	var lstSelected = new Array();
	
	function contains(a, obj) {
		var i = a.length;
		while (i--) {
			if (a[i] === obj) {
		    	return true;
		    }
		}
		return false;
	}

	function removeByElement(arrayName,arrayElement)
	 {
	    for(var i=0; i<arrayName.length;i++ )
	     { 
	        if(arrayName[i]==arrayElement)
	            arrayName.splice(i,1); 
	      } 
	  }
	
	function doSelect(ob) {
		if (ob.selectedIndex != -1) {
			if (contains(lstSelected, ob.selectedIndex))
					removeByElement(lstSelected, ob.selectedIndex);
			else
				lstSelected.push(ob.selectedIndex);

			for (var i=0; lstSelected.length; i++) {
				ob.options[lstSelected[i]].selected=true;
			}
				
		}
	}

	//-->
</script>

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
		<td>Datatype:</td>
		<td><select multiple="multiple" name="conceptDatatype" size="5">
			<option value="-1"></option>
			<c:forEach var="dataType" items="${dataTypes}">
				<option
					<c:if test="${fn:contains(conceptSearch.dataTypes, dataType)}"> selected </c:if> value="${dataType.name}">${dataType.name}
				</option>
			</c:forEach>
		</select></td>
	</tr>
	<tr>
		<td>Classes:</td>
		<td><select multiple="multiple" name="conceptClasses" size="5">
			<option value="-1"></option>
			<c:forEach var="class" items="${conceptClasses}">
				<option
					<c:if test="${fn:contains(conceptSearch.conceptClasses, class)}"> selected </c:if> value="${class.name}">${class.name}
				</option>
			</c:forEach>
		</select></td>
	</tr>
	<tr>
		<td>Is set?</td>
		<td><select name="conceptIsSet" size="1">
			<option value="-1"></option>
			<option <c:if test="${conceptSearch.isSet == 1}"> selected </c:if> value="1">Yes</option>
			<option <c:if test="${conceptSearch.isSet == 0}"> selected </c:if> value="0">No</option>
		</select></td>
	</tr>
	<tr>
		<td></td>
		<td><input type="submit" name="action" value="Search"></td>
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
		<th><spring:message code="conceptmanagement.concept" /><a href="?sort=name&order=asc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/movedown.gif"></a><a href="?sort=name&order=desc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/moveup.gif"></a></th>
		<th><spring:message code="conceptmanagement.class" /><a href="?sort=class&order=asc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/movedown.gif"></a><a href="?sort=class&order=desc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/moveup.gif"></a></th>
		<th><spring:message code="conceptmanagement.datatype" /><a href="?sort=datatype&order=asc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/movedown.gif"></a><a href="?sort=datatype&order=desc"><img style="width: 15px; height: 15px;" border="0" src="/openmrs/images/moveup.gif"></a></th>
		<th>Other Names</th>
	</tr>
	<c:forEach var="concept" items="${searchResult}">
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

<%@ include file="/WEB-INF/template/footer.jsp"%>
