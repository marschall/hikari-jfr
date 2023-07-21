module com.github.marschall.hikari.jfr {

  requires jdk.jfr;
  
  requires java.sql;
  requires com.zaxxer.hikari;

  exports com.github.marschall.hikari.jfr;
  
}