package ru.hh.nab.example.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import ru.hh.nab.example.dao.GenericDao;
import ru.hh.nab.example.dto.CarDto;
import ru.hh.nab.example.dto.UserDto;
import ru.hh.nab.example.entity.User;

public class UserService {

  private GenericDao genericDao;

  @Inject
  public UserService(GenericDao genericDao) {
    this.genericDao = genericDao;
  }

  public UserDto getUser(int id) {
    User user = Optional.ofNullable(genericDao.get(User.class, id))
        .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    List<CarDto> cars = user.getCars().stream()
        .map(car -> new CarDto(car.getId(), car.getModel(), car.getColor())).collect(Collectors.toList());
    return new UserDto(user.getId(), user.getName(), cars);
  }

  public void saveUser(User user) {
    user.getCars().forEach(car -> car.setUser(user));
    genericDao.save(user);
  }

  public void updateUser(User user) {
    genericDao.update(user);
  }

  public void deleteUser(int id) {
    User user = genericDao.get(User.class, id);
    genericDao.delete(user);
  }
}
