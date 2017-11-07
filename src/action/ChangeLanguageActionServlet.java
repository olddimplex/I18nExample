package action;

import java.io.IOException;
import java.security.AccessControlException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sitemap.ServletPath;
import context.SessionListener;
import domain.ELanguage;

/**
 * The Class ChangeLanguageActionServlet.
 */
@WebServlet(name = "ChangeLanguageActionServlet", urlPatterns = ServletPath.LANGUAGE, loadOnStartup = 1)
public final class ChangeLanguageActionServlet extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(ChangeLanguageActionServlet.class);

	/** Name of the request parameter holding the language code */
	public static final String LANGUAGE_PARAM_NAME = "language";

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(final ServletConfig config) throws ServletException {
		//
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.
	 * HttpServletRequest , javax.servlet.http.HttpServletResponse)
	 */
	// ---------------------------------------------------------------------------------------------------------------------
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		this.doPost(request, response);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.
	 * HttpServletRequest , javax.servlet.http.HttpServletResponse)
	 */
	// ---------------------------------------------------------------------------------------------------------------------
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		final String language = ELanguage.findByName(request.getParameter(LANGUAGE_PARAM_NAME));
		final String pageReloadUrl = request.getParameter(AServlet.LOCATION_PARAM_NAME);

		if (language != null) {
			request.getSession().setAttribute(SessionListener.LANGUAGE_ATTRIBUTE_NAME, language);
		}
		if (pageReloadUrl != null) {
			try {
				response.sendRedirect(pageReloadUrl);
			} catch (final AccessControlException e) {
				LOGGER.error(e);
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
		} else {
			response.setStatus(304);
		}

	}

}
