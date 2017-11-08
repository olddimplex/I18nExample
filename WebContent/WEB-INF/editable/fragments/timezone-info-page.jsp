<%@ page pageEncoding="UTF-8" %>
<%@ page import="sitemap.*" %>
<%@ page import="action.*" %>
<%@ page import="jsp.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cust" uri="/WEB-INF/taglibs/custom.tld" %>
<%@ taglib prefix="i18n" uri="/WEB-INF/taglibs/i18n-custom.tld" %>
<c:set var="viewCountParamName" scope="page" value="<%=IncludeTagSupport.VIEW_COUNT_PARAM_NAME %>"/>
<cust:include 
	dao="<%=AServlet.DAO_CALL_SUPPORT_ATTRIBUTE_NAME %>" 
	dataObject="<%=EditableActionServlet.TIMEZONE_INFO_DATA_ATTRIBUTE_NAME %>" 
	view="<%=ViewPath.FRAGMENT_TIMEZONE_INFO_DATA_EDITABLE %>"
	viewCount="<%=IncludeTagSupport.VIEW_COUNT_PARAM_NAME %>"
/>
<%-- The following input tag is added unconditionally, so as to any previous messages be cleaned first --%>
<input type="hidden" name="ajax-update" value="<%=AutocompleteActionServlet.SELECTOR_CLASS_DISPLAY_MESSAGES %>"/>
<c:if test="${empty requestScope[viewCountParamName]}">
	<cust:error-message text="${i18n:translate('No data', language)}"/>
</c:if>
