package i18n;

import java.sql.Timestamp;

/** 
 *  
 */
public class Translation {
    
    private String origin;
    private String result;
    private String language;
    private Timestamp changeDate;
    

    /**
     * Default constructor
     */
    public Translation() {
        super();
    }

    /**
     * Copy constructor
     * 
     * @param translation
     */
    public Translation(Translation translation) {
        super();
        if(translation != null) {
            this.origin = translation.getOrigin();
            this.result = translation.getResult();
            this.language = translation.getLanguage();
            this.changeDate = translation.getChangeDate();
        }
    }

    /**
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the changeDate
     */
    public Timestamp getChangeDate() {
        return changeDate;
    }

    /**
     * @param changeDate the changeDate to set
     */
    public void setChangeDate(Timestamp changeDate) {
        this.changeDate = changeDate;
    }
}
