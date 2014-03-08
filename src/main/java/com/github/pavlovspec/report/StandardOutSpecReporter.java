package com.github.pavlovspec.report;

public class StandardOutSpecReporter implements SpecReporter {
    @Override
    public void report(String report) {
        System.out.println(report);
    }
}
