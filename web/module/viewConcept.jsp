<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="View Concepts" otherwise="/login.htm" />

<h2>View Concept</h2>

Concepts will be displayed here. ${concept}

<%@ include file="/WEB-INF/template/footer.jsp"%>
