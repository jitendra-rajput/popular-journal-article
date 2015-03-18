<%-- Copyright © 2011 DAIICHI SANKYO COMPANY, LIMITED. All Rights Reserved.
 *
 * @project Popular Articles
 * @author Jitendra Rajput
 * @description Configuration For popular articles
 *
--%>

<%@ include file="init.jsp" %>

<%

	String[] deltaArray = PortletProps.get("popular.article.delta.values").split(StringPool.COMMA);
	String redirect = ParamUtil.getString(request, "redirect");
	String customDelta = preferences.getValue("customDelta", StringPool.BLANK);
	String scope = preferences.getValue("scope", StringPool.BLANK);
		
%>


<form action="<liferay-portlet:actionURL portletConfiguration="true" />" method="post" name="<portlet:namespace />fm">
	<input name="<portlet:namespace />redirect" type="hidden" value="<%= HtmlUtil.escape(redirect) %>" /> 
	<input name="<portlet:namespace /><%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />


	<div id="dbfields">
		<table> 
			<tr>
				<td><liferay-ui:message key="article-delta-configuration-msg" /></td>
				<td>
				<select name="<portlet:namespace />preferences--customDelta--">
				 <c:forEach var="delta" items="<%= deltaArray %>">
					<option value="${delta}"  <c:if test='<%= pageContext.getAttribute("delta").equals(customDelta) %>'>selected="selected"</c:if>>${delta}</option>
				 </c:forEach>
				</select>
				</td>
			</tr>
			
			<tr>
				<td>Scope</td>
				<td>
					<select name="<portlet:namespace />preferences--scope--">
						<option value="Current" <c:if test='<%= "Current".equals(scope) %>'>selected="selected"</c:if>>Current</option>
						<option value="Company" <c:if test='<%= "Company".equals(scope) %>'>selected="selected"</c:if>>Company</option>
					</select>
				
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<input type="submit" value="Save" class="btn btn-primary"/>
				</td>
			</tr>
		</table>
	</div>
</form>

