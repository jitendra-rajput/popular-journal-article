package com.liferay.populararticle.portlet;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.populararticle.portlet.util.PopularArticleUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class PopularArticlePortlet extends MVCPortlet {

	

	@Override
	public void init() throws PortletException {
		 viewJSP = getInitParameter("view-content");
	}

	
	@Override
	public void doView(RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {
	
		renderRequest.setAttribute("searchContainer", PopularArticleUtil.getSearchContainer(renderRequest, renderResponse));
		include(viewJSP, renderRequest, renderResponse);
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
	 private static final Log LOGGER = LogFactoryUtil.getLog(PopularArticlePortlet.class);
	    

}
