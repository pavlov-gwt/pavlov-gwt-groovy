package com.github.pavlovspec

import org.junit.Test

import com.github.pavlovspec.Spec;

/**
 * Created by Achilles on 2/12/14.
 */
class ReuseThenSpec {

	def theAnswerIsFour = { answer -> assert 4 == answer }

	@Test
	public void testTwoPlusTwo(){
		Spec.given('Two numbers, both equal to 2'){ [2, 2]}.when('I add them together'){ a , b ->
			a + b
		}.then('The answer is four', theAnswerIsFour)
	}

	@Test
	public void testTwoTimesTwo(){
		Spec.given('Two numbers, both equal to 2'){ [2, 2]}.when('I add them together'){ a , b ->
			a * b
		}.then('The answer is four', theAnswerIsFour)
	}
}
