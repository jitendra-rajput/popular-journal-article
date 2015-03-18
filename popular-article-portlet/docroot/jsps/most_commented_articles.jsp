<liferay-ui:search-container hover="false"  searchContainer="${commentedSearchContainer}">

	<liferay-ui:search-container-results 
		results="${commentedSearchContainer.results}"
		total="${commentedSearchContainer.total}" />
	<liferay-ui:search-container-row 
		className="com.liferay.populararticle.portlet.bean.MBMessageVO"
		keyProperty="classPK" modelVar="mbMessage">
		
		<liferay-ui:search-container-column-text name="title" buffer="bufferSelection">
		
		<portlet:renderURL var="articlePreviewURL" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
			<portlet:param name="openPreview" value="true"/>
			<portlet:param name="resourcePrimKey" value="<%=String.valueOf(mbMessage.getClassPK()) %>"/>
			<portlet:param name="target" value="_blank"/>
		</portlet:renderURL>
		
		<%
			bufferSelection.append("<a target='_blank' href='");
			bufferSelection.append(articlePreviewURL);
			bufferSelection.append("'>");
			bufferSelection.append(PopularArticleUtil.getArticleName(mbMessage.getClassPK(),locale));
			bufferSelection.append("</a>");
		%>
		
		
		</liferay-ui:search-container-column-text>
		<liferay-ui:search-container-column-text name="comments" buffer="imgBufferSelection">
		
		<%
			imgBufferSelection.append("<img src='");
			imgBufferSelection.append(renderRequest.getContextPath()+"/"+themeDisplay.getPathImage()+"/conversation.png");
			imgBufferSelection.append("'>");
			imgBufferSelection.append("</img> &nbsp;&nbsp;");
			imgBufferSelection.append(mbMessage.getCommentCount());
		%>
		</liferay-ui:search-container-column-text>
		<liferay-ui:search-container-column-text name="author" buffer="nameBufferSelection">
		
			<%
			nameBufferSelection.append(PopularArticleUtil.getAuthorName(mbMessage.getClassPK()));
		%>
		
		</liferay-ui:search-container-column-text>
		<liferay-ui:search-container-column-jsp name="action" align="center" path="/jsps/commented_article_action.jsp" />
	</liferay-ui:search-container-row>
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>
