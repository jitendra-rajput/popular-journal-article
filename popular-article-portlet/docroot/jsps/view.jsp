<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.populararticle.portlet.util.PopularArticleUtil"%>
<%@ include file="init.jsp"%>

<%
	String tabs2 = ParamUtil.getString(request, "tabs2", "most-viewed");
	String tabs1Names = "most-viewed,most-rated,most-commented";
%>

<liferay-portlet:renderURL  var="portletURL">
	<portlet:param name="tabs2" value="<%= tabs2 %>" />
</liferay-portlet:renderURL>

<liferay-ui:tabs
		names="<%= tabs1Names %>"
		param="tabs2"
		url="<%= portletURL %>"
/>


<c:choose>

<c:when test='<%= tabs2.equals("most-viewed") %>'>
	 <%@ include file="most_viewed_articles.jsp"%>
</c:when>
<c:when test='<%= tabs2.equals("most-rated") %>'>
	<%@ include file="most_rated_articles.jsp"%>
</c:when>
<c:when test='<%= tabs2.equals("most-commented") %>'>
	<%@ include file="most_commented_articles.jsp"%>
</c:when>
</c:choose>