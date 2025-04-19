package io.github.moonstroke.spelledoutnumbers.benchmarks;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import io.github.moonstroke.spelledoutnumbers.NumericTextParser;


@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(1)
public class UsEnglishNumericTextParserBenchmark {
	final NumericTextParser parser = NumericTextParser.getTextParserFor(Locale.US);


	@Benchmark
	public void benchmarkDigit(Blackhole bh) {
		bh.consume(parser.parse("seven"));
	}

	@Benchmark
	public void benchmarkTeen(Blackhole bh) {
		bh.consume(parser.parse("sixteen"));
	}

	@Benchmark
	public void benchmarkTen(Blackhole bh) {
		bh.consume(parser.parse("fifty"));
	}

	@Benchmark
	public void benchmarkCompoundNumber(Blackhole bh) {
		bh.consume(parser.parse("forty-two"));
	}

	@Benchmark
	public void benchmarkLongMaxValue(Blackhole bh) {
		bh.consume(parser.parse("nine quintillion two hundred twenty-three quadrillion three hundred seventy-two trillion thirty-six billion eight hundred fifty-four million seven hundred seventy-four thousand seven hundred eighty-four"));
	}

	@Benchmark
	public void benchmarkDoubleMaxValue(Blackhole bh) {
		bh.consume(parser.parse("one hundred seventy-nine uncentillion seven hundred sixty-nine centillion three hundred thirteen novenonagintillion four hundred eighty-six octononagintillion two hundred thirty-one septenonagintillion five hundred seventy senonagintillion eight hundred fourteen quinnonagintillion five hundred twenty-seven quattuornonagintillion four hundred twenty-three trenonagintillion seven hundred thirty-one duononagintillion seven hundred four unnonagintillion three hundred fifty-six nonagintillion seven hundred ninety-eight novemoctogintillion seventy octooctogintillion five hundred sixty-seven septemoctogintillion five hundred twenty-five sexoctogintillion eight hundred forty-four quinoctogintillion nine hundred ninety-six quattuoroctogintillion five hundred ninety-eight tresoctogintillion nine hundred seventeen duooctogintillion four hundred seventy-six unoctogintillion eight hundred three octogintillion one hundred fifty-seven novenseptuagintillion two hundred sixty octoseptuagintillion seven hundred eighty septenseptuagintillion twenty-eight seseptuagintillion five hundred thirty-eight quinseptuagintillion seven hundred sixty quattuorseptuagintillion five hundred eighty-nine treseptuagintillion five hundred fifty-eight duoseptuagintillion six hundred thirty-two unseptuagintillion seven hundred sixty-six septuagintillion eight hundred seventy-eight novensexagintillion one hundred seventy-one octosexagintillion five hundred forty septensexagintillion four hundred fifty-eight sesexagintillion nine hundred fifty-three quinsexagintillion five hundred fourteen quattuorsexagintillion three hundred eighty-two tresexagintillion four hundred sixty-four duosexagintillion two hundred thirty-four unsexagintillion three hundred twenty-one sexagintillion three hundred twenty-six novenquinquagintillion eight hundred eighty-nine octoquinquagintillion four hundred sixty-four septenquinquagintillion one hundred eighty-two sesquinquagintillion seven hundred sixty-eight quinquinquagintillion four hundred sixty-seven quattuorquinquagintillion five hundred forty-six tresquinquagintillion seven hundred three duoquinquagintillion five hundred thirty-seven unquinquagintillion five hundred sixteen quinquagintillion nine hundred eighty-six novenquadragintillion forty-nine octoquadragintillion nine hundred ten septenquadragintillion five hundred seventy-six sesquadragintillion five hundred fifty-one quinquadragintillion two hundred eighty-two quattuorquadragintillion seventy-six tresquadragintillion two hundred forty-five duoquadragintillion four hundred ninety unquadragintillion ninety quadragintillion three hundred eighty-nine noventrigintillion three hundred twenty-eight octotrigintillion nine hundred forty-four septentrigintillion seventy-five sestrigintillion eight hundred sixty-eight quintrigintillion five hundred eight quattuortrigintillion four hundred fifty-five trestrigintillion one hundred thirty-three duotrigintillion nine hundred forty-two untrigintillion three hundred four trigintillion five hundred eighty-three novemvigintillion two hundred thirty-six octovigintillion nine hundred three septemvigintillion two hundred twenty-two sesvigintillion nine hundred forty-eight quinvigintillion one hundred sixty-five quattuorvigintillion eight hundred eight tresvigintillion five hundred fifty-nine duovigintillion three hundred thirty-two unvigintillion one hundred twenty-three vigintillion three hundred forty-eight novendecillion two hundred seventy-four octodecillion seven hundred ninety-seven septendecillion eight hundred twenty-six sedecillion two hundred four quindecillion one hundred forty-four quattuordecillion seven hundred twenty-three tredecillion one hundred sixty-eight duodecillion seven hundred thirty-eight undecillion one hundred seventy-seven decillion one hundred eighty nonillion nine hundred nineteen octillion two hundred ninety-nine septillion eight hundred eighty-one sextillion two hundred fifty quintillion four hundred four quadrillion twenty-six trillion one hundred eighty-four billion one hundred twenty-four million eight hundred fifty-eight thousand three hundred sixty-eight"));
	}

	@Benchmark
	public void benchmarkDoubleMinValue(Blackhole bh) {
		bh.consume(parser.parse("zero point zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero zero four nine"));
	}


	public static void main(String[] args) throws RunnerException {
		Options opts = new OptionsBuilder()
				.include(UsEnglishNumericTextParserBenchmark.class.getSimpleName())
				.build();
		new Runner(opts).run();
	}
}
