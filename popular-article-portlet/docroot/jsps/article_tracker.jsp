<%@page import="com.liferay.portal.model.Layout"%>
<%@page import="com.liferay.portlet.journal.model.JournalContentSearch"%>
<%@page import="com.liferay.populararticle.portlet.util.PopularArticleUtil"%>
<%@ include file="/jsps/init.jsp" %>


<div class="portlet-msg-alert">
	<liferay-ui:message key="asset-publisher-alert-message" />
</div>

<c:choose>
<c:when test="${not empty articleLocationList}">
<table>
<tr><th><liferay-ui:message key="article-located-followig-pages"></liferay-ui:message></th></tr>
 	<c:forEach var="articleLocation" items="${articleLocationList}">
	<tr><td>
	<%  Layout layoutObj = PopularArticleUtil.getLayout((JournalContentSearch)pageContext.getAttribute("articleLocation"));%>
	<a target="_blank" href="<%= PopularArticleUtil.getPageURL(layoutObj, themeDisplay.getPortalURL()) %>">
		<%= layoutObj.getName(locale) %>
	</a>
	</td></tr>
	</c:forEach>
</table>
</c:when>

<c:otherwise>
<div class="portlet-msg-info">
	<liferay-ui:message key="no-location-were-found" />
</div>
</c:otherwise>
</c:choose>