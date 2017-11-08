package context;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;

import domain.LngKey;
import domain.Translation;

/**
 * Implementation of {@link ServletContextListener}
 */
public class ContextListener implements ServletContextListener {
    
    private static ConcurrentHashMap<LngKey,String> TRANSLATIONS;
    
    private static volatile boolean UPDATE_IN_PROGRESS;
    
    private static AtomicLong LATEST_UPDATE_TIME = new AtomicLong(-1);

    /**
     * Lifecycle callback for cleanup purposes
     * 
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }

    /**
     * Lifecycle callback for initialization purposes. <br/>
     * 
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
        try {
            InputStreamReader reader =
                new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("translations.csv"),"UTF-16");
            while(reader.ready()) {
                String[] tokens = new String[3];
                StringWriter sw = new StringWriter();
                if(tokens.length == parseLine(reader, sw, tokens, '"', ',')) {
                    String language = tokens[0].trim();
                    if(language.length() > 0 && !language.startsWith("#")) {
                        String keyPhrase = tokens[1].trim();
                        if(keyPhrase.length() > 0) {
                            if(TRANSLATIONS == null) {
                                // Note only one thread is supposed to update the map
                                TRANSLATIONS = new ConcurrentHashMap<LngKey,String>(16, 0.75F, 1);
                            }
                            TRANSLATIONS.put(new LngKey(language, keyPhrase),tokens[2]);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private int parseLine(
        InputStreamReader reader
      , StringWriter sw
      , String[] fields
      , char quoteChar
      , char fieldDelimiter
      ) throws IOException {
        int fieldIndex = 0;
        boolean isQuoted = false;
        boolean preceededByQuote = false;
        int currentChar = -1;
        sw.getBuffer().setLength(0);
        while((currentChar = reader.read()) >= 0 && currentChar != 13 && currentChar != 10) {
            if(fieldIndex < fields.length) {
                if(currentChar == quoteChar) { 
                    isQuoted = !isQuoted;
                    if(isQuoted) {
                        if(preceededByQuote) {
                            sw.write(currentChar);
                            preceededByQuote = false;
                        }
                    } else {
                        preceededByQuote = true;
                    }
                    continue;
                } else {
                    preceededByQuote = false;
                }
                if(!isQuoted && currentChar == fieldDelimiter) {
                    fields[fieldIndex] = sw.toString();
                    sw.getBuffer().setLength(0);
                    fieldIndex++;
                    continue;
                }
                sw.write(currentChar);
            }
        }
        if(fieldIndex < fields.length) {
            fields[fieldIndex] = sw.toString();
            fieldIndex++;
        }
        return fieldIndex;
    }

    /**
     * Finds a translation of the given phrase. <br/>
     * The search is case-sensitive and no pre-processing is applied (trim, normalize, etc.)
     * 
     * @param phrase The exact phrase to translate
     * @param language Language key as stored in the translations file
     * @return The translated phrase if found, the phrase itself otherwise.
     */
    public static String translate(String phrase, String language) {
        if(TRANSLATIONS != null) {
            String translation = TRANSLATIONS.get(new LngKey(language, phrase));
            if(translation != null) {
                return translation;
            }
        }
        return phrase;
    }

	/**
	 * Finds a translation of the given phrase. <br/>
	 * The search is case-sensitive and no pre-processing is applied (trim, normalize, etc.)
	 *
	 * @param phrase
	 *            The exact phrase to translate
	 * @param session
	 *            to obtain the language code from
	 * @return The translated phrase if found, the phrase itself otherwise.
	 * @see {@link SessionListener#LANGUAGE_ATTRIBUTE_NAME}
	 */
	public static String translate(final String phrase, final HttpSession session) {
		if (session != null) {
			return translate(phrase, String.valueOf(session.getAttribute(SessionListener.LANGUAGE_ATTRIBUTE_NAME)));
		} else {
			return phrase;
		}
	}

    /**
     * Replaces the translations that are affected by the supplied list. <br/>
     * No new translations are added compared to the initial list loaded from classpath.
     * 
     * @param changedTranslarionsList
     * @return the latest change time in the list
     */
    public static Timestamp updateTranslations(List<Translation> changedTranslarionsList) {
        Timestamp latestChangeTime = null;
        if(changedTranslarionsList != null && !changedTranslarionsList.isEmpty() && !UPDATE_IN_PROGRESS) {
            UPDATE_IN_PROGRESS = true;
            for(Translation t: changedTranslarionsList) {
                if(isMeaningful(t)) {
                    TRANSLATIONS.replace(new LngKey(t.getLanguage(), t.getOrigin()),t.getResult());
                    if(t.getChangeDate() != null) {
                        if(latestChangeTime == null || t.getChangeDate().after(latestChangeTime)) {
                            latestChangeTime = t.getChangeDate();
                        }
                    }
                }
            }
            if(latestChangeTime != null) {
                LATEST_UPDATE_TIME.set(latestChangeTime.getTime());
            }
            UPDATE_IN_PROGRESS = false;
        }
        return latestChangeTime;
    }
    
    private static boolean isMeaningful(Translation t) {
        return t != null && 
               t.getOrigin() != null && t.getOrigin().trim().length() > 0 &&
               t.getLanguage() != null && t.getLanguage().trim().length() > 0;
    }

    /**
     * @return the latest update time
     */
    public static long getLatestUpdateTime() {
        return LATEST_UPDATE_TIME.get();
    }
}
