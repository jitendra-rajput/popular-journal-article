package com.liferay.populararticle.portlet.util;


import java.util.List;
import java.util.Locale;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.asset.service.persistence.AssetEntryQuery;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;

public final class PopularArticleUtil {
	
	private PopularArticleUtil(){
		
	}
	
	public static List<AssetEntry> getMostViewedArticles(long groupId ,int start , int end){
		
		List<AssetEntry> mostViewedAssetList = null;
		// mostViewedArticleList =JournalArticleLocalServiceUtil.search(themeDisplay.getCompanyId(), themeDisplay.getScopeGroupId(), 0, StringPool.BLANK,  0.0, StringPool.BLANK, StringPool.BLANK, StringPool.BLANK, new Date(), new Date(), WorkflowConstants.STATUS_APPROVED, new Date(), -1, -1, new ArticleModifiedDateComparator());
		
		
		AssetEntryQuery assetEntryQuery = new AssetEntryQuery();
		assetEntryQuery.setClassName(JournalArticle.class.getName());
		assetEntryQuery.setStart(start);
		assetEntryQuery.setEnd(end);
		PortalUtil.getClassNameId(JournalArticle.class.getName());
		assetEntryQuery.setExcludeZeroViewCount(true);
		assetEntryQuery.setGroupIds(new long[]{groupId});
		assetEntryQuery.setOrderByCol1("viewCount");
		assetEntryQuery.setOrderByType1("DESC");
		
		
		 try {
			 mostViewedAssetList = AssetEntryLocalServiceUtil.getEntries(assetEntryQuery);
			
		} catch (SystemException e) {
			
			LOGGER.error(e.getMessage());
		}
		return mostViewedAssetList;
		 
	}
	
	public static int getMostViewedArticlesCount(long groupId ,int start, int end) {

		List<AssetEntry> mostViewedAssetList = null;
		int count = 0;
		
		AssetEntryQuery assetEntryQuery = new AssetEntryQuery();
		assetEntryQuery.setClassName(JournalArticle.class.getName());
		assetEntryQuery.setStart(start);
		assetEntryQuery.setEnd(end);
		PortalUtil.getClassNameId(JournalArticle.class.getName());
		assetEntryQuery.setExcludeZeroViewCount(true);
		assetEntryQuery.setGroupIds(new long[]{groupId});

		try {
			mostViewedAssetList = AssetEntryLocalServiceUtil.getEntries(assetEntryQuery);

		} catch (SystemException e) {

			LOGGER.error(e.getMessage());
		}

		if (Validator.isNotNull(mostViewedAssetList)) {

			count = mostViewedAssetList.size();
		}
		return count;

	}
	
	  public static SearchContainer<AssetEntry> getSearchContainer(RenderRequest renderRequest ,RenderResponse renderResponse){
	    	
		  	ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
	    	SearchContainer<AssetEntry> searchContainer = null;
	    	
	    	PortletURL portletURL = renderResponse.createRenderURL();

	    	searchContainer = new SearchContainer<AssetEntry>(renderRequest, null, null, SearchContainer.DEFAULT_CUR_PARAM, 10, portletURL, null, "no-web-content-were-found");
	    	
	    	List<AssetEntry> mostViewedAssetList = getMostViewedArticles(themeDisplay.getScopeGroupId(),searchContainer.getStart() , searchContainer.getEnd());
	    	int size = getMostViewedArticlesCount(themeDisplay.getScopeGroupId(),QueryUtil.ALL_POS, QueryUtil.ALL_POS); 
	    	int customSize = getDeltaFromPref(renderRequest);
	    	
	    	if(customSize<size){
	    		size = customSize;
	    	}
			searchContainer.setResults(mostViewedAssetList);
			searchContainer.setTotal(size);
			return searchContainer;
	    	
	   }
	  
	public static JournalArticle getLatestArticle(long classPk) {

		JournalArticle latestJournalArticle = null;

		try {
			latestJournalArticle = JournalArticleLocalServiceUtil
					.getLatestArticle(classPk);//resourcePrimKey
		} catch (PortalException e) {
			LOGGER.error(e.getMessage());
		} catch (SystemException e) {
			LOGGER.error(e.getMessage());
		}
		return latestJournalArticle;
	}
	
	public static String getArticleName(long classPk , Locale locale){
		
		String articleTitle = StringPool.BLANK;
		JournalArticle journalArticle = getLatestArticle(classPk);
		if(Validator.isNotNull(journalArticle)){
			articleTitle = journalArticle.getTitle(locale);
		}
		return articleTitle;
		
	}
	
	public static String getPreviewURl(ThemeDisplay themeDisplay,long classPk){
		
		StringBuilder previewURL = new StringBuilder();
		JournalArticle journalArticle = getLatestArticle(classPk);
		if(Validator.isNotNull(journalArticle)){
		
			previewURL.append(themeDisplay.getPathMain());
			previewURL.append("/journal/view_article_content?cmd=");
			previewURL.append(Constants.VIEW);
			previewURL.append("&groupId=");
			previewURL.append(journalArticle.getGroupId());
			previewURL.append("&articleId=");
			previewURL.append(journalArticle.getArticleId());
			previewURL.append("&version=");
			previewURL.append(journalArticle.getVersion());
		}
		
		return previewURL.toString();
		
	}
	
	  public static int getDeltaFromPref(PortletRequest portletRequest){
	        
	        int customDelta = 0;
	        PortletPreferences preferences = portletRequest.getPreferences();
	        try
	        {
	            String portletResource = ParamUtil.getString(portletRequest, "portletResource");
	            if (Validator.isNotNull(portletResource))
	            {

	                preferences = PortletPreferencesFactoryUtil.getPortletSetup(portletRequest, portletResource);

	            }
	        } catch (PortalException e)
	        {
	           LOGGER.error(e.getMessage());
	        } catch (SystemException e)
	        {
	            LOGGER.error(e.getMessage());
	        }
	        customDelta = GetterUtil.getInteger(preferences.getValue("customDelta", StringPool.BLANK),10);
	        if(Validator.isNull(customDelta) || customDelta < 10){
	        	customDelta = 10;
	        }
	        return customDelta;
	    }
	  
	  public static String getAuthorName(long classPk){
		  
		  String authorName = StringPool.BLANK;
		  JournalArticle journalArticle = getLatestArticle(classPk);
		  if(Validator.isNotNull(journalArticle)){
			  authorName = journalArticle.getUserName();
		  }
		return authorName;
		  
	  }
	
	
	 private static final Log LOGGER = LogFactoryUtil.getLog(PopularArticleUtil.class);

}
