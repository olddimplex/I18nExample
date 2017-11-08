<%@page import="action.AServlet"%>
<%@ page pageEncoding="UTF-8" %>
<%@ page import="action.*, domain.EDisplayMessageType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="i18n" uri="/WEB-INF/taglibs/i18n-custom.tld" %>
<c:set var="messageCollectionParamName" scope="page" value="<%=AServlet.DISPLAY_MESSAGE_COLLECTION_PARAMETER_NAME %>"/>
<c:set var="messageCollection" scope="page" value="${requestScope[messageCollectionParamName]}"/>
<c:forEach var="message" items="${requestScope[messageCollectionParamName]}">
	<c:if test="${not empty message}">
		<c:choose>
			<c:when test="${message.messageType == EDisplayMessageType.SUCCESS}">
				<c:set var="alertClass" value="alert-success" />
			</c:when>
			<c:when test="${message.messageType == EDisplayMessageType.ERROR}">
				<c:set var="alertClass" value="alert-danger" />
			</c:when>
			<c:otherwise>
				<c:set var="alertClass" value="alert-info" />
			</c:otherwise>
		</c:choose>
	
		<div class="alert ${alertClass} alert-dismissible" role="alert">
			<button type="button" class="close" data-dismiss="alert" aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<strong>${i18n:translate(message.messageType.label, language)}</strong>&nbsp;<c:out value="${i18n:translate(message.messageText, language)}" />
		</div>
	</c:if>
</c:forEach>