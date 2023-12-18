package com.piestimation.reactive.util;

import java.util.concurrent.ThreadLocalRandom;

import reactor.core.publisher.Flux;

/**
 * Utility class for generating random numbers within a specified range [Min, max).
 * 
 * NOTE: Just like any other estimation in statistics monte-carlo also has limitations Here double
 * is used instead of integer to generate random values for better precision rate. Using the integer
 * would compromise approximation for smaller intervals! Important thing to note here is that the
 * below range would not include max(inclusive) but given the random distribution for larger data
 * sets, it would approximate correctly. For the last bit to correction to reach closest to
 * max(inclusive) Double.MIN_VALUE is added.
 * 
 * @return Flux of random points as double arrays [x, y].
 */
public class RandomGenerator {
  public static Flux<double[]> generateRandomPoint(double min, double max) {
    double x = ThreadLocalRandom.current().nextDouble(min, max);
    double y = ThreadLocalRandom.current().nextDouble(min, max);
    return Flux.just(new double[] {x, y});
  }
}
