package jsp;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import context.ContextListener;

/**
 * Custom JSP tag implementation to translate strings
 */
public class TranslateTagSupport extends SimpleTagSupport {
    
    String language;

    /**
     * @param language Language key as stored in the translations file
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
     */
    @Override
    public void doTag() throws JspException, IOException {
        StringWriter sw = new StringWriter();
        getJspBody().invoke(sw);
        getJspContext().getOut().write((language == null)?sw.toString():ContextListener.translate(sw.toString(), language));
    }
}
