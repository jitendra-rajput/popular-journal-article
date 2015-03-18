<%@page import="com.liferay.portlet.ratings.model.RatingsStats"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@ include file="/jsps/init.jsp" %>
<%
	ResultRow resultRow = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	RatingsStats ratingObj = (RatingsStats)resultRow.getObject();
	
%>
<liferay-ui:ratings-score score ="<%=ratingObj.getAverageScore() %>" />