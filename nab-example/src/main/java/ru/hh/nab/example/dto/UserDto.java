package ru.hh.nab.example.dto;

import java.util.List;

public class UserDto {
  private int id;
  private String name;

  private List<CarDto> cars;

  public UserDto() {

  }

  public UserDto(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public UserDto(int id, String name, List<CarDto> cars) {
    this.id = id;
    this.name = name;
    this.cars = cars;
  }

  public int getId() {
    return id;
  }

  public UserDto setId(int id) {
    this.id = id;

    return this;
  }

  public String getName() {
    return name;
  }

  public UserDto setName(String name) {
    this.name = name;

    return this;
  }

  public List<CarDto> getCars() {
    return cars;
  }

  public UserDto setCars(List<CarDto> cars) {
    this.cars = cars;

    return this;
  }

  @Override
  public String toString() {
    return "UserDto{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", cars=" + cars +
        '}';
  }
}
