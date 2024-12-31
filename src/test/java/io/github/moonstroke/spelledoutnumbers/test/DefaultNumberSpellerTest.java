package io.github.moonstroke.spelledoutnumbers.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.moonstroke.spelledoutnumbers.NumberSpeller;

class DefaultNumberSpellerTest {
	static NumberSpeller speller;


	@BeforeAll
	static void setUp() {
		speller = NumberSpeller.getNumberSpellerFor(Locale.getDefault());
	}

	@ParameterizedTest
	@MethodSource("validInputs")
	void testSpellerSpellsValidInputs(double input, String expected) {
		assertEquals(expected, speller.spellOut(input));
	}

	static Stream<Arguments> validInputs() {
		/* Explicit array allows trailing comma for cleaner diffs */
		return Stream.of(new Arguments[] {
				Arguments.of(Double.NaN, "not a number"),
				Arguments.of(Double.POSITIVE_INFINITY, "infinity"),
				Arguments.of(Double.NEGATIVE_INFINITY, "minus infinity"),
				Arguments.of(0., "zero"),
				Arguments.of(-0., "zero"),
				Arguments.of(1., "one"),
				Arguments.of(1.414213, "one point four one four two one three"),
				Arguments.of(2., "two"),
				Arguments.of(2.718281, "two point seven one eight two eight one"),
				Arguments.of(3., "three"),
				Arguments.of(3.1415926, "three point one four one five nine two six"),
				Arguments.of(4., "four"),
				Arguments.of(5., "five"),
				Arguments.of(6., "six"),
				Arguments.of(7., "seven"),
				Arguments.of(8., "eight"),
				Arguments.of(9., "nine"),
				Arguments.of(10., "ten"),
				Arguments.of(11., "eleven"),
				Arguments.of(12., "twelve"),
				Arguments.of(13., "thirteen"),
				Arguments.of(14., "fourteen"),
				Arguments.of(15., "fifteen"),
				Arguments.of(16., "sixteen"),
				Arguments.of(17., "seventeen"),
				Arguments.of(18., "eighteen"),
				Arguments.of(19., "nineteen"),
				Arguments.of(20., "twenty"),
				Arguments.of(21., "twenty-one"),
				Arguments.of(22., "twenty-two"),
				Arguments.of(23., "twenty-three"),
				Arguments.of(24., "twenty-four"),
				Arguments.of(25., "twenty-five"),
				Arguments.of(26., "twenty-six"),
				Arguments.of(27., "twenty-seven"),
				Arguments.of(28., "twenty-eight"),
				Arguments.of(29., "twenty-nine"),
				Arguments.of(30., "thirty"),
				Arguments.of(31., "thirty-one"),
				Arguments.of(32., "thirty-two"),
				Arguments.of(33., "thirty-three"),
				Arguments.of(34., "thirty-four"),
				Arguments.of(35., "thirty-five"),
				Arguments.of(36., "thirty-six"),
				Arguments.of(37., "thirty-seven"),
				Arguments.of(38., "thirty-eight"),
				Arguments.of(39., "thirty-nine"),
				Arguments.of(40., "forty"),
				Arguments.of(41., "forty-one"),
				Arguments.of(42., "forty-two"),
				Arguments.of(43., "forty-three"),
				Arguments.of(44., "forty-four"),
				Arguments.of(45., "forty-five"),
				Arguments.of(46., "forty-six"),
				Arguments.of(47., "forty-seven"),
				Arguments.of(48., "forty-eight"),
				Arguments.of(49., "forty-nine"),
				Arguments.of(50., "fifty"),
				Arguments.of(51., "fifty-one"),
				Arguments.of(52., "fifty-two"),
				Arguments.of(53., "fifty-three"),
				Arguments.of(54., "fifty-four"),
				Arguments.of(55., "fifty-five"),
				Arguments.of(56., "fifty-six"),
				Arguments.of(57., "fifty-seven"),
				Arguments.of(58., "fifty-eight"),
				Arguments.of(59., "fifty-nine"),
				Arguments.of(60., "sixty"),
				Arguments.of(61., "sixty-one"),
				Arguments.of(62., "sixty-two"),
				Arguments.of(63., "sixty-three"),
				Arguments.of(64., "sixty-four"),
				Arguments.of(65., "sixty-five"),
				Arguments.of(66., "sixty-six"),
				Arguments.of(67., "sixty-seven"),
				Arguments.of(68., "sixty-eight"),
				Arguments.of(69., "sixty-nine"),
				Arguments.of(70., "seventy"),
				Arguments.of(71., "seventy-one"),
				Arguments.of(72., "seventy-two"),
				Arguments.of(73., "seventy-three"),
				Arguments.of(74., "seventy-four"),
				Arguments.of(75., "seventy-five"),
				Arguments.of(76., "seventy-six"),
				Arguments.of(77., "seventy-seven"),
				Arguments.of(78., "seventy-eight"),
				Arguments.of(79., "seventy-nine"),
				Arguments.of(80., "eighty"),
				Arguments.of(81., "eighty-one"),
				Arguments.of(82., "eighty-two"),
				Arguments.of(83., "eighty-three"),
				Arguments.of(84., "eighty-four"),
				Arguments.of(85., "eighty-five"),
				Arguments.of(86., "eighty-six"),
				Arguments.of(87., "eighty-seven"),
				Arguments.of(88., "eighty-eight"),
				Arguments.of(89., "eighty-nine"),
				Arguments.of(90., "ninety"),
				Arguments.of(91., "ninety-one"),
				Arguments.of(92., "ninety-two"),
				Arguments.of(93., "ninety-three"),
				Arguments.of(94., "ninety-four"),
				Arguments.of(95., "ninety-five"),
				Arguments.of(96., "ninety-six"),
				Arguments.of(97., "ninety-seven"),
				Arguments.of(98., "ninety-eight"),
				Arguments.of(99., "ninety-nine"),
				Arguments.of(100., "one hundred"),
				Arguments.of(111., "one hundred eleven"),
				Arguments.of(123.45, "one hundred twenty-three point four five"),
				Arguments.of(200., "two hundred"),
				Arguments.of(300., "three hundred"),
				Arguments.of(321., "three hundred twenty-one"),
				Arguments.of(1000., "one thousand"),
				Arguments.of(1234., "one thousand two hundred thirty-three"),
				Arguments.of(10_000., "ten thousand"),
				Arguments.of(54_321., "fifty-four thousand three hundred twenty-one"),
		});
	}
}
