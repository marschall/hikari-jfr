package com.github.marschall.hikari.jfr;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

class JfrMetricsTrackerFactoryTests {

  private HikariDataSource dataSource;

  @BeforeEach
  void setUp() {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setMetricsTrackerFactory(new JfrMetricsTrackerFactory());
    hikariConfig.setMaximumPoolSize(2);
    hikariConfig.setJdbcUrl("jdbc:h2:mem:test_mem");
    hikariConfig.setConnectionTimeout(TimeUnit.SECONDS.toMillis(2L));
    dataSource = new HikariDataSource(hikariConfig);
  }

  @AfterEach
  void afterEach() {
    this.dataSource.close();
  }

  @Test
  void generateEvents() throws SQLException {
    query();
  }
  
  void query() throws SQLException {
    try (Connection connection = this.dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("SELECT x FROM SYSTEM_RANGE(1, ?)")) {
      preparedStatement.setInt(1, 100);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          int rowNum = resultSet.getInt(1);
          if (rowNum % 10 == 0) {
            innerQuery(rowNum);
          }
        }
      }
    }
  }

  private void innerQuery(int i) throws SQLException {
    try (Connection connection = this.dataSource.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("SELECT ?")) {
      preparedStatement.setInt(1, i);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          assertEquals(i, resultSet.getInt(1));
        }
      }
    }
  }

}
