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


	public static void main(String[] args) throws RunnerException {
		Options opts = new OptionsBuilder()
				.include(UsEnglishNumericTextParserBenchmark.class.getSimpleName())
				.build();
		new Runner(opts).run();
	}
}
