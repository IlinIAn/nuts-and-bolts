package ru.hh.nab.example.dao;

import java.io.Serializable;
import javax.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

public class GenericDao {

  private final SessionFactory sessionFactory;

  @Inject
  public GenericDao(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Transactional
  public <T> T get(Class<T> clazz, Serializable id) {
    return getSession().get(clazz, id);
  }

  @Transactional
  public void save(Object object) {
    if (object != null) {
      getSession().save(object);
    }
  }

  @Transactional
  public void update(Object object) {
    if (object != null) {
      getSession().update(object);
    }
  }

  @Transactional
  public void delete(Object object) {
    if (object != null) {
      getSession().delete(object);
    }
  }

  private Session getSession() {
    return sessionFactory.getCurrentSession();
  }
}
