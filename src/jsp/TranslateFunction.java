package jsp;

import context.ContextListener;

/**
 * Custom JSP function to translate strings
 */
public class TranslateFunction {

    /**
     * Function implementation method
     * 
     * @param phrase to translate
     * @param language Language key as stored in the translations file
     * @return Translated phrase or the phrase itself if no language is specified or no translation is found
     */
    public static String translate(String phrase, String language) {
        return (language == null)?phrase:ContextListener.translate(phrase, language);
    }
}
