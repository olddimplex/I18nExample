package i18n;

/**
 * Key object to serve the translation map. <br/>
 * Supports a case-insensitive search.
 */
public class LngKey {
    
    private String language;
    private String phrase;

    /**
     * Default constructor
     */
    public LngKey() {
    }

    /**
     * Constructor with all fields.
     * 
     * @param language
     * @param phrase
     */
    public LngKey(String language, String phrase) {
        super();
        this.language = language;
        this.phrase = phrase;
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
     * @return the phrase
     */
    public String getPhrase() {
        return phrase;
    }

    /**
     * @param phrase the phrase to set
     */
    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((language == null) ? 0 : language.toUpperCase().hashCode());
        result = prime * result + ((phrase == null) ? 0 : phrase.toUpperCase().hashCode());
        return result;
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LngKey other = (LngKey) obj;
        if (language == null) {
            if (other.language != null)
                return false;
        } else if (!language.equalsIgnoreCase(other.language))
            return false;
        if (phrase == null) {
            if (other.phrase != null)
                return false;
        } else if (!phrase.equalsIgnoreCase(other.phrase))
            return false;
        return true;
    }    
}
