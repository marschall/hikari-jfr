package com.github.marschall.hikari.jfr;

import com.zaxxer.hikari.metrics.IMetricsTracker;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.PoolStats;

/**
 * A {@link MetricsTrackerFactory} that creates a {@link IMetricsTracker} that
 * generates JFR events.
 */
public final class JfrMetricsTrackerFactory implements MetricsTrackerFactory {

  @Override
  public IMetricsTracker create(String poolName, PoolStats poolStats) {
    return new JFRMetricsTracker(poolName, poolStats);
  }

}
