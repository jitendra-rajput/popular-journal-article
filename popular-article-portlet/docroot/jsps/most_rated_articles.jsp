<liferay-ui:search-container hover="false"  searchContainer="${ratedArticleSearchContainer}">

	<liferay-ui:search-container-results 
		results="${ratedArticleSearchContainer.results}"
		total="${ratedArticleSearchContainer.total}" />
	<liferay-ui:search-container-row 
		className="com.liferay.portlet.ratings.model.RatingsStats"
		keyProperty="statsId" modelVar="ratingStatsObj">
		
		<liferay-ui:search-container-column-text name="title" buffer="bufferSelection">
		
		<portlet:renderURL var="articlePreviewURL" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
			<portlet:param name="openPreview" value="true"/>
			<portlet:param name="resourcePrimKey" value="<%=String.valueOf(ratingStatsObj.getClassPK()) %>"/>
			<portlet:param name="target" value="_blank"/>
		</portlet:renderURL>
		
		<%
			bufferSelection.append("<a target='_blank' href='");
			bufferSelection.append(articlePreviewURL);
			bufferSelection.append("'>");
			bufferSelection.append(PopularArticleUtil.getArticleName(ratingStatsObj.getClassPK(),locale));
			bufferSelection.append("</a>");
		%>
		
		</liferay-ui:search-container-column-text>
		<liferay-ui:search-container-column-jsp name="average-score" align="center" path="/jsps/article_rating.jsp" />
		<liferay-ui:search-container-column-text name="author" buffer="nameBufferSelection">
		
			<%
			nameBufferSelection.append(PopularArticleUtil.getAuthorName(ratingStatsObj.getClassPK()));
			%>
		
		</liferay-ui:search-container-column-text>
		<liferay-ui:search-container-column-jsp name="action" align="center" path="/jsps/rated_article_action.jsp" />
	</liferay-ui:search-container-row>
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>
