package context;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The listener interface for receiving sessionCounter events. The class that is
 * interested in processing a sessionCounter event implements this interface,
 * and the object created with that class is registered with a component using
 * the component's <code>addSessionCounterListener<code> method. When
 * the sessionCounter event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see SessionCounterEvent
 */
public final class SessionListener implements HttpSessionListener {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LogManager.getLogger(SessionListener.class);

    /**
     * Name of the servlet context init parameter holding the default session
     * language
     */
    public static final String LANGUAGE_PARAMETER_NAME = "language-default";

    /** Name of the session attribute holding the language code */
    public static final String LANGUAGE_ATTRIBUTE_NAME = "language";

    public static final String LANGUAGE_DEFAULT = "EN";

    private String languageDefault;
    private volatile boolean languageDefaultSet;

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http
     * .HttpSessionEvent)
     */
    @Override
    public void sessionCreated(final HttpSessionEvent event) {
        LOGGER.info(">>>> Session Created - add one session into counter");
        final HttpSession session = event.getSession();
        if (!this.languageDefaultSet) {
            final String languageDefaultInitParam = session.getServletContext().getInitParameter(LANGUAGE_PARAMETER_NAME);
            this.languageDefault = (languageDefaultInitParam == null) ? LANGUAGE_DEFAULT : languageDefaultInitParam;
            this.languageDefaultSet = true;
        }
        session.setAttribute(LANGUAGE_ATTRIBUTE_NAME, this.languageDefault);
        // session.setMaxInactiveInterval(C_SESSION_TIMEOUT);//--set session
        // time out, could be individual per session
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(String.format(">>>>> Session Created - deduct one session from counter >>>>> SID: %s \t sessionTimeout:%s [sec]", session.getId().toUpperCase(),
                    session.getMaxInactiveInterval()));
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet
     * .http.HttpSessionEvent)
     */
    @Override
    public void sessionDestroyed(final HttpSessionEvent event) {
        if (LOGGER.isInfoEnabled()) {
            final HttpSession session = event.getSession();
            LOGGER.info(String.format(">>>>> Session Destroyed - deduct one session from counter >>>>> SID: %s", session.getId().toUpperCase()));
        }
    }
}
