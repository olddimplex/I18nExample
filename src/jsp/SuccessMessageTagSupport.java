package jsp;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import action.AServlet;

public class SuccessMessageTagSupport extends SimpleTagSupport {

	private String messageText;

	/**
	 * @return the messageText
	 */
	public void setText(final String text) {
		this.messageText = text;
	}
	
	@Override
	public void doTag() throws JspException, IOException {
		final HttpServletRequest request = this.getRequest();
		final Object controllerServletObject = request.getAttribute(AServlet.CONTROLLER_SERVLET_INSTANCE);
		if(controllerServletObject != null && AServlet.class.isInstance(controllerServletObject)) {
			final AServlet controllerServlet = (AServlet)controllerServletObject;
			controllerServlet.addSuccessMessage(request, this.messageText);
		}
	}

	private HttpServletRequest getRequest() {
		return (HttpServletRequest)this.getPageContext().getRequest();
	}

	private PageContext getPageContext() {
		return (PageContext) getJspContext();
	}
}
