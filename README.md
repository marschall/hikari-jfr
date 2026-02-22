Hikari JFR [![Build Status](https://github.com/marschall/hikari-jfr/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/marschall/hikari-jfr/actions?query=workflow%3Abuild+branch%3Amaster) [![Maven Central](https://img.shields.io/maven-central/v/com.github.marschall/hikari-jfr?color=31c653&label=maven%20central)](https://central.sonatype.com/artifact/com.github.marschall/hikari-jfr) [![Javadocs](https://www.javadoc.io/badge/com.github.marschall/hikari-jfr.svg)](https://www.javadoc.io/doc/com.github.marschall/hikari-jfr)
==========

A HikariCP MetricsTrackerFactory that generates JFR events.

![Flight Recording of a JUnit Test](https://github.com/marschall/hikari-jfr/blob/master/src/main/javadoc/resources/hikari-jfr.png?raw=true)

This project requires Java 11 based on OpenJDK or later and HikariCP 5+.

Usage
-----

```xml
<dependency>
  <groupId>com.github.marschall</groupId>
  <artifactId>hikari-jfr</artifactId>
  <version>1.0.2</version>
</dependency>
```

```java
HikariConfig hikariConfig = ...
hikariConfig.setMetricsTrackerFactory(new JfrMetricsTrackerFactory());
```

Caveats
-------

The duration of the events is always 0, instead the duration is stored in an additonal column.

Events
------

The following events are supported


<dl>

<dt>Connection Created</dt>
<dd>A physical connection was created. It has the following attributes
<dl>
<dt>Pool Name</dt>
<dd>The name of the connection pool</dd>
<dt>Creation Time</dt>
<dd>The time it took to create a physical connection</dd>
</dl>

<dt>Connection Acquired</dt>
<dd>A connection was acquired from the pool. It has the following attributes
<dl>
<dt>Pool Name</dt>
<dd>The name of the connection pool</dd>
<dt>Acquisition Time</dt>
<dd>The time it took to acquire a connection from the pool</dd>
</dl>

<dt>Connection Borrowed</dt>
<dd>A connection was borrowed from the pool. It has the following attributes
<dl>
<dt>Pool Name</dt>
<dd>The name of the connection pool</dd>
<dt>Borrowed Time</dt>
<dd>The time the connection was borrowed from the pool</dd>
</dl>

<dt>Connection Timeout</dt>
<dd>A could not be acquired in the request time from the pool. It has the following attributes
<dl>
<dt>Pool Name</dt>
<dd>The name of the connection pool</dd>
</dl>

<dt>Pool Stats</dt>
<dd>Statistics about a pool. It has the following attributes
<dl>
<dt>Pool Name</dt>
<dd>The name of the pool the statistics are about</dd>
<dt>Total Connections</dt>
<dd>The total number of connections in the pool</dd>
<dt>Idle Connections</dt>
<dd>The number of idle connections in the pool</dd>
<dt>Active Connections</dt>
<dd>The number of active connections in the pool</dd>
<dt>Pending Threads</dt>
<dd>The number of pending threads</dd>
<dt>Max Connections</dt>
<dd>The maximum number of connections in the pool</dd>
<dt>Min Connections</dt>
<dd>The minimum number of connections in the pool</dd>
</dl>

</dl>


Command Line
------------

```sh
$jfr print --events "com.github.marschall.hikari.jfr.*" target/unit-tests.jfr

com.github.marschall.hikari.jfr.JFRMetricsTracker$ConnectionBorrowedEvent {
  startTime = 09:37:47.114 (2026-02-06)
  poolName = "HikariPool-1"
  borrowedTime = 23.0 ms
  eventThread = "main" (javaThreadId = 3)
  stackTrace = [
    com.github.marschall.hikari.jfr.JFRMetricsTracker.recordConnectionUsageMillis(long) line: 131
    com.zaxxer.hikari.pool.PoolBase$MetricsTrackerDelegate.recordConnectionUsage(PoolEntry) line: 753
    com.zaxxer.hikari.pool.HikariPool.recycle(PoolEntry) line: 436
    com.zaxxer.hikari.pool.PoolEntry.recycle() line: 81
    com.zaxxer.hikari.pool.ProxyConnection.close() line: 268
    ...
  ]
}

com.github.marschall.hikari.jfr.JFRMetricsTracker$PoolStatsEvent {
  startTime = 09:37:47.114 (2026-02-06)
  poolName = "HikariPool-1"
  totalConnections = 1
  idleConnections = 0
  activeConnections = 1
  pendingThreads = 0
  maxConnections = 2
  minConnections = 2
  eventThread = "main" (javaThreadId = 3)
}
````
