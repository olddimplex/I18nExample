package action;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sitemap.ServletPath;
import sitemap.ViewPath;
import util.HttpUtil;
import util.ResponseWrapper;
import dao.DAOParams;
import dao.DaoCallSupport;
import dao.tz.TimezoneDao;
import domain.DisplayMessage;
import domain.TimezoneInfo;
import filter.RequestEnrichmentFilter;

/**
 * Servlet implementation class AutocompleteActionServlet
 */
@WebServlet(ServletPath.AUTOCOMPLETE)
public class AutocompleteActionServlet extends AServlet {
	
	public static final String TIMEZONE_INFO_DATA_ATTRIBUTE_NAME = "timezoneInfoDataEditable";
	/** Pagination */
	public static final String SELECTOR_CLASS_TIMEZONE_INFO = "timezoneInfoPage";
	/** Modal */
	public static final String SELECTOR_CLASS_TIMEZONE_INFO_EDIT = "timezoneInfoEdit";
	/** Timezone suggestions by abbreviation prefix */
	public static final String SELECTOR_CLASS_TIMEZONE_ABBREVIATION_SUGGESTIONS = "timezoneInfoSuggestionsByAbbreviationPrefix";
	/** Display messages */
	public static final String SELECTOR_CLASS_DISPLAY_MESSAGES = "displayMessages";
	/** Holds the id of a {@link TimezoneInfo} object. */
	public static final String TIMEZONE_ID_PARAM_NAME = "timezoneid"; // keep it lowercase
	/** Holds the abbreviation of a {@link TimezoneInfo} object. */
	public static final String TIMEZONE_ABBREVIATION_PARAM_NAME = "timezoneabbr";
	/** Holds the name of a {@link TimezoneInfo} object. */
	public static final String TIMEZONE_NAME_PARAM_NAME = "timezonename";
	/** Holds the offset of a {@link TimezoneInfo} object. */
	public static final String TIMEZONE_OFFSET_PARAM_NAME = "timezoneoffset";
	
	private static final long serialVersionUID = -5837825589483822995L;
	
	private static final Logger LOGGER = LogManager.getLogger(AutocompleteActionServlet.class);
	
	private final Integer PAGE_SIZE = Integer.valueOf(10);
	
	private final transient TimezoneDao timezoneDao;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AutocompleteActionServlet() {
        super();
        this.timezoneDao = new TimezoneDao();
    }

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.getServletContext().setAttribute("timezoneInfoDataEditableAttributeName", TIMEZONE_INFO_DATA_ATTRIBUTE_NAME);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (HttpUtil.acceptsJSON(request)) {
			this.respondWithJson(request, response);
		} else {
			try {
				setDaoCallSupportAttributeForPage(request);
				setTotalPagesMapAttribute(request);
			} catch (final Exception e) {
				this.getLogger().error("Failed to populate", e);
			} finally {
				request.getRequestDispatcher(ViewPath.AUTOCOMPLETE).forward(request, response);
			}
		}
	}

	/**
	 * @see {@link AServlet#respondWithJson(HttpServletRequest, HttpServletResponse)}
	 */
	@Override
	protected void respondWithJsonML(
			final HttpServletRequest request,
			final HttpServletResponse response,
			final String fragmentClassName) throws Exception {
		switch(fragmentClassName) {
			case SELECTOR_CLASS_TIMEZONE_INFO: 
				setDaoCallSupportAttributeForPage(request);
				setTotalPagesMapAttribute(request);
				this.includeAsJsonML(ViewPath.FRAGMENT_TIMEZONE_INFO_EDITABLE_PAGE, request, new ResponseWrapper(response), response);
				break;
			case SELECTOR_CLASS_TIMEZONE_INFO_EDIT: 
				this.renderTimezoneInfoEditForm(request, response);
				break;
			case SELECTOR_CLASS_TIMEZONE_ABBREVIATION_SUGGESTIONS:
				this.respondWithTimezoneSuggestionList(request, response);
				break;
			case SELECTOR_CLASS_DISPLAY_MESSAGES:
				this.respondWithDisplayMessageList(request, response);
				break;
			default:
				break;
		}
//		response.getOutputStream().print(',');
//		// errors
//		this.includeErrorListAsJsonML(request, response);
	}

	private void setDaoCallSupportAttributeForPage(final HttpServletRequest request) {
		final DAOParams callParams = new DAOParams();
		callParams.addParameter(
			TimezoneDao.ABBREVIATION_PARAM_NAME, 
			request.getParameter(TIMEZONE_ABBREVIATION_PARAM_NAME));
		callParams.addParameter(
			TimezoneDao.PAGE_PARAMETER_NAME,
			HttpUtil.getParamAsInt(request, SELECTOR_CLASS_TIMEZONE_INFO, Integer.valueOf(1)));
		callParams.addParameter(TimezoneDao.PAGE_SIZE_PARAMETER_NAME, PAGE_SIZE);
		
		request.setAttribute(
			DAO_CALL_SUPPORT_ATTRIBUTE_NAME, 
			new DaoCallSupport(this.timezoneDao, callParams));
	}

	private void setTotalPagesMapAttribute(final HttpServletRequest request) {
		final Map<String, Integer> totalDataPagesMap = new HashMap<>(1);
		final String abbrPrefix = request.getParameter(TIMEZONE_ABBREVIATION_PARAM_NAME);
		totalDataPagesMap.put(
			SELECTOR_CLASS_TIMEZONE_INFO, 
			AServlet.getTotalPages(this.timezoneDao.getTimezoneInfoTotal(abbrPrefix), PAGE_SIZE));
		AServlet.setTotalPagesMap(request, totalDataPagesMap);
	}

	private void renderTimezoneInfoEditForm(final HttpServletRequest request, final HttpServletResponse response) {
		final DAOParams callParams = new DAOParams();
		callParams.addParameter(
			TimezoneDao.ID_PARAMETER_NAME,
			HttpUtil.getParamAsInt(request, TIMEZONE_ID_PARAM_NAME, null));
		request.setAttribute(
			TIMEZONE_INFO_DATA_ATTRIBUTE_NAME, 
			this.timezoneDao.find(callParams));
		this.includeAsJsonML(ViewPath.FRAGMENT_TIMEZONE_INFO_FORM, request, new ResponseWrapper(response), response);
	}

	private void respondWithTimezoneSuggestionList(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException, SQLException {
		final String tzAbbrParamValue = request.getParameter(QUERY_PARAMETER_NAME);
		if(tzAbbrParamValue != null) {
			final String tzAbbrPrefix = URLDecoder.decode(tzAbbrParamValue, "UTF-8").toUpperCase();
			// Intentionally not using response.getWriter() 
			final Writer writer = new OutputStreamWriter(response.getOutputStream(), RequestEnrichmentFilter.CHARACTER_ENCODING);
			writer.write('[');
			String delimiter = "";
			for (final TimezoneInfo postalCode : this.timezoneDao.findByAbbreviationPrefix(tzAbbrPrefix)) {
				writer.write(delimiter);
				writer.write(String.valueOf(postalCode));
				delimiter = ",";
			}
			writer.write(']');
			writer.flush();
		}
	}

	private void respondWithDisplayMessageList(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException, SQLException {
		final Deque<DisplayMessage> displayMessageDeque = this.getDisplayMessageDeque(request);
		Collection<DisplayMessage> coll = null;
		while(!displayMessageDeque.isEmpty()) {
			if(coll == null) {
				coll = new LinkedList<DisplayMessage>();
			}
			try {
				coll.add(displayMessageDeque.removeFirst());
			} catch (NoSuchElementException e) {
				break;
			}
		}
		if(coll != null && !coll.isEmpty()) {
			request.setAttribute(
				DISPLAY_MESSAGE_COLLECTION_PARAMETER_NAME, 
				coll);
			this.includeAsJsonML(ViewPath.FRAGMENT_ALERT, request, new ResponseWrapper(response), response);
		}
	}
}
