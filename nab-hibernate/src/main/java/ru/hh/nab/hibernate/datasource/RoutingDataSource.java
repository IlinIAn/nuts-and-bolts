package ru.hh.nab.hibernate.datasource;

import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;
import ru.hh.nab.hibernate.transaction.DataSourceContextUnsafe;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class RoutingDataSource extends AbstractRoutingDataSource {

  private final Map<DataSourceType, DataSource> replicas = new HashMap<>();
  private DataSourceProxyFactory proxyFactory;

  public RoutingDataSource(DataSource defaultDataSource) {
    setDefaultTargetDataSource(new LazyConnectionDataSourceProxy(defaultDataSource));
  }

  @Override
  protected Object determineCurrentLookupKey() {
    return DataSourceContextUnsafe.getDataSourceType();
  }

  @Nullable
  @Override
  protected DataSource determineTargetDataSource() {
    DataSource original = super.determineTargetDataSource();
    return proxyFactory != null ? proxyFactory.createProxy(original) : original;
  }

  @Override
  public void afterPropertiesSet() {
    setTargetDataSources(new HashMap<>(replicas));
    super.afterPropertiesSet();
  }

  public void addDataSource(DataSourceType dataSourceType, DataSource dataSource) {
    replicas.put(dataSourceType, dataSource);
  }

  public void setProxyFactory(DataSourceProxyFactory proxyFactory) {
    this.proxyFactory = proxyFactory;
  }
}
