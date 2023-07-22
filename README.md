Hikari JFR [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/hikari-jfr/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/hikari-jfr) [![Javadocs](https://www.javadoc.io/badge/com.github.marschall/hikari-jfr.svg)](https://www.javadoc.io/doc/com.github.marschall/hikari-jfr)
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
  <version>1.0.1</version>
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
</dd>

<dt>Connection Acquired</dt>
<dd>A connection was acquired from the pool. It has the following attributes
<dl>
<dt>Pool Name</dt>
<dd>The name of the connection pool</dd>
<dt>Acquisition Time</dt>
<dd>The time it took to acquire a connection from the pool</dd>
</dl>
</dd>

<dt>Connection Borrowed</dt>
<dd>A connection was borrowed from the pool. It has the following attributes
<dl>
<dt>Pool Name</dt>
<dd>The name of the connection pool</dd>
<dt>Borrowed Time</dt>
<dd>The time the connection was borrowed from the pool</dd>
</dl>
</dd>

<dt>Connection Timeout</dt>
<dd>A could not be acquired in the request time from the pool. It has the following attributes
<dl>
<dt>Pool Name</dt>
<dd>The name of the connection pool</dd>
</dl>
</dd>

</dl>

