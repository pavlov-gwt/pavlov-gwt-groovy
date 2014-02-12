package org.pavlov.spec;

import groovy.lang.Closure;
import org.pavlov.report.SpecReporter;
import org.pavlov.report.StandardOutSpecReporter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Specification {

    public enum State {
        NONE,
        GIVEN,
        WHEN,
        THEN
    }

    protected State currentState = State.NONE;

    public Specification given(String givenText, Closure c) {
        return handleAction(givenText, c, State.GIVEN);
    }

    public Specification when(String whenText, Closure c) {
        return handleAction(whenText, c, State.WHEN);
    }

    public Specification then(String thenText, Closure c) {
        return handleAction(thenText, c, State.THEN);
    }

    public Specification and(String text, Closure c) {
        if (currentState == State.NONE) {
           throw new RuntimeException("Cannot call 'and' before first calling given or when.");
        }
        handleAction(text, c, currentState);
        return this;
    }

    protected SpecReporter reporter = new StandardOutSpecReporter();

    //TODO allow multiple return values
    protected Object returnValue;

    protected Specification handleAction(String text, Closure c, State expected) {
        boolean newState = validateState(expected);

        String report = expected + ": " + text;
        if (!newState) {
           report = "AND " + report;
        }
        reporter.report(report);
        //TODO delegate should have access to the return from given, when, etc as well
        c.setDelegate(this);
        try {
            returnValue = c.call(returnValue);
        }
        catch(Throwable t) {
            //we only allow exceptions to be thrown in the when
            if(expected == State.WHEN) {
                returnValue = t;
            } else {
                //TODO make custom exception
                throw new RuntimeException("An exception occurred during " + expected, t);
            }
        }
        return this;
    }

    /**
     * Makes sure that setting the current state to the new state is allowed, and if so, sets it
     * @param newState the state that should be set in this call
     * @return true if a new state was set
     */
    protected boolean validateState(State newState) {
        int compare = currentState.compareTo(newState);
        if ( compare > 0) {
            //TODO make custom exception
            throw new RuntimeException("Cannot add a " + newState + " when the current state is " + currentState);
        }
        currentState = newState;
        return compare != 0;
    }
}
