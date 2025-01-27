package io.github.moonstroke.spelledoutnumbers.benchmarks;

import java.util.Locale;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import io.github.moonstroke.spelledoutnumbers.NumberSpeller;


@State(Scope.Thread)
public class UsEnglishNumberSpellerBenchmark {
	final NumberSpeller speller = NumberSpeller.getNumberSpellerFor(Locale.US);


	@Benchmark
	public void benchmarkLongMaxValue(Blackhole bh) {
		bh.consume(speller.spellOut(9223372036854774784.));
	}

	@Benchmark
	public void benchmarkValueOutOfLongRange(Blackhole bh) {
		bh.consume(speller.spellOut(0x2p63));
	}

	@Benchmark
	public void benchmarkDoubleMaxValue(Blackhole bh) {
		bh.consume(speller.spellOut(Double.MAX_VALUE));
	}
}
