<%@page import="com.liferay.populararticle.portlet.util.PopularArticleUtil"%>
<%@ include file="init.jsp"%>


<liferay-ui:search-container hover="false"  searchContainer="${searchContainer}">

	<liferay-ui:search-container-results 
		results="${searchContainer.results}"
		total="${searchContainer.total}" />
	<liferay-ui:search-container-row 
		className="com.liferay.portlet.asset.model.AssetEntry"
		keyProperty="entryId" modelVar="assetEntryObj">
		
		<liferay-ui:search-container-column-text name="title" buffer="bufferSelection">
		
		<%
			bufferSelection.append("<a target='_blank' href='");
			bufferSelection.append(PopularArticleUtil.getPreviewURl(themeDisplay,assetEntryObj.getClassPK()));
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
		
	</liferay-ui:search-container-row>
	<liferay-ui:search-iterator/>
</liferay-ui:search-container>
