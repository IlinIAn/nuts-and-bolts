package ru.hh.nab.example.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "car")
public class Car {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public int id;

  public String model;

  public String color;

  @ManyToOne(fetch = FetchType.LAZY)
  public User user;

  public Car() {

  }

  public Car(int id, String model, String color) {
    this.id = id;
    this.model = model;
    this.color = color;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
