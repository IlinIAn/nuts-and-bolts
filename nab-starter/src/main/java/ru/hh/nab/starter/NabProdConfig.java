package ru.hh.nab.starter;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.eclipse.jetty.servlet.FilterHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import ru.hh.metrics.StatsDSender;
import ru.hh.nab.common.properties.FileSettings;
import static ru.hh.nab.common.properties.PropertiesUtils.fromFilesInSettingsDir;
import ru.hh.nab.starter.jmx.MBeanExporterFactory;
import static ru.hh.nab.starter.server.cache.HttpCacheFilterFactory.createCacheFilterHolder;

import javax.management.MBeanServer;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@Import({NabCommonConfig.class})
public class NabProdConfig {

  @Bean
  FileSettings fileSettings() throws Exception {
    Properties properties = fromFilesInSettingsDir("service.properties", "service.properties.dev");
    return new FileSettings(properties);
  }

  @Bean
  StatsDClient statsDClient() {
    return new NonBlockingStatsDClient(null, "localhost", 8125, 10000);
  }

  @Bean
  StatsDSender statsDSender(ScheduledExecutorService scheduledExecutorService, StatsDClient statsDClient) {
    return new StatsDSender(statsDClient, scheduledExecutorService);
  }

  @Bean
  FilterHolder cacheFilter(FileSettings fileSettings,
                           String serviceName,
                           StatsDClient statsDClient,
                           ScheduledExecutorService scheduledExecutorService) {
    return createCacheFilterHolder(fileSettings, serviceName, statsDClient, scheduledExecutorService);
  }

  @Bean
  MBeanServerFactoryBean mBeanServerFactoryBean() {
    MBeanServerFactoryBean mBeanServerFactoryBean = new MBeanServerFactoryBean();
    mBeanServerFactoryBean.setLocateExistingServerIfPossible(true);
    return mBeanServerFactoryBean;
  }

  @Bean
  MBeanExporter mBeanExporter(FileSettings settings, MBeanServer mbeanServer) {
    return MBeanExporterFactory.create(settings, mbeanServer);
  }
}
