<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<h2><spring:message code="conceptmanagement.heading" /></h2>

<br/>
<form method="post" class="box">
  <table>
    <tr>
   	  <td><input type="text" name="search" size="20" value="weight"></td>
   	  <td><input type="submit" value="<spring:message code="conceptmanagement.go" />"></td>
  	</tr>
  </table>
</form>
<c:if test="${fn:length(search_result) gt 0}">
	<dir>returned ${fn:length(search_result)} results</dir>
</c:if>
<div class="box">
<table>
  <tr>
   	<th><spring:message code="conceptmanagement.actions" /></th>
   	<th><spring:message code="conceptmanagement.concept" /></th>
   	<th><spring:message code="conceptmanagement.class" /></th>
   	<th><spring:message code="conceptmanagement.datatype" /></th>
  </tr>
  <c:forEach var="concept" items="${search_result}">
    <tr>
      <td><a href="../../dictionary/concept.htm?conceptId=${concept.conceptId}"><spring:message code="conceptmanagement.view" /></a></td>
      <td>${concept.names}</td>
      <td>${concept.conceptClass.name}</td>
      <td>${concept.datatype.name}</td>
    </tr>	
  </c:forEach>	
</table>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>
