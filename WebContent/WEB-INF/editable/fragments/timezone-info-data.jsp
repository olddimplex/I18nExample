<%@ page pageEncoding="UTF-8" %>
<%@ page import="action.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="i18n" uri="/WEB-INF/taglibs/i18n-custom.tld" %>
<c:set var="timezoneInfoData" scope="page" value="${requestScope[timezoneInfoDataEditableAttributeName]}"/>
<c:set var="editText" scope="page" value="${i18n:translate('Edit', language)}"/>
<c:forEach var="timezoneInfo" items="${timezoneInfoData}">
<tr>
	<td><c:out value="${timezoneInfo.abbreviation}" /></td>
	<td><c:out value="${timezoneInfo.name}" /></td>
	<td><c:out value="${timezoneInfo.offset}" /></td>
	<td>
<%-- Note the data- attribute names are in lowercase - JQuery will convert them anyway, so be aware --%>
		<input 
			type="button" data-toggle="modal" data-target="#editModal" value="<c:out value='${editText}'/>"
			class="btn btn-primary ajax-update"
			data-timezoneid="${timezoneInfo.id}"
			data-classname="<%=EditableActionServlet.SELECTOR_CLASS_TIMEZONE_INFO_EDIT %>"
			/>
	</td>
</tr>
</c:forEach>