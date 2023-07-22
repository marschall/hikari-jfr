module com.github.marschall.hikari.jfr {

  requires jdk.jfr;

  requires transitive com.zaxxer.hikari;

  exports com.github.marschall.hikari.jfr;

}