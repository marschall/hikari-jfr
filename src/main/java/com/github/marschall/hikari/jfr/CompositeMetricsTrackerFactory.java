package com.github.marschall.hikari.jfr;

import java.util.Objects;

import com.zaxxer.hikari.metrics.IMetricsTracker;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.PoolStats;

/**
 * Adds support for running two metrics tracker factories at the same time.
 */
public final class CompositeMetricsTrackerFactory implements MetricsTrackerFactory {

  private final MetricsTrackerFactory first;
  private final MetricsTrackerFactory second;

  /**
   * Constructs a new {@link CompositeMetricsTrackerFactory} out of two other
   * {@link MetricsTrackerFactory}.
   * 
   * @param first the first {@link MetricsTrackerFactory}, not {@code null}
   * @param second the second {@link MetricsTrackerFactory}, not {@code null}
   */
  public CompositeMetricsTrackerFactory(MetricsTrackerFactory first, MetricsTrackerFactory second) {
    this.first = Objects.requireNonNull(first);
    this.second = Objects.requireNonNull(first);
  }

  @Override
  public IMetricsTracker create(String poolName, PoolStats poolStats) {
    var firstTracker = this.first.create(poolName, poolStats);
    var secondTracker = this.second.create(poolName, poolStats);
    return new CompositeMetricsTracker(firstTracker, secondTracker);
  }

}
