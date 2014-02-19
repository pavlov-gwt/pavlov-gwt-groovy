package com.github.pavlov.gwt.spec;

import groovy.lang.Closure;

/**
 * Static class for a utility method that can be statically imported that creates a new specification simply by calling
 * given
 */
public class Spec {

    private Spec() {}

    /**
     * @see Specification#given(String, groovy.lang.Closure)
     */
	public static Specification given(String givenText, Closure<?> c) {
        return new Specification().given(givenText, c);
    }
}
