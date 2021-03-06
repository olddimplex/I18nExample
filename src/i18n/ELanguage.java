package i18n;

import context.SessionListener;

public enum ELanguage {
	EN,
	FR,
	IN;

	/**
	 * 
	 */
	public static String findByName(final String name) {
		for(final ELanguage language : values()) {
			if(language.name().equals(name)) {
				return language.name();
			}
		}
		return SessionListener.LANGUAGE_DEFAULT;
	}
}
