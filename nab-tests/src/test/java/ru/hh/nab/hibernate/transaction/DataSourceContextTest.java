package ru.hh.nab.hibernate.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.support.TransactionTemplate;
import ru.hh.nab.hibernate.HibernateTestConfig;
import ru.hh.nab.testbase.hibernate.HibernateTestBase;

import static ru.hh.nab.datasource.DataSourceType.MASTER;
import static ru.hh.nab.datasource.DataSourceType.READONLY;
import static ru.hh.nab.datasource.DataSourceType.SLOW;
import static ru.hh.nab.hibernate.transaction.DataSourceContextUnsafe.getDataSourceType;
import static ru.hh.nab.hibernate.transaction.DataSourceContext.onReplica;
import static ru.hh.nab.hibernate.transaction.DataSourceContext.onSlowReplica;

@ContextConfiguration(classes = {HibernateTestConfig.class})
public class DataSourceContextTest extends HibernateTestBase {

  @Before
  public void setUp() {
    DataSourceContextUnsafe.setDefaultMDC();
  }

  @Test
  public void testOnReplica() {
    assertIsCurrentDataSourceMaster();

    onReplica(() -> {
      assertEquals(READONLY.getName(), getDataSourceType());
      assertEquals(READONLY.getName(), MDC.get(DataSourceContextUnsafe.MDC_KEY));
      return null;
    });

    assertIsCurrentDataSourceMaster();
  }

  @Test
  public void testOnSlowReplica() {
    assertIsCurrentDataSourceMaster();

    onSlowReplica(() -> {
      assertEquals(SLOW.getName(), getDataSourceType());
      assertEquals(SLOW.getName(), MDC.get(DataSourceContextUnsafe.MDC_KEY));
      return null;
    });

    assertIsCurrentDataSourceMaster();
  }

  private static void assertIsCurrentDataSourceMaster() {
    assertNull(getDataSourceType());
    assertEquals(MASTER.getName(), MDC.get(DataSourceContextUnsafe.MDC_KEY));
  }

  @Test(expected = IllegalStateException.class)
  public void testOnReplicaInTransaction() {
    TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
    transactionTemplate.execute(transactionStatus -> onReplica(() -> null));
  }

  @Test(expected = IllegalStateException.class)
  public void testOnSlowReplicaInTransaction() {
    TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
    transactionTemplate.execute(transactionStatus -> onSlowReplica(() -> null));
  }
}
