package org.pavlov.spec

import org.junit.Test
import org.pavlov.report.DefaultSpecReporter
import org.pavlov.report.SpecReporter

import static org.pavlov.spec.Spec.given;

/**
 * Created by brigid on 2/9/14.
 */
class SpecSpec {

    @Test
    public void testSimpleSpecification() {
        given("A simple test in groovy") {
            System.println('This is my test!')
        }.when("I write the test") {
            System.println("Inside when!")
        }.then("It works like a charm!") { when ->
            ((Specification)getDelegate()).reporter.report("Inside then!")
        }
    }

    @Test
    public void testComplexSpecification() {
        given("A first given") {
        }.and("A second given") {
        }.when("I have a first when") {
        }.and("I have a second when") {
        }.then("It works like a charm!") {
        }.and("I can have a second then!") {
            System.println("I made it all the way!")
        }

    }

    /**
     * Note, this is a bit of magic.  Closures automatically return the last line of the closure.  In the when, we're not
     * actually transforming the string as making a new string that is upper case, and that's being returned and read in the then
     */
    @Test
    public void testChaining() {
        given("An object created in the given") {
            "some string"
        }.when("I transform that object in the when") { String s ->
            s.toUpperCase()
        }.then("The transformed object is available in the then") { result ->
            assert "SOME STRING" == result
        }
    }

    @Test
    public void testException() {
        given("An exception") {
            new Exception("Custom Exception")
        }.when("That exception is thrown in the when") { Exception e ->
            throw e
        }.then("That exception is passed to the then") { Exception result ->
            assert 'Custom Exception' == result.getMessage()
        }
    }

    @Test
    public void testGivenAfterWhen() {
        given("A specification that when has been called on"){
            Specification spec = new Specification()
            spec.reporter = new DefaultSpecReporter()
            spec.when('Spec under test'){}
        }.when("given() is called on it"){ spec ->
            spec.given('Spec under test'){}
        }.then('An exception is thrown'){ RuntimeException exception ->
            assert exception instanceof RuntimeException
            assert exception.getMessage().contains('GIVEN')
            assert exception.getMessage().contains('WHEN')
        }
    }

}
