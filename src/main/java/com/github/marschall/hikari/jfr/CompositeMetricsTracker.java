package com.github.marschall.hikari.jfr;

import java.util.Objects;

import com.zaxxer.hikari.metrics.IMetricsTracker;

final class CompositeMetricsTracker implements IMetricsTracker {

  private final IMetricsTracker first;
  private final IMetricsTracker second;

  CompositeMetricsTracker(IMetricsTracker first, IMetricsTracker second) {
    this.first = Objects.requireNonNull(first);
    this.second = Objects.requireNonNull(first);
  }

  public void recordConnectionCreatedMillis(long connectionCreatedMillis) {
    this.first.recordConnectionCreatedMillis(connectionCreatedMillis);
    this.second.recordConnectionCreatedMillis(connectionCreatedMillis);
  }

  public void recordConnectionAcquiredNanos(long elapsedAcquiredNanos) {
    this.first.recordConnectionAcquiredNanos(elapsedAcquiredNanos);
    this.second.recordConnectionAcquiredNanos(elapsedAcquiredNanos);
  }

  public void recordConnectionUsageMillis(long elapsedBorrowedMillis) {
    this.first.recordConnectionUsageMillis(elapsedBorrowedMillis);
    this.second.recordConnectionUsageMillis(elapsedBorrowedMillis);
  }

  public void recordConnectionTimeout() {
    this.first.recordConnectionTimeout();
    this.second.recordConnectionTimeout();
  }

  public void close() {
    this.first.close();
    this.second.close();
  }

}
