<%@ page pageEncoding="UTF-8" %>
<%@ page import="sitemap.*" %>
<%@ page import="action.*" %>
<%@ page import="java.util.Date"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cust" uri="/WEB-INF/taglibs/custom.tld" %>
<%@ taglib prefix="i18n" uri="/WEB-INF/taglibs/i18n-custom.tld" %>
<c:set var="selectorClassName" scope="page" value="<%=EditableActionServlet.SELECTOR_CLASS_TIMEZONE_INFO %>"/>
<c:set var="closeText" scope="page" value="${i18n:translate('Close', language)}"/>
<!DOCTYPE html>
<html lang="en">
  <head>
	<jsp:include page="<%=ViewPath.FRAGMENT_HEAD_HEADER %>"/>
  </head>
  <body>
  	<div id="loader" class="loadcloak"></div>
	<div class="container col-md-10">
		<table class="table table-sm">
		  <thead>
		    <tr>
		      <th><c:out value="${i18n:translate('Abbr.', language)}"/></th>
		      <th><c:out value="${i18n:translate('Name', language)}"/></th>
		      <th><c:out value="${i18n:translate('UTC offset', language)}"/></th>
		      <th/>
		    </tr>
		  </thead>
		  <tbody class="${selectorClassName}">
		  	<jsp:include page="<%=ViewPath.FRAGMENT_TIMEZONE_INFO_EDITABLE_PAGE %>"/>
		  </tbody>
		</table>
		<jsp:include page="<%=ViewPath.FRAGMENT_PAGINATION %>">
			<jsp:param value="${selectorClassName}" name="selectorClass"/>
		</jsp:include>
	</div>
<%-- Modals --%>
	<div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title" id="exampleModalLabel"><c:out value="${i18n:translate('Edit timezone', language)}"/></h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="<c:out value="${closeText}"/>">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body <%=EditableActionServlet.SELECTOR_CLASS_TIMEZONE_INFO_EDIT %>"></div>
	    </div>
	  </div>
	</div>
	<script type="text/template" data-template="loading-indicator">
		<div class="loading-indicator">&nbsp;</div>
	</script>
	<jsp:include page="<%=ViewPath.FRAGMENT_BODY_FOOTER %>"/>
  </body>
</html>