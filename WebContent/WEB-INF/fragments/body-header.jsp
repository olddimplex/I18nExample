<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="util.*, filter.*, action.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>

<c:set var="language" scope="page" value="${sessionScope[languageAttributeName]}"/>
<c:set var="pageReloadUrlAttributeName" scope="page" value="<%= RequestEnrichmentFilter.RELOAD_URL_ATTRIBUTE_NAME %>"/>
<c:set var="loggedInUser" scope="page" value="${true}" />

       <%
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");

        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");

        // Force Last-Modified
        //response.setDateHeader("Last-Modified", (new Date()).getTime() );

   	    // Set refresh, autoload time as session time out, seconds
	    //response.setIntHeader("Refresh", request.getSession().getMaxInactiveInterval());//--authomatic update on every <session-time-out> sec.

  		%>

         <!-- Header -->
        <nav class="navbar navbar-toggleable-md navbar-light bg-faded">
	        <button class="navbar-toggler navbar-toggler-left" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
	        	<span class="navbar-toggler-icon"></span>
	        </button>
        	<div class="collapse navbar-collapse justify-content-end" id="navbarSupportedContent">
			    <ul class="navbar-nav pull-right">
                    <li class="nav-item">
                        <form class="form-inline" action="<c:url value='/language'/>" enctype="application/x-www-form-urlencoded">
                        	<input type="hidden" name="<%= ChangeLanguageActionServlet.LANGUAGE_PARAM_NAME %>" value="EN">
                        	<input type="hidden" name="<%= AServlet.LOCATION_PARAM_NAME %>">
	                        <button class="btn btn-link ${(language == 'EN') ? 'active' : ''}" tabindex="1">
	                            <i class="flag flag-gb"></i>
	                        </button>
                        </form>
                    </li>
                    <li class="nav-item">
                        <form class="form-inline" action="<c:url value='/language'/>" enctype="application/x-www-form-urlencoded">
                        	<input type="hidden" name="<%= ChangeLanguageActionServlet.LANGUAGE_PARAM_NAME %>" value="IN">
                        	<input type="hidden" name="<%= AServlet.LOCATION_PARAM_NAME %>">
	                        <button class="btn btn-link ${(language == 'IN') ? 'active' : ''}" tabindex="1">
                            	<i class="flag flag-in"></i>
	                        </button>
                        </form>
                    </li>
                    <li class="nav-item">
                        <form class="form-inline" action="<c:url value='/language'/>" enctype="application/x-www-form-urlencoded">
                        	<input type="hidden" name="<%= ChangeLanguageActionServlet.LANGUAGE_PARAM_NAME %>" value="FR">
                        	<input type="hidden" name="<%= AServlet.LOCATION_PARAM_NAME %>">
	                        <button class="btn btn-link ${(language == 'FR') ? 'active' : ''}" tabindex="1">
                            	<i class="flag flag-fr"></i>
	                        </button>
                        </form>
                    </li>
                </ul>       
            </div>                    
        </nav>
