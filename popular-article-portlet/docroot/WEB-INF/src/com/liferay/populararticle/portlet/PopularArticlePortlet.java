package com.liferay.populararticle.portlet;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.populararticle.portlet.util.PopularArticleUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class PopularArticlePortlet extends MVCPortlet {

	

	@Override
	public void init() throws PortletException {
		 viewJSP = getInitParameter("view-content");
		 previewJSP = getInitParameter("preview-jsp");
		 articleTrackerJSP = getInitParameter("article-tracker-jsp");
	}

	
	@Override
	public void doView(RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {
	        
	        String tabs2 = ParamUtil.getString(renderRequest, "tabs2", "most-viewed");
	        boolean openArticleTracker = ParamUtil.getBoolean(renderRequest, "openArticleTracker");
	        boolean openPreview = ParamUtil.getBoolean(renderRequest, "openPreview");
	        if(openArticleTracker){
	            String articleId = ParamUtil.getString(renderRequest, "articleId");
	            long groupId = ParamUtil.getLong(renderRequest, "groupId");
	            renderRequest.setAttribute("articleLocationList", PopularArticleUtil.getArticleLocationList(groupId, articleId));
	            include(articleTrackerJSP, renderRequest, renderResponse);
	        }else if(openPreview){
	            long classPK = ParamUtil.getLong(renderRequest, "resourcePrimKey");
	            JournalArticle journalArticle = PopularArticleUtil.getLatestArticle(classPK);
	            renderRequest.setAttribute(WebKeys.JOURNAL_ARTICLE, journalArticle);
	            include(previewJSP, renderRequest, renderResponse);
	            
	        }else{
	            if(Validator.equals(tabs2, "most-rated")){
	                renderRequest.setAttribute("ratedArticleSearchContainer", PopularArticleUtil.getMostRatedSearchContainer(renderRequest, renderResponse));
	            }else if(Validator.equals(tabs2, "most-commented")){
                        renderRequest.setAttribute("commentedSearchContainer", PopularArticleUtil.getMostCommentedSearchContainer(renderRequest, renderResponse));
                    }else{
                        renderRequest.setAttribute("searchContainer", PopularArticleUtil.getSearchContainer(renderRequest, renderResponse));
    	        }
    		
    	        renderRequest.setAttribute("tabs2", tabs2);
    		include(viewJSP, renderRequest, renderResponse);
	    }
	}
	
	
	

	protected void include(String path, RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {

		PortletRequestDispatcher portletRequestDispatcher = getPortletContext()
				.getRequestDispatcher(path);

		if (portletRequestDispatcher == null) {
			LOGGER.error(path + " is not a valid include");
		} else {
			portletRequestDispatcher.include(renderRequest, renderResponse);
		}
	}



	
	    
	 private String viewJSP;
	 private String previewJSP;
	 private String articleTrackerJSP;
	 private static final Log LOGGER = LogFactoryUtil.getLog(PopularArticlePortlet.class);
	    

}
