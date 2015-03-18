

<liferay-ui:search-container hover="false"  searchContainer="${searchContainer}">

	<liferay-ui:search-container-results 
		results="${searchContainer.results}"
		total="${searchContainer.total}" />
	<liferay-ui:search-container-row 
		className="com.liferay.portlet.asset.model.AssetEntry"
		keyProperty="entryId" modelVar="assetEntryObj">
		
		<liferay-ui:search-container-column-text name="title" buffer="bufferSelection">
		
		<portlet:renderURL var="articlePreviewURL" windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>">
			<portlet:param name="openPreview" value="true"/>
			<portlet:param name="resourcePrimKey" value="<%=String.valueOf(assetEntryObj.getClassPK()) %>"/>
			<portlet:param name="target" value="_blank"/>
		</portlet:renderURL>
		
		<%
			bufferSelection.append("<a target='_blank' href='");
			bufferSelection.append(articlePreviewURL);
			bufferSelection.append("'>");
			bufferSelection.append(PopularArticleUtil.getArticleName(assetEntryObj.getClassPK(),locale));
			bufferSelection.append("</a>");
		%>
		
		
		</liferay-ui:search-container-column-text>
		<liferay-ui:search-container-column-text name="view-count" property="viewCount"/>
		<liferay-ui:search-container-column-text name="author" buffer="nameBufferSelection">
		
			<%
			nameBufferSelection.append(PopularArticleUtil.getAuthorName(assetEntryObj.getClassPK()));
		%>
		
		</liferay-ui:search-container-column-text>
		<liferay-ui:search-container-column-jsp name="action" align="center" path="/jsps/article_action.jsp" />
	</liferay-ui:search-container-row>
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>
