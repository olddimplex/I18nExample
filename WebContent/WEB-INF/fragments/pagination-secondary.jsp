<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ taglib prefix="i18n" uri="/WEB-INF/taglibs/i18n-custom.tld" %>
<c:set var="pageNavigationText" scope="page" value="${i18n:translate('Page navigation',language)}"/>
<nav aria-label="<c:out value='${pageNavigationText}'/>" class="text-center">
    <ul class="${fn:escapeXml(param.selectorClass)} pagination" data-secondary="yes"></ul>
</nav>
