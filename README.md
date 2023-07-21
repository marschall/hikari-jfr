Hikari JFR [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/hikari-jfr/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/hikari-jfr) [![Javadocs](https://www.javadoc.io/badge/com.github.marschall/hikari-jfr.svg)](https://www.javadoc.io/doc/com.github.marschall/hikari-jfr)
==========

A HikariCP MetricsTrackerFactory that generates JFR events.

![Flight Recording of a JUnit Test](https://github.com/marschall/hikari-jfr/src/main/javadoc/resources/hikari-jfr.png)

This project requires Java 11 based on OpenJDK or later and HikariCP 5+.

Usage
-----

```xml
<dependency>
  <groupId>com.github.marschall</groupId>
  <artifactId>hikari-jfr</artifactId>
  <version>1.0.0</version>
</dependency>
```

```java
HikariConfig hikariConfig = ...
hikariConfig.setMetricsTrackerFactory(new JfrMetricsTrackerFactory());
```