package action;

import java.io.IOException;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Logger;

import util.IStringTransformer;
import util.JsonUtil;
import util.ResponseWrapper;
import context.ContextListener;
import context.SessionListener;
import dao.DaoCallSupport;
import domain.DisplayMessage;
import domain.EDisplayMessageType;

public abstract class AServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6874573652767919424L;

	/** For use with AJAX calls. Holds the marker class of the HTML element to update. */
	public static final String SELECTOR_CLASS_PARAM_NAME = "classname";

	/** For use with AJAX calls. Holds the URL of the current page as seen by the browser. */
	public static final String LOCATION_PARAM_NAME = "location";
	/** */
	public static final String QUERY_PARAMETER_NAME = "query";

	/** Default page number to render */
	public static final String PAGE_NUMBER_DEFAULT = "1";

	/**
	 * Name of the request attribute holding the map of <selector class name> =>
	 * <total number of pages available>
	 */
	public static final String TOTAL_PAGES_MAP_ATTRIBUTE_NAME = "totalDataPagesMap";

	/**
	 * Name of the request attribute holding an instance of
	 * {@link DaoCallSupport}
	 */
	public static final String DAO_CALL_SUPPORT_ATTRIBUTE_NAME = "dynamicIncludeDaoCallSupport";

	public static final String DISPLAY_MESSAGE_DEQUE_KEY = "displayMessageDeque";

	public static final String DISPLAY_MESSAGE_COLLECTION_PARAMETER_NAME = "displayMessageCollection";

	/** For reference from within JSP */
	public static final String CONTROLLER_SERVLET_INSTANCE = "controllerServletInstance";

	@Override
	public void init(final ServletConfig config) throws ServletException {
		super.init(config);
		this.getServletContext().setAttribute("totalDataPagesMapAttributeName", TOTAL_PAGES_MAP_ATTRIBUTE_NAME);
		this.getServletContext().setAttribute("languageAttributeName", SessionListener.LANGUAGE_ATTRIBUTE_NAME);
	}

	@Override
	protected void service(
			final HttpServletRequest request, 
			final HttpServletResponse response
			) throws ServletException, IOException {
		request.setAttribute(CONTROLLER_SERVLET_INSTANCE, this);
		super.service(request, response);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

	/**
	 * Calculates the total pages as expected by the pagination solution.
	 * 
	 * @param totalRows
	 * @param pageSize
	 * @return
	 */
	public static final int getTotalPages(final int totalRows, final int pageSize) {
		int totalPages = totalRows / pageSize;
		if (totalRows % pageSize != 0) {
			totalPages++;
		}
		return totalPages;
	}

	/**
	 * Set the map of <selector class name> => <total pages> request attribute
	 * as expected by the pagination solution.
	 *
	 * @param request
	 * @param totalPagesMap
	 */
	public static final void setTotalPagesMap(final HttpServletRequest request, final Map<String, Integer> totalPagesMap) {
		request.setAttribute(TOTAL_PAGES_MAP_ATTRIBUTE_NAME, totalPagesMap);
	}

	/**
	 * For JSONML format, see: <a href="http://www.jsonml.org/">jsonml.org</a>
	 *
	 * @see {@link JsonUtil#toJsonML(java.io.InputStream, java.io.OutputStream)}
	 */
	protected final void includeAsJsonML(
			final String resourceUri, 
			final HttpServletRequest request, 
			final ResponseWrapper httpResponseWrapper,
			final HttpServletResponse response) {
		try {
			request.getRequestDispatcher(resourceUri).include(request, httpResponseWrapper);
			JsonUtil.toJsonML(httpResponseWrapper.getBytes(), response.getOutputStream());
		} catch (final Exception e) {
			this.getLogger().error("INTERNAL SERVER ERROR", e);
		}
	}

	/**
	 * Prints a common JSON structure to response stream.<br/>
	 * Relies on {@link #respondWithJsonML(HttpServletRequest, HttpServletResponse)}
	 * for the actual content part.
	 */
	protected void respondWithJson(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		final String fragmentClassName = request.getParameter(SELECTOR_CLASS_PARAM_NAME);
		if (fragmentClassName != null) {
			try {
				response.setContentType("application/json");
				response.getOutputStream().print("{\"content\":[");
				this.respondWithJsonML(request, response, fragmentClassName);
				response.getOutputStream().print(']');
				final Object totalDataPagesMap = 
					request.getAttribute(TOTAL_PAGES_MAP_ATTRIBUTE_NAME);
				if(totalDataPagesMap != null) {
					response.getOutputStream().print(",\"meta\":");
					response.getOutputStream().print(JsonUtil.toJsonNoHtmlEscaping(totalDataPagesMap));
				}
				response.getOutputStream().print('}');
			} catch (final Exception e) {
				this.getLogger().error("Failed to process an AJAX request", e);
			}
		}
	}

	/**
	 * Noop implementation, designed for overriding.
	 * 
	 * @param request
	 * @param response
	 */
	protected void respondWithJsonML(
			final HttpServletRequest request, 
			final HttpServletResponse response,
			final String fragmentClassName
			) throws Exception {
	}

	public void addErrorMessage(
			final HttpServletRequest request, 
			final String messageText, 
			final IStringTransformer transformer) {
		this.getLogger().error(transformer.transform(messageText));
		final String messageTextTranslated = ContextListener.translate(messageText, request.getSession(false));
		this.addMessage(request, transformer.transform(messageTextTranslated), EDisplayMessageType.ERROR);
	}

	public void addErrorMessage(
			final HttpServletRequest request, 
			final String messageText, 
			final Throwable throwable,
			final IStringTransformer transformer) {
		this.getLogger().error(transformer.transform(messageText), throwable);
		final String messageTranslated = ContextListener.translate(messageText, request.getSession(false));
		this.addMessage(request, transformer.transform(messageTranslated), EDisplayMessageType.ERROR);
	}

	public void addErrorMessage(final HttpServletRequest request, final String messageText) {
		this.addMessage(request, messageText, EDisplayMessageType.ERROR);
	}

	public void addSuccessMessage(final HttpServletRequest request, final String messageText) {
		this.addMessage(request, messageText, EDisplayMessageType.SUCCESS);
	}

	public void addSuccessMessage(
			final HttpServletRequest request, 
			final String messageText, 
			final IStringTransformer transformer) {
		this.getLogger().error(transformer.transform(messageText));
		final String messageTextTranslated = ContextListener.translate(messageText, request.getSession(false));
		this.addMessage(request, transformer.transform(messageTextTranslated), EDisplayMessageType.SUCCESS);
	}

	private void addMessage(
			final HttpServletRequest request, 
			final String messageText, 
			final EDisplayMessageType displayMessageType) {
		final HttpSession session = request.getSession(false);
		if (session != null) {
			final DisplayMessage displayMessage = new DisplayMessage(displayMessageType, messageText);
			this.getDisplayMessageDeque(session).add(displayMessage);
		}
	}

	public Deque<DisplayMessage> getDisplayMessageDeque(final HttpServletRequest request) {
		final HttpSession session = request.getSession(false);
		if (session != null) {
			return this.getDisplayMessageDeque(session);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private Deque<DisplayMessage> getDisplayMessageDeque(final HttpSession session) {
		final Object displayMessageDequeObj = session.getAttribute(DISPLAY_MESSAGE_DEQUE_KEY);
		if (displayMessageDequeObj != null && Deque.class.isInstance(displayMessageDequeObj)) {
			return (Deque<DisplayMessage>) displayMessageDequeObj;
		} else {
			final Deque<DisplayMessage> displayMessageCollection = new ConcurrentLinkedDeque<>();
			session.setAttribute(DISPLAY_MESSAGE_DEQUE_KEY, displayMessageCollection);
			return displayMessageCollection;
		}
	}

	protected abstract Logger getLogger();
}
