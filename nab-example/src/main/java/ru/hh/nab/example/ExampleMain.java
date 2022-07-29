package ru.hh.nab.example;

import java.util.Set;
import ru.hh.nab.common.properties.PropertiesUtils;
import ru.hh.nab.example.config.ExampleConfig;
import ru.hh.nab.starter.NabApplication;
import ru.hh.nab.websocket.NabWebsocketConfigurator;

public class ExampleMain {

  public static void main(String[] args) {
    // specify settings dir if its not currentDir
    System.setProperty(PropertiesUtils.SETINGS_DIR_PROPERTY, "nab-example/src/etc");

    NabApplication.builder()
      .configureJersey(ExampleJerseyConfig.class).addAllowedPackages("ru.hh").bindToRoot()
      .apply(builder -> NabWebsocketConfigurator.configureWebsocket(builder, Set.of("ru.hh")))
      .build().run(ExampleConfig.class);
  }
}
