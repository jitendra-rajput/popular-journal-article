package com.liferay.populararticle.portlet.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.populararticle.portlet.bean.MBMessageVO;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.OrderFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionList;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalContentSearch;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalContentSearchLocalServiceUtil;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import com.liferay.portlet.ratings.model.RatingsStats;
import com.liferay.portlet.ratings.service.RatingsStatsLocalServiceUtil;

public final class PopularArticleUtil
{

    private PopularArticleUtil()
    {

    }

    /*
     * public static List<AssetEntry> getMostViewedArticles(long groupId ,int
     * start , int end){ List<AssetEntry> mostViewedAssetList = null; //
     * mostViewedArticleList
     * =JournalArticleLocalServiceUtil.search(themeDisplay.getCompanyId(),
     * themeDisplay.getScopeGroupId(), 0, StringPool.BLANK, 0.0,
     * StringPool.BLANK, StringPool.BLANK, StringPool.BLANK, new Date(), new
     * Date(), WorkflowConstants.STATUS_APPROVED, new Date(), -1, -1, new
     * ArticleModifiedDateComparator()); AssetEntryQuery assetEntryQuery = new
     * AssetEntryQuery();
     * assetEntryQuery.setClassName(JournalArticle.class.getName());
     * assetEntryQuery.setStart(start); assetEntryQuery.setEnd(end);
     * assetEntryQuery.setExcludeZeroViewCount(true);
     * assetEntryQuery.setGroupIds(new long[]{groupId});
     * assetEntryQuery.setOrderByCol1("viewCount");
     * assetEntryQuery.setOrderByType1("DESC"); try { mostViewedAssetList =
     * AssetEntryLocalServiceUtil.getEntries(assetEntryQuery); } catch
     * (SystemException e) { LOGGER.error(e.getMessage()); } return
     * mostViewedAssetList; }
     */

    public static DynamicQuery getMostViewedArticleQuery(long companyId, long groupId, int start, int end,
            boolean isComapnyScope)
    {

        DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AssetEntry.class);
        dynamicQuery.add(PropertyFactoryUtil.forName("companyId").eq(companyId));
        if (!isComapnyScope)
        {
            dynamicQuery.add(PropertyFactoryUtil.forName("groupId").eq(groupId));
        }
        dynamicQuery.add(PropertyFactoryUtil.forName("classNameId").eq(
                PortalUtil.getClassNameId(JournalArticle.class.getName())));
        dynamicQuery.add(PropertyFactoryUtil.forName("viewCount").gt(0));
        dynamicQuery.add(PropertyFactoryUtil.forName("visible").eq(Boolean.TRUE));
        dynamicQuery.addOrder(OrderFactoryUtil.desc("viewCount"));
        dynamicQuery.setLimit(start, end);
        return dynamicQuery;
    }

    public static List<AssetEntry> getMostViewedArticles(long companyId, long groupId, int start, int end,
            boolean isComapnyScope)
    {

        List<AssetEntry> mostViewedAssetList = null;
        DynamicQuery dynamicQuery = getMostViewedArticleQuery(companyId, groupId, start, end, isComapnyScope);
        try
        {
            mostViewedAssetList = AssetEntryLocalServiceUtil.dynamicQuery(dynamicQuery);
        } catch (SystemException e)
        {
            LOGGER.error(e.getMessage());
        }
        return mostViewedAssetList;
    }

    public static int getMostViewedArticlesCount(long companyId, long groupId, int start, int end,
            boolean isComapnyScope)
    {
        int size = 0;
        List<AssetEntry> mostViewedAssetList = getMostViewedArticles(companyId, groupId, start, end, isComapnyScope);
        if (Validator.isNotNull(mostViewedAssetList))
        {
            size = mostViewedAssetList.size();
        }
        return size;
    }

    /*
     * public static int getMostViewedArticlesCount(long groupId ,int start, int
     * end) { int count = 0; AssetEntryQuery assetEntryQuery = new
     * AssetEntryQuery();
     * assetEntryQuery.setClassName(JournalArticle.class.getName());
     * assetEntryQuery.setStart(start); assetEntryQuery.setEnd(end);
     * assetEntryQuery.setExcludeZeroViewCount(true);
     * assetEntryQuery.setGroupIds(new long[]{groupId}); try { count =
     * AssetEntryLocalServiceUtil.getEntriesCount(assetEntryQuery); } catch
     * (SystemException e) { LOGGER.error(e.getMessage()); } return count; }
     */

    public static SearchContainer<AssetEntry> getSearchContainer(RenderRequest renderRequest,
            RenderResponse renderResponse)
    {

        ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
        SearchContainer<AssetEntry> searchContainer = null;

        PortletURL portletURL = renderResponse.createRenderURL();
        portletURL.setParameter("tabs2", "most-viewed");

        searchContainer = new SearchContainer<AssetEntry>(renderRequest, null, null, SearchContainer.DEFAULT_CUR_PARAM,
                10, portletURL, null, "no-web-content-were-found");

        boolean isCompanyScope = isComapnyScope(renderRequest);

        List<AssetEntry> mostViewedAssetList = getMostViewedArticles(themeDisplay.getCompanyId(),
                themeDisplay.getScopeGroupId(), searchContainer.getStart(), searchContainer.getEnd(), isCompanyScope);
        int size = getMostViewedArticlesCount(themeDisplay.getCompanyId(), themeDisplay.getScopeGroupId(),
                QueryUtil.ALL_POS, QueryUtil.ALL_POS, isCompanyScope);
        int customSize = getDeltaFromPref(renderRequest);

        if (customSize < size)
        {
            size = customSize;
        }
        searchContainer.setResults(mostViewedAssetList);
        searchContainer.setTotal(size);
        return searchContainer;

    }

    public static JournalArticle getLatestArticle(long classPk)
    {

        JournalArticle latestJournalArticle = null;

        try
        {
            latestJournalArticle = JournalArticleLocalServiceUtil.getLatestArticle(classPk);// resourcePrimKey
        } catch (PortalException e)
        {
            LOGGER.error(e.getMessage());
        } catch (SystemException e)
        {
            LOGGER.error(e.getMessage());
        }
        return latestJournalArticle;
    }

    public static String getArticleName(long classPk, Locale locale)
    {

        String articleTitle = StringPool.BLANK;
        JournalArticle journalArticle = getLatestArticle(classPk);
        if (Validator.isNotNull(journalArticle))
        {
            articleTitle = journalArticle.getTitle(locale);
        }
        return articleTitle;

    }

    public static String getPreviewURl(ThemeDisplay themeDisplay, long classPk)
    {

        StringBuilder previewURL = new StringBuilder();
        JournalArticle journalArticle = getLatestArticle(classPk);
        if (Validator.isNotNull(journalArticle))
        {

            previewURL.append(themeDisplay.getPathMain());
            previewURL.append("/journal/preview_article_content?cmd=");
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

    public static int getDeltaFromPref(PortletRequest portletRequest)
    {

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
        customDelta = GetterUtil.getInteger(preferences.getValue("customDelta", StringPool.BLANK), 10);
        if (Validator.isNull(customDelta) || customDelta < 10)
        {
            customDelta = 10;
        }
        return customDelta;
    }

    public static String getAuthorName(long classPk)
    {

        String authorName = StringPool.BLANK;
        JournalArticle journalArticle = getLatestArticle(classPk);
        if (Validator.isNotNull(journalArticle))
        {
            authorName = journalArticle.getUserName();
        }
        return authorName;

    }

    public static SearchContainer<RatingsStats> getMostRatedSearchContainer(RenderRequest renderRequest,
            RenderResponse renderResponse)
    {

        ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
        SearchContainer<RatingsStats> searchContainer = null;

        PortletURL portletURL = renderResponse.createRenderURL();
        portletURL.setParameter("tabs2", "most-rated");

        searchContainer = new SearchContainer<RatingsStats>(renderRequest, null, null,
                SearchContainer.DEFAULT_CUR_PARAM, 10, portletURL, null, "no-web-content-were-found");

        boolean isCompanyScope = isComapnyScope(renderRequest);
        List<RatingsStats> mostRatedAssetList = getMostRatedArticles(themeDisplay.getCompanyId(),
                themeDisplay.getScopeGroupId(), searchContainer.getStart(), searchContainer.getEnd(), isCompanyScope);
        int size = getMostRatedArticlesCount(themeDisplay.getCompanyId(), themeDisplay.getScopeGroupId(),
                isCompanyScope);
        int customSize = getDeltaFromPref(renderRequest);

        if (customSize < size)
        {
            size = customSize;
        }
        searchContainer.setResults(mostRatedAssetList);
        searchContainer.setTotal(size);
        return searchContainer;

    }

    public static SearchContainer<MBMessageVO> getMostCommentedSearchContainer(RenderRequest renderRequest,
            RenderResponse renderResponse)
    {

        ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
        SearchContainer<MBMessageVO> searchContainer = null;

        PortletURL portletURL = renderResponse.createRenderURL();
        portletURL.setParameter("tabs2", "most-commented");

        searchContainer = new SearchContainer<MBMessageVO>(renderRequest, null, null,
                SearchContainer.DEFAULT_CUR_PARAM, 10, portletURL, null, "no-web-content-were-found");

        boolean isCompanyScope = isComapnyScope(renderRequest);
        List<MBMessageVO> mostRatedAssetList = getMostCommentedArticles(themeDisplay.getCompanyId(),
                themeDisplay.getScopeGroupId(), searchContainer.getStart(), searchContainer.getEnd(), isCompanyScope);
        int size = getMostCommentedArticlesCount(themeDisplay.getCompanyId(), themeDisplay.getScopeGroupId(),
                isCompanyScope);
        int customSize = getDeltaFromPref(renderRequest);

        if (customSize < size)
        {
            size = customSize;
        }
        searchContainer.setResults(mostRatedAssetList);
        searchContainer.setTotal(size);
        return searchContainer;

    }

    public static List<MBMessageVO> getMostCommentedArticles(long companyId,long groupId,int start,int end,boolean isCompanyScope)
    {

       DynamicQuery dynamicQuery = getCommentedQuery(companyId,groupId,isCompanyScope);
       List<MBMessageVO> mbMessageList = new ArrayList<MBMessageVO>();
        try
        {
            List<Object[]> commentedArticleList  = MBMessageLocalServiceUtil.dynamicQuery(dynamicQuery,start,end);
            if(Validator.isNotNull(commentedArticleList)){
                for(Object[] longArray : commentedArticleList){
                    JournalArticle article = getLatestArticle(GetterUtil.getLong(longArray[0]));
                    if(Validator.isNotNull(article) && !Validator.equals(WorkflowConstants.STATUS_IN_TRASH, article.getStatus())){
                            MBMessageVO mbMessageVO = new MBMessageVO();
                        mbMessageVO.setClassPK(GetterUtil.getLong(longArray[0]));
                        mbMessageVO.setCommentCount(GetterUtil.getLong(longArray[1]));
                        mbMessageList.add(mbMessageVO);
                    }
                   
                }
               
            }
        } catch (SystemException e)
        {
            LOGGER.error(e.getMessage());
        }
        return mbMessageList;

    }
    

    public static List<RatingsStats> getMostRatedArticles(long companyId, long groupId, int start, int end,
            boolean isCompanyScope)
    {

        DynamicQuery dynamicQuery = getRatingQuery(companyId, groupId, isCompanyScope);
        List<RatingsStats> ratingStatsList = null;
        try
        {
            ratingStatsList = RatingsStatsLocalServiceUtil.dynamicQuery(dynamicQuery, start, end);
        } catch (SystemException e)
        {
            LOGGER.error(e.getMessage());
        }
        return ratingStatsList;

    }

    public static int getMostRatedArticlesCount(long companyId, long groupId, boolean isCompanyScope)
    {
        int size = 0;
        List<RatingsStats> ratingStatsList = getMostRatedArticles(companyId, groupId, QueryUtil.ALL_POS,
                QueryUtil.ALL_POS, isCompanyScope);
        if (Validator.isNotNull(ratingStatsList))
        {
            size = ratingStatsList.size();
        }
        return size;

    }

    public static int getMostCommentedArticlesCount(long companyId, long groupId, boolean isCompanyScope)
    {
        int size = 0;
        List<MBMessageVO> ratingStatsList = getMostCommentedArticles(companyId, groupId, QueryUtil.ALL_POS,
                QueryUtil.ALL_POS, isCompanyScope);
        if (Validator.isNotNull(ratingStatsList))
        {
            size = ratingStatsList.size();
        }
        return size;

    }

    public static DynamicQuery getRatingQuery(long comapnyId, long groupId, boolean isCompanyScope)
    {

        DynamicQuery dq0 = DynamicQueryFactoryUtil.forClass(JournalArticle.class, "journalarticle");
        dq0 = dq0.setProjection(ProjectionFactoryUtil.property("resourcePrimKey"));
        dq0 = dq0.add(PropertyFactoryUtil.forName("journalarticle.companyId").eq(comapnyId));
        dq0= dq0.add(PropertyFactoryUtil.forName("journalarticle.status").ne(WorkflowConstants.STATUS_IN_TRASH));
        if (!isCompanyScope)
        {
            dq0 = dq0.add(PropertyFactoryUtil.forName("journalarticle.groupId").eq(groupId));
        }
        dq0 = dq0.add(PropertyFactoryUtil.forName("ratingstats.averageScore").ne(0.0));

        DynamicQuery query = DynamicQueryFactoryUtil.forClass(RatingsStats.class, "ratingstats")
                .add(PropertyFactoryUtil.forName("ratingstats.classPK").in(dq0))
                .addOrder(OrderFactoryUtil.desc("ratingstats.averageScore"));
        return query;

    }

    public static DynamicQuery getCommentedQuery(long comapnyId, long groupId, boolean isCompanyScope)
    {

        ProjectionList plist = ProjectionFactoryUtil.projectionList();
        plist.add(ProjectionFactoryUtil.groupProperty("classPK"));
        plist.add(ProjectionFactoryUtil.rowCount(), "_count");

        DynamicQuery querySub = DynamicQueryFactoryUtil.forClass(MBMessage.class,
                PortalClassLoaderUtil.getClassLoader());
        querySub = querySub.setProjection(plist);
        querySub = querySub.add(PropertyFactoryUtil.forName("classNameId").eq(
                PortalUtil.getClassNameId(JournalArticle.class.getName())));
        querySub = querySub.add(PropertyFactoryUtil.forName("companyId").eq(comapnyId));
        if (!isCompanyScope)
        {
            querySub = querySub.add(PropertyFactoryUtil.forName("groupId").eq(groupId));
        }
        querySub = querySub.add(PropertyFactoryUtil.forName("parentMessageId").ne(0L));
        querySub = querySub.addOrder(OrderFactoryUtil.desc("_count"));
        return querySub;
    }

    public static boolean isComapnyScope(RenderRequest renderRequest)
    {

        boolean isCompanyScope = false;
        PortletPreferences pref = renderRequest.getPreferences();
        String scope = pref.getValue("scope", "Current");
        if (Validator.equals("Company", scope))
        {
            isCompanyScope = true;
        }
        return isCompanyScope;
    }

    public static long getPreviewPlid(JournalArticle article, ThemeDisplay themeDisplay) throws Exception
    {

        if ((article != null) && Validator.isNotNull(article.getLayoutUuid()))
        {
            Layout layout = LayoutLocalServiceUtil.getLayoutByUuidAndCompanyId(article.getLayoutUuid(),
                    themeDisplay.getCompanyId());

            return layout.getPlid();
        }

        Layout layout = LayoutLocalServiceUtil.fetchFirstLayout(themeDisplay.getScopeGroupId(), false,
                LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);

        if (layout == null)
        {
            layout = LayoutLocalServiceUtil.fetchFirstLayout(themeDisplay.getScopeGroupId(), true,
                    LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);
        }

        if (layout != null)
        {
            return layout.getPlid();
        }

        return themeDisplay.getPlid();
    }

    public static List<JournalContentSearch> getArticleLocationList(long groupId, String articleId)
    {

        List<JournalContentSearch> journalContentSearchList = null;
        try
        {
            journalContentSearchList = JournalContentSearchLocalServiceUtil.getArticleContentSearches(groupId,
                    articleId);
        } catch (SystemException e)
        {
            LOGGER.error(e.getMessage());
        }
        return journalContentSearchList;
    }

    public static String getPageURL(Layout layout, String portalURL)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(portalURL);

        if (Validator.isNotNull(layout))
        {
            if (layout.isPrivateLayout())
            {
                sb.append(PortalUtil.getPathFriendlyURLPrivateGroup());
            } else
            {
                sb.append(PortalUtil.getPathFriendlyURLPublic());
            }

            try
            {
                sb.append(layout.getGroup().getFriendlyURL());
            } catch (PortalException e)
            {
                LOGGER.error(e.getMessage());
            } catch (SystemException e)
            {
                LOGGER.error(e.getMessage());
            }
            sb.append(layout.getFriendlyURL());
        }
        return sb.toString();
    }

    public static Layout getLayout(JournalContentSearch journalContentSearch)
    {

        Layout layout = null;
        try
        {
            layout = LayoutLocalServiceUtil.getLayout(journalContentSearch.getGroupId(),
                    journalContentSearch.getPrivateLayout(), journalContentSearch.getLayoutId());
        } catch (PortalException e)
        {
            LOGGER.error(e.getMessage());
        } catch (SystemException e)
        {
            LOGGER.error(e.getMessage());
        }
        return layout;
    }

    private static final Log LOGGER = LogFactoryUtil.getLog(PopularArticleUtil.class);

}
