package ru.hh.nab.testbase.old.postgres.embedded;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import javax.sql.DataSource;
import org.apache.commons.text.StringSubstitutor;
import ru.hh.nab.common.files.FileSystemUtils;
import ru.hh.nab.common.properties.FileSettings;
import ru.hh.nab.datasource.DataSourceFactory;
import static ru.hh.nab.datasource.DataSourceSettings.JDBC_URL;
import static ru.hh.nab.datasource.DataSourceSettings.PASSWORD;
import static ru.hh.nab.datasource.DataSourceSettings.USER;
import ru.hh.nab.datasource.monitoring.NabMetricsTrackerFactoryProvider;

public class EmbeddedPostgresDataSourceFactory extends DataSourceFactory {
  public static final String DEFAULT_JDBC_URL = "jdbc:postgresql://${host}:${port}/${database}";
  public static final String DEFAULT_DATABASE = "postgres";
  public static final String DEFAULT_USER = "postgres";

  public EmbeddedPostgresDataSourceFactory() {
    super(null);
  }

  public EmbeddedPostgresDataSourceFactory(NabMetricsTrackerFactoryProvider nabMetricsTrackerFactoryProvider) {
    super(nabMetricsTrackerFactoryProvider);
  }

  @Override
  protected DataSource createDataSource(String dataSourceName, boolean isReadonly, FileSettings dataSourceSettings) {
    Properties properties = dataSourceSettings.getProperties();

    final StringSubstitutor jdbcUrlParamsSubstitutor = new StringSubstitutor(Map.of(
            "port", getEmbeddedPostgres().getPort(),
            "host", "localhost",
            "database", DEFAULT_DATABASE
    ));
    String jdbcUrl = jdbcUrlParamsSubstitutor.replace(Optional.ofNullable(dataSourceSettings.getString(JDBC_URL)).orElse(DEFAULT_JDBC_URL));
    properties.setProperty(JDBC_URL, jdbcUrl);
    properties.setProperty(USER, DEFAULT_USER);
    properties.setProperty(PASSWORD, "");

    return super.createDataSource(dataSourceName, isReadonly, new FileSettings(properties));
  }

  private static class EmbeddedPostgresSingleton {
    private static final UUID INSTANCE_ID = UUID.randomUUID();
    private static final EmbeddedPostgres INSTANCE = createEmbeddedPostgres();

    private static final String PG_DIR = "embedded-pg";
    private static final String PG_DIR_PROPERTY = "ot.epg.working-dir";

    private static EmbeddedPostgres createEmbeddedPostgres() {
      try {
        File dataDirectory = null;
        String embeddedPgDir = getEmbeddedPgDir();
        if (embeddedPgDir != null) {
          System.setProperty(PG_DIR_PROPERTY, embeddedPgDir);
          dataDirectory = new File(embeddedPgDir, INSTANCE_ID.toString());
        }
        return EmbeddedPostgres.builder()
          .setServerConfig("autovacuum", "off")
          .setLocaleConfig("lc-collate", "C")
          .setDataDirectory(dataDirectory)
          .start();
      } catch (IOException e) {
        throw new IllegalStateException("Can't start embedded Postgres", e);
      }
    }

    private static String getEmbeddedPgDir() throws IOException {
      Path tmpfsPath = FileSystemUtils.getTmpfsPath();
      if (tmpfsPath == null) {
        return null;
      }

      Path pgPath = Paths.get(tmpfsPath.toString(), PG_DIR);
      if (Files.notExists(pgPath)) {
        Files.createDirectory(pgPath);
      }
      return pgPath.toString();
    }
  }

  public static EmbeddedPostgres getEmbeddedPostgres() {
    return EmbeddedPostgresSingleton.INSTANCE;
  }
}
