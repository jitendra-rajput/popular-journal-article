<%-- Copyright © 2011 DAIICHI SANKYO COMPANY, LIMITED. All Rights Reserved.
 *
 * @project Popular Articles
 * @author Jitendra Rajput
 * @description Configuration For popular articles
 *
--%>

<%@page import="com.liferay.portal.kernel.util.GetterUtil"%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>
<%@page import="javax.portlet.PortletPreferences"%>
<%@page import="com.liferay.portlet.PortletPreferencesFactoryUtil"%>
<%@page import="com.liferay.portal.kernel.util.Validator"%>
<%@page import="com.liferay.portal.kernel.util.HtmlUtil"%>
<%@page import="com.liferay.portal.kernel.util.Constants"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@ include file="init.jsp" %>


<%
PortletPreferences preferences = renderRequest.getPreferences();
String portletResource = ParamUtil.getString(request, "portletResource");

if (Validator.isNotNull(portletResource)) {
	preferences = PortletPreferencesFactoryUtil.getPortletSetup(request, portletResource);
}
	String redirect = ParamUtil.getString(request, "redirect");
	int customDelta = GetterUtil.getInteger(preferences.getValue("customDelta", StringPool.BLANK),10);
	if(Validator.isNull(customDelta) || customDelta <10){
		customDelta = 10;
	}
	
		
%>


<form action="<liferay-portlet:actionURL portletConfiguration="true" />" method="post" name="<portlet:namespace />fm">
	<input name="<portlet:namespace />redirect" type="hidden" value="<%= HtmlUtil.escape(redirect) %>" /> 
	<input name="<portlet:namespace /><%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />

	<div class="portlet-msg-info">
		<liferay-ui:message key="It should be in 10X format [10,20,30,50..]" />
	</div>

	<div id="dbfields">
		<table> 
			<tr>
				<td>Specify Maximum Articles Display</td>
				<td><input type="text" name="<portlet:namespace />preferences--customDelta--" value="<%=customDelta %>"/></td>
			</tr>
			<tr>
				<td colspan="2">
					<input type="submit" value="Save" />
				</td>
			</tr>
		</table>
	</div>
</form>

