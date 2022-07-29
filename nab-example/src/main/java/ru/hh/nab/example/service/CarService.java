package ru.hh.nab.example.service;

import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hh.nab.example.dao.GenericDao;
import ru.hh.nab.example.dto.CarDto;
import ru.hh.nab.example.entity.Car;

public class CarService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CarService.class);
  private GenericDao genericDao;

  @Inject
  public CarService(GenericDao genericDao) {
    this.genericDao = genericDao;
  }

  public void saveCar(Car car) {
    genericDao.save(car);
    LOGGER.info("save car with id = {}", car.id);
  }

  public CarDto getCar(int id) {
    Car car = Optional.ofNullable(genericDao.get(Car.class, id))
        .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));

    return new CarDto(car.getId(), car.getModel(), car.getColor());
  }

  public void update(Car car) {
    genericDao.update(car);
  }

  public void delete(int id) {
    Car car = genericDao.get(Car.class, id);
    genericDao.delete(car);
  }
}
