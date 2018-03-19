package ru.hh.nab.testbase;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import ru.hh.nab.core.CoreCommonConfig;

@ContextConfiguration(classes = {CoreCommonConfig.class})
public abstract class CoreTestBase extends AbstractJUnit4SpringContextTests {

  protected <T> T getBean(Class<T> beanType) {
    return applicationContext.getBean(beanType);
  }

  protected <T> T getBean(Class<T> beanType, String beanId) {
    return applicationContext.getBean(beanId, beanType);
  }
}