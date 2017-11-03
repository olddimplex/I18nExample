package domain;

import context.SessionListener;

public enum ELanguage {
	EN,
	DA,
	NO;

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
