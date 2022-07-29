package ru.hh.nab.example.dto;

public class CarDto {
  public int id;

  public String model;

  public String color;

  public CarDto() {

  }

  public CarDto(int id, String model, String color) {
    this.id = id;
    this.model = model;
    this.color = color;
  }

  public int getId() {
    return id;
  }

  public CarDto setId(int id) {
    this.id = id;

    return this;
  }

  public String getModel() {
    return model;
  }

  public CarDto setModel(String model) {
    this.model = model;

    return this;
  }

  public String getColor() {
    return color;
  }

  public CarDto setColor(String color) {
    this.color = color;

    return this;
  }

  @Override
  public String toString() {
    return "CarDto{" +
        "id=" + id +
        ", model='" + model + '\'' +
        ", color='" + color + '\'' +
        '}';
  }
}
