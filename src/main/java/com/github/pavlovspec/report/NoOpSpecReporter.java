package com.github.pavlovspec.report;

/**
 * Default adapter implementation of SpecReporter (doesn't do anything)
 */
public class NoOpSpecReporter implements SpecReporter {

    @Override
    public void report(String report) {}
}
