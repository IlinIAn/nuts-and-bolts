package ru.hh.nab.example.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.hh.nab.common.properties.FileSettings;
import ru.hh.nab.datasource.DataSourceFactory;
import ru.hh.nab.datasource.DataSourceType;
import ru.hh.nab.example.ExampleWebsocketConfig;
import ru.hh.nab.example.dao.GenericDao;
import ru.hh.nab.example.resource.CarResource;
import ru.hh.nab.example.resource.UserResource;
import ru.hh.nab.example.service.CarService;
import ru.hh.nab.example.service.UserService;
import ru.hh.nab.hibernate.MappingConfig;
import ru.hh.nab.hibernate.NabHibernateProdConfig;
import ru.hh.nab.hibernate.datasource.RoutingDataSource;
import ru.hh.nab.starter.NabProdConfig;

@Configuration
@Import({
    NabProdConfig.class,
    ExampleWebsocketConfig.class,
    NabHibernateProdConfig.class,

    CarService.class,
    UserService.class,
    CarResource.class,
    UserResource.class,
    GenericDao.class
})
public class ExampleConfig {

  @Bean
  DataSource dataSource(DataSourceFactory dataSourceFactory, FileSettings fileSettings) {
    DataSource masterDataSource = dataSourceFactory.create(DataSourceType.MASTER, false, fileSettings);
    RoutingDataSource routingDataSource = new RoutingDataSource(masterDataSource);

    return routingDataSource;
  }

  @Bean
  MappingConfig mappingConfig() {
    MappingConfig mappingConfig = new MappingConfig();
    mappingConfig.addPackagesToScan("ru.hh.nab.example.entity");
    return mappingConfig;
  }
}
