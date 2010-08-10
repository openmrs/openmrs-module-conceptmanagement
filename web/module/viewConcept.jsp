<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<openmrs:require privilege="View Concepts" otherwise="/login.htm" />

<style type="text/css">
	#conceptTabs {
		margin: 10px auto 7px auto;
		padding-top: 5px;
		padding-left: 5px;
		padding-bottom: 2px;
		border-bottom: 1px solid navy;
		width: 99%;
	}
	
	#conceptTabs ul, #conceptTabs li {
		display: inline;
		list-style-type: none;
		padding: 0px 0px 0px 0px;
	}
	
	#conceptTabs a:link, #conceptTabs a:visited {
		border: 1px solid navy;
		font-size: small;
		font-weight: bold;
		margin-right: 8px;
		padding: 2px 10px 2px 10px;
		text-decoration: none;
		color: navy;
		background: #E0E0F0;
	}
	#conceptTabs a:link.active, #conceptTabs a:visited.active {
		border-bottom: 1px solid #FFFFFF;
	}
	#conceptTabs a.current, #conceptTabs a:link.current, #conceptTabs a:visited.current, #conceptTabs a.current:hover {
		background: #FFFFFF;
		border-bottom: 1px solid #FFFFFF;
		color: navy;
		text-decoration: none;
	}
	#conceptTabs a:hover {
		text-decoration: underline;
	}
</style>

<script type="text/javascript">
	var timeOut = null;
	addEvent(window, 'load', initTabs);

	var userId = "1";

	function initTabs() {
		var c = getTabCookie();
		if (c == null) {
			var tabs = document.getElementById("conceptTabs").getElementsByTagName("a");
			if (tabs.length && tabs[0].id)
				c = tabs[0].id;
		}
		changeTab(c);
	}
	
	function setTabCookie(tabType) {
		document.cookie = "conceptTab-" + userId + "="+escape(tabType);
	}
	
	function getTabCookie() {
		var cookies = document.cookie.match('conceptTab-' + userId + '=(.*?)(;|$)');
		if (cookies) {
			return unescape(cookies[1]);
		}
		return null;
	}
	
	function changeTab(tabObj) {
		if (!document.getElementById || !document.createTextNode) {return;}
		if (typeof tabObj == "string")
			tabObj = document.getElementById(tabObj);
		
		if (tabObj) {
			var tabs = tabObj.parentNode.parentNode.getElementsByTagName('a');
			for (var i=0; i<tabs.length; i++) {
				if (tabs[i].className.indexOf('current') != -1) {
					manipulateClass('remove', tabs[i], 'current');
				}
				var divId = tabs[i].id.substring(0, tabs[i].id.lastIndexOf("Tab"));
				var divObj = document.getElementById(divId);
				if (divObj) {
					if (tabs[i].id == tabObj.id)
						divObj.style.display = "";
					else
						divObj.style.display = "none";
				}
			}
			addClass(tabObj, 'current');
			
			setTabCookie(tabObj.id);
		}
		return false;
    }
</script>

<h2>View Concept</h2>
<br />
<h3>Viewing Concept ${concept.name} (${concept.conceptId})</h3>

<div id="conceptTabs">
	<ul>		
			<li><a id="conceptOverviewTab" href="#" onclick="return changeTab(this);" hidefocus="hidefocus">Overview</a></li>
		
			<li><a id="conceptDetailsTab" href="#" onclick="return changeTab(this);" hidefocus="hidefocus">Details</a></li>	
		
			<li><a id="conceptMetadataTab" href="#" onclick="return changeTab(this);" hidefocus="hidefocus">Metadata</a></li>
	</ul>
</div>

<div id="conceptOverview" style="display:none;">
<div class="boxHeader">Overview</div>
<div class="box">
<table>
	<tr>
		<td><b>ID</b></td>
		<td>${concept.conceptId}</td>
	</tr>
	<tr>
		<td><b>Name</b></td>
		<td>${concept.name}</td>
	</tr>
	<tr>
		<td><b>Description</b></td>
		<td>${concept.description}</td>
	</tr>
	<tr>
		<td><b>Class</b></td>
		<td>${concept.conceptClass.name}</td>
	</tr>
	<tr>
		<td><b>Datatype</b></td>
		<td>${concept.datatype.name}</td>
	</tr>
</table>
</div>
</div> <!-- end conceptOverview -->

<div id="conceptDetails" style="display:none;">
<div class="boxHeader">Overview</div>
<div class="box">
<table>
	<tr>
		<td><b>Test</b></td>
		<td>Test</td>
	</tr>
</table>
</div>
</div> <!-- end conceptDetails -->

<div id="conceptMetadata" style="display:none;">
<div class="boxHeader">Overview</div>
<div class="box">
<table>
	<tr>
		<td><b>Created</b></td>
		<td>Created on <fmt:formatDate value="${concept.dateCreated}" pattern="dd/MM/yyyy"/>  by ${concept.creator.person.names} (id: ${concept.creator.userId})</td>
	</tr>
	<tr>
		<td><b>Changed</b></td>
		<td>Changed on <fmt:formatDate value="${concept.dateChanged}" pattern="dd/MM/yyyy"/> by ${concept.creator.person.names} (id: ${concept.creator.userId})</td>
	</tr>
	<tr>
		<td><b>Version</b></td>
		<c:choose>
			<c:when test='${concept.version==""}'>
				<td>none</td>
			</c:when>
			<c:otherwise>
				<td>${concept.version}</td>
			</c:otherwise>
		</c:choose>
	</tr>
</table>
</div>
</div> <!-- end conceptMeatadata -->



<%@ include file="/WEB-INF/template/footer.jsp"%>
