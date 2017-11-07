<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ taglib prefix="i18n" uri="/WEB-INF/taglibs/i18n-custom.tld" %>
<c:set var="cls" scope="page" value="${fn:escapeXml(param.selectorClass)}"/>
<c:set var="totalPages" scope="page" value="${fn:escapeXml(requestScope[totalDataPagesMapAttributeName][cls])}"/>
<c:set var="startText" scope="page" value="${i18n:translate('Start',language)}"/>
<c:set var="endText" scope="page" value="${i18n:translate('End',language)}"/>
<c:set var="previousText" scope="page" value="${i18n:translate('Previous',language)}"/>
<c:set var="nextText" scope="page" value="${i18n:translate('Next',language)}"/>
<c:set var="pageNavigationText" scope="page" value="${i18n:translate('Page navigation',language)}"/>
<input type="hidden" name="${cls}-total-data-pages" value="${totalPages}"/>
<%-- 
	The scripts below are intentionally made non-executable. They are solely used to provide textual content to JS code.
--%>
<script type="text/template" data-template="${cls}-start-button-item">
	<li class="start">
		<a href="?<c:out value="${cls}"/>=1" aria-label="<c:out value='${startText}'/>" class="addLoader"><c:out value="${startText}"/></a>
	</li>
	<li class="prev">
		<a href="?<c:out value="${cls}"/>={{page}}" aria-label="<c:out value='${previousText}'/>" class="addLoader">&laquo;</a>
	</li>
</script>
<script type="text/template" data-template="${cls}-active-button-item">
	<li class="page active">
		<a href="?<c:out value="${cls}"/>={{page}}" class="addLoader">{{page}}</a>
	</li>
</script>
<script type="text/template" data-template="${cls}-regular-button-item">
	<li class="page">
		<a href="?<c:out value="${cls}"/>={{page}}" class="addLoader">{{page}}</a>
	</li>
</script>
<script type="text/template" data-template="${cls}-end-button-item">
	<li class="next">
		<a href="?<c:out value="${cls}"/>={{page}}" aria-label="<c:out value='${nextText}'/>" class="addLoader">&raquo;</a>
	</li>
	<li class="end">
		<a href="?<c:out value="${cls}"/>=${totalPages}" aria-label="<c:out value='${endText}'/>" class="addLoader"><c:out value="${endText}"/></a>
	</li>
</script>
<nav aria-label="<c:out value='${pageNavigationText}'/>" class="text-center">
    <ul class="${cls} pagination"></ul>
</nav>
