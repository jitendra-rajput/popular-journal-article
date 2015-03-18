<%@page import="com.liferay.portal.kernel.language.UnicodeLanguageUtil"%>
<%@page import="com.liferay.portal.kernel.language.LanguageUtil"%>
<%@page import="com.liferay.populararticle.portlet.util.PopularArticleUtil"%>
<%@page import="com.liferay.portlet.journal.model.JournalArticle"%>
<%@page import="com.liferay.portal.kernel.util.WebKeys"%>
<%@page import="com.liferay.portlet.asset.model.AssetEntry"%>
<%@page import="com.liferay.portal.kernel.dao.search.ResultRow"%>
<%@ include file="/jsps/init.jsp" %>

<%
	ResultRow resultRow = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	AssetEntry assetObj = (AssetEntry)resultRow.getObject();
	JournalArticle article = PopularArticleUtil.getLatestArticle(assetObj.getClassPK());
	
%>
		
		<liferay-portlet:renderURL var="locateArticleURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
			<portlet:param name="openArticleTracker" value="true" />
			<portlet:param name="articleId" value="<%= String.valueOf(article.getArticleId()) %>" />
			<portlet:param name="groupId" value="<%= String.valueOf(article.getGroupId()) %>" />
			
		</liferay-portlet:renderURL>
		
		<liferay-portlet:renderURL plid="<%= PopularArticleUtil.getPreviewPlid(article, themeDisplay) %>" portletName="15" var="previewArticleContentURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
				<portlet:param name="struts_action" value="/journal/preview_article_content" />
				<portlet:param name="groupId" value="<%= String.valueOf(article.getGroupId()) %>" />
				<portlet:param name="articleId" value="<%= article.getArticleId() %>" />
				<portlet:param name="version" value="<%= String.valueOf(article.getVersion()) %>" />
				<portlet:param name="cmd" value="view" />
		</liferay-portlet:renderURL>
			<%
			String taglibUrl = "javascript:" + renderResponse.getNamespace() + "openArticleTracker('"+locateArticleURL+"')";
			String taglibOnClick = "Liferay.fire('previewArticle', {title: '" + HtmlUtil.escapeJS(article.getTitle(locale)) + "', uri: '" + HtmlUtil.escapeJS(previewArticleContentURL.toString()) + "'});";
			%>
					
		<liferay-ui:icon-menu >
			<liferay-ui:icon
				image="preview"
				onClick="<%= taglibOnClick %>"
				url="javascript:;"
			/>
					
		<liferay-ui:icon
				image="view"
				message='<%= LanguageUtil.get(pageContext, "locate-article")  %>'
				url="<%= taglibUrl %>"
						
		/>
	</liferay-ui:icon-menu>	
				
				
<aui:script>
	function <portlet:namespace />openArticleTracker(locateArticleURL) {
		Liferay.Util.openWindow(
		{
			dialog: {
                width:500
            },
				
				id: '<portlet:namespace />openArticleTracker',
				title: '<%= UnicodeLanguageUtil.get(pageContext, "locate-journal-articles") %>',
				uri:  locateArticleURL,
		}		
		);
	}

</aui:script>	