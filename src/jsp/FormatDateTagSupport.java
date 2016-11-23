package jsp;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Custom JSP tag implementation to format {@link Date}s
 */
public class FormatDateTagSupport extends SimpleTagSupport {
    
    private Date date;
    
    private DateFormat dateFormat;

    /**
     * @return the {@link Date}
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the {@link Date} to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the {@link DateFormat} format
     */
    public DateFormat getDateFormat() {
        if(dateFormat == null) {
            dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, ((PageContext)getJspContext()).getRequest().getLocale());
        }
        return dateFormat;
    }

    /**
     * @param dateFormat the {@link DateFormat} to set
     */
    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
     */
    @Override
    public void doTag() throws JspException, IOException {
        if(date != null) {
            getJspContext().getOut().write(getDateFormat().format(getDate()));
        }
    }
}
