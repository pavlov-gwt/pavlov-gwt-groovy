package com.github.pavlov.gwt.spec;

import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.pavlov.gwt.report.SpecReporter;
import com.github.pavlov.gwt.report.StandardOutSpecReporter;

public class Specification {

	public enum State {
		NONE, GIVEN, WHEN, THEN
	}

	protected State currentState = State.NONE;

	protected List<Object> givenReturnValues = new ArrayList<Object>();
	protected List<String> givens = new ArrayList<String>();

	protected List<Object> whenReturnValues = new ArrayList<Object>();
	protected List<String> whens = new ArrayList<String>();

	protected List<Object> thenReturnValues = new ArrayList<Object>();
	protected List<String> thens = new ArrayList<String>();

	public Specification given(String givenText, Closure<?> c) {
		return handleAction(givenText, c, State.GIVEN);
	}

	public Specification when(String whenText, Closure<?> c) {
		return handleAction(whenText, c, State.WHEN);
	}

	public Specification then(String thenText, Closure<?> c) {
		return handleAction(thenText, c, State.THEN);
	}

	public Specification and(String text, Closure<?> c) {
		if (currentState == State.NONE) {
			throw new RuntimeException(
					"Cannot call 'and' before first calling given or when.");
		}
		handleAction(text, c, currentState);
		return this;
	}

	protected SpecReporter reporter = new StandardOutSpecReporter();

	protected Specification handleAction(String text, Closure<?> c, State expected) {
		boolean newState = validateState(expected);

		// format and report the message
		String report = expected + ": " + text;
		if (!newState) {
			report = "AND " + report;
		}
		reporter.report(report);

		List<Object> parameters = new ArrayList<Object>(0);
		List<Object> returnValues = null;

		switch (expected) {
		case GIVEN:
			givens.add(text);
			returnValues = givenReturnValues;
			break;

		case WHEN:
			whens.add(text);
			parameters = givenReturnValues;
			returnValues = whenReturnValues;
			break;

		case THEN:
			thens.add(text);
			parameters = whenReturnValues;
			returnValues = thenReturnValues;
			break;

		default:
			break;
		}

		// set this Specification as the delegate
		c.setDelegate(this);

		// TODO allow multiple return values
		Object newReturnValue;

		try {
			newReturnValue = c.call(parameters.toArray());
		} catch (Throwable t) {
			// we only allow exceptions to be thrown in the when
			if (expected == State.WHEN) {
				newReturnValue = t;
			} else {
				// TODO make custom exception
				throw new RuntimeException("An exception occurred during "
						+ expected, t);
			}
		}

		addReturnValues(newReturnValue, returnValues);
		return this;
	}

	protected void addReturnValues(Object newValue, List<Object> values) {
		if (newValue == null) {
			return;
		}

		if (newValue instanceof Object[]) {
			values.addAll(Arrays.asList((Object[]) newValue));
			return;
		}

		values.add(newValue);
	}

	/**
	 * Makes sure that setting the current state to the new state is allowed,
	 * and if so, sets it
	 * 
	 * @param newState
	 *            the state that should be set in this call
	 * @return true if a new state was set
	 */
	protected boolean validateState(State newState) {
		int compare = currentState.compareTo(newState);
		if (compare > 0) {
			// TODO make custom exception
			throw new RuntimeException("Cannot add a " + newState
					+ " when the current state is " + currentState);
		}
		currentState = newState;
		return compare != 0;
	}
}
