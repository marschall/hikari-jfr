package com.github.marschall.hikari.jfr;

import static jdk.jfr.Timespan.MILLISECONDS;
import static jdk.jfr.Timespan.NANOSECONDS;

import com.zaxxer.hikari.metrics.IMetricsTracker;
import com.zaxxer.hikari.metrics.PoolStats;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.StackTrace;
import jdk.jfr.Timespan;

/**
 * A {@link IMetricsTracker} that generates JFR events.
 * 
 * <h2>Events</h2>
 * The following events are supported
 * 
 * <dl>
 * 
 * <dt>Connection Created</dt>
 * <dd>A physical connection was created. It has the following attributes
 * <dl>
 * <dt>Pool Name</dt>
 * <dd>The name of the connection pool</dd>
 * <dt>Creation Time</dt>
 * <dd>The time it took to create a physical connection</dd>
 * </dl>
 * </dd>
 * 
 * <dt>Connection Acquired</dt>
 * <dd>A connection was acquired from the pool. It has the following attributes
 * <dl>
 * <dt>Pool Name</dt>
 * <dd>The name of the connection pool</dd>
 * <dt>Acquisition Time</dt>
 * <dd>The time it took to acquire a connection from the pool</dd>
 * </dl>
 * </dd>
 * 
 * <dt>Connection Borrowed</dt>
 * <dd>A connection was borrowed from the pool. It has the following attributes
 * <dl>
 * <dt>Pool Name</dt>
 * <dd>The name of the connection pool</dd>
 * <dt>Borrowed Time</dt>
 * <dd>The time the connection was borrowed from the pool</dd>
 * </dl>
 * </dd>
 * 
 * <dt>Connection Timeout</dt>
 * <dd>A could not be acquired in the request time from the pool. It has the following attributes
 * <dl>
 * <dt>Pool Name</dt>
 * <dd>The name of the connection pool</dd>
 * </dl>
 * </dd>
 * 
 * </dl>
 */
final class JFRMetricsTracker implements IMetricsTracker {

  private static final String CATEGORY = "HikariCP";

  private final String poolName;

  private final PoolStats poolStats;

  JFRMetricsTracker(String poolName, PoolStats poolStats) {
    this.poolName = poolName;
    this.poolStats = poolStats;
  }

  private void recordPoolStats() {
    PoolStatsEvent event = new PoolStatsEvent();
    event.setPoolName(this.poolName);
    event.setActiveConnections(this.poolStats.getActiveConnections());
    event.setIdleConnections(this.poolStats.getIdleConnections());
    event.setMaxConnections(this.poolStats.getMaxConnections());
    event.setMinConnections(this.poolStats.getMinConnections());
    event.setPendingThreads(this.poolStats.getPendingThreads());
    event.setTotalConnections(this.poolStats.getTotalConnections());
    event.commit();
  }

  @Override
  public void recordConnectionCreatedMillis(long connectionCreatedMillis) {
    ConnectionCreatedEvent event = new ConnectionCreatedEvent();
    event.setPoolName(this.poolName);
    event.setCreationTime(connectionCreatedMillis);
    event.commit();

    this.recordPoolStats();
  }

  @Override
  public void recordConnectionAcquiredNanos(long elapsedAcquiredNanos) {
    ConnectionAcquiredEvent event = new ConnectionAcquiredEvent();
    event.setPoolName(this.poolName);
    event.setAcquisitionTime(elapsedAcquiredNanos);
    event.commit();

    this.recordPoolStats();
  }

  @Override
  public void recordConnectionUsageMillis(long elapsedBorrowedMillis) {
    ConnectionBorrowedEvent event = new ConnectionBorrowedEvent();
    event.setPoolName(this.poolName);
    event.setBorrowedTime(elapsedBorrowedMillis);
    event.commit();

    this.recordPoolStats();
  }

  @Override
  public void recordConnectionTimeout() {
    ConnectionTimeoutEvent event = new ConnectionTimeoutEvent();
    event.setPoolName(this.poolName);
    event.commit();

    this.recordPoolStats();
  }

  @Label("Connection Created")
  @Description("A physical connection was created")
  @Category(CATEGORY)
  static final class ConnectionCreatedEvent extends Event {

    @Label("Pool Name")
    @Description("The name of the connection pool")
    private String poolName;

    @Label("Creation Time")
    @Description("The time it took to create a physical connection")
    @Timespan(MILLISECONDS)
    private long creationTime;

    void setPoolName(String poolName) {
      this.poolName = poolName;
    }

    void setCreationTime(long creationTime) {
      this.creationTime = creationTime;
    }

  }

  @Label("Connection Acquired")
  @Description("A connection was acquired from the pool")
  @Category(CATEGORY)
  static final class ConnectionAcquiredEvent extends Event {

    @Label("Pool Name")
    @Description("The name of the pool the connection was acquired from")
    private String poolName;

    @Label("Acquisition Time")
    @Description("The time it took to acquire a connection from the pool")
    @Timespan(NANOSECONDS)
    private long acquisitionTime;

    void setPoolName(String poolName) {
      this.poolName = poolName;
    }

    void setAcquisitionTime(long creationTime) {
      this.acquisitionTime = creationTime;
    }

  }

  @Label("Connection Borrowed")
  @Description("A connection was borrowed from the pool")
  @Category(CATEGORY)
  static final class ConnectionBorrowedEvent extends Event {

    @Label("Pool Name")
    @Description("The name of the pool the connection was borrowed from")
    private String poolName;

    @Label("Borrowed Time")
    @Description("The time the connection was borrowed from the pool")
    @Timespan(MILLISECONDS)
    private long borrowedTime;

    void setPoolName(String poolName) {
      this.poolName = poolName;
    }

    void setBorrowedTime(long creationTime) {
      this.borrowedTime = creationTime;
    }

  }

  @Label("Connection Timeout")
  @Description("A could not be acquired in the request time from the pool")
  @Category(CATEGORY)
  static final class ConnectionTimeoutEvent extends Event {

    @Label("Pool Name")
    @Description("The name of the pool the connection was requested from")
    private String poolName;

    void setPoolName(String poolName) {
      this.poolName = poolName;
    }

  }

  @Label("Pool Stats")
  @Description("Statistics about a pool")
  @Category(CATEGORY)
  @StackTrace(false)
  static final class PoolStatsEvent extends Event {

    @Label("Pool Name")
    @Description("The name of the pool the stats are about")
    private String poolName;

    @Label("Total Connections")
    @Description("The total number of connections in the pool")
    private int totalConnections;

    @Label("Idle Connections")
    @Description("The number of idle connections in the pool")
    private int idleConnections;

    @Label("Active Connections")
    @Description("The number of active connections in the pool")
    private int activeConnections;

    @Label("Pending Threads")
    @Description("The number of pending threads")
    private int pendingThreads;

    @Label("Max Connections")
    @Description("The maximum number of connections in the pool")
    private int maxConnections;

    @Label("Min Connections")
    @Description("The minimum number of connections in the pool")
    private int minConnections;

    void setPoolName(String poolName) {
      this.poolName = poolName;
    }

    void setTotalConnections(int totalConnections) {
      this.totalConnections = totalConnections;
    }

    void setIdleConnections(int idleConnections) {
      this.idleConnections = idleConnections;
    }

    void setActiveConnections(int activeConnections) {
      this.activeConnections = activeConnections;
    }

    void setPendingThreads(int pendingThreads) {
      this.pendingThreads = pendingThreads;
    }

    void setMaxConnections(int maxConnections) {
      this.maxConnections = maxConnections;
    }

    void setMinConnections(int minConnections) {
      this.minConnections = minConnections;
    }

  }

}
