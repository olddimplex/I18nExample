package jsp;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ibm.icu.text.DateFormat;

/**
 * Custom JSP tag implementation to format {@link Date}s
 */
public class IcuFormatDateTagSupport extends SimpleTagSupport {
    
    private Date date;
    
    private String dateFormat;
    
    private Locale locale;

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
     * @return the {@link String} format
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * @param dateFormat the {@link String} to set
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @param locale the locale to set
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
     */
    @Override
    public void doTag() throws JspException, IOException {
        if(date != null) {
            String dateFormat = getDateFormat();
            if(dateFormat == null) {
                dateFormat = DateFormat.YEAR_NUM_MONTH_DAY;
            }
            Locale loc = getLocale();
            if(loc == null) {
                loc = ((PageContext)getJspContext()).getRequest().getLocale();
            }
            DateFormat fmt = DateFormat.getPatternInstance(dateFormat, loc);
            getJspContext().getOut().write(fmt.format(getDate()));
        }
    }
}
