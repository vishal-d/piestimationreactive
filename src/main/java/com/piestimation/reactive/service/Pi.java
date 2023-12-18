package com.piestimation.reactive.service;

import static com.piestimation.reactive.util.RandomGenerator.generateRandomPoint;

import org.springframework.stereotype.Service;

import com.piestimation.reactive.dto.PiEstimationRequest;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;;

/**
 * Calculates the approximate value of Pi using Monte Carlo Method. Makes use of RandomGenerator to
 * generate random points in the specified range. Increments the counter if the point is inside the
 * circle which is then used to calculate estimated value of Pi
 */
@Service
@Slf4j
public class Pi {

  /**
   * Estimates Pi using the Monte Carlo method.
   *
   * @param request The PiEstimationRequest containing the radius and totalPoints for the
   *        estimation.
   * @return A Flux<Double> representing the estimation results.
   * @throws IllegalArgumentException If the provided totalPoints or radius is invalid.
   */
  public Flux<Double> estimate(final PiEstimationRequest request) throws IllegalArgumentException {
    final int totalPoints = request.getTotalPoints();
    final double radius = request.getRadius();

    // Validate input parameters
    validateInputParameters(totalPoints, radius);

    // Perform the Pi estimation
    return Flux.range(0, totalPoints).parallel().runOn(Schedulers.parallel())
        .flatMap(i -> generateRandomPoint(-radius, radius)) // Generate random points asynchronously
        .filter(point -> isInsideCircle(point, radius)) // Filter points inside the circle
        .sequential().count() // Count the points inside the circle
        .doOnNext(count -> log.info("Total points inside circle: {}", count))
        .map(count -> 4.0 * count / totalPoints) // Map to calculate the estimated value of Pi
        .doOnNext(estimatedPi -> log.info("Estimated value of Pi: {}", estimatedPi)).flux();
  }

  /**
   * Checks whether a given point is inside the circle of the specified radius.
   *
   * @param point The coordinates of the point as an array [x, y].
   * @param radius The radius of the circle.
   * @return True if the point is inside the circle, false otherwise.
   */
  private boolean isInsideCircle(double[] point, double radius) {
    double x = point[0];
    double y = point[1];
    return Math.hypot(x, y) <= radius;
  }

  /**
   * Validates the input parameters for Pi estimation.
   *
   * @param totalPoints The total number of points for the estimation.
   * @param radius The radius of the circle.
   * @throws IllegalArgumentException If the provided totalPoints or radius is invalid.
   */
  private void validateInputParameters(int totalPoints, double radius) {
    if (totalPoints <= 0) {
      log.warn("Invalid totalPoints: {}. Pi estimation may not be accurate.", totalPoints);
      throw new IllegalArgumentException("Invalid totalPoints");
    }

    if (radius <= 0) {
      log.warn("Invalid radius: {}. Pi estimation may not be accurate.", radius);
      throw new IllegalArgumentException("Invalid radius");
    }
  }
}

