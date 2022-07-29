package ru.hh.nab.example.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ru.hh.nab.example.dto.CarDto;
import ru.hh.nab.example.entity.Car;
import ru.hh.nab.example.service.CarService;

@Path("/car")
public class CarResource {

  private CarService carService;

  @Inject
  public CarResource(CarService carService) {
    this.carService = carService;
  }

  @Path("{id :[0-9]+}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public CarDto getCar(@PathParam("id") int id) {
    return carService.getCar(id);
  }

  @Path("/save")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response saveCar(Car car) {
    carService.saveCar(car);
    return Response.ok().build();
  }

  @Path("/update")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  public Response update(Car car) {
    carService.update(car);
    return Response.ok().build();
  }

  @Path("/delete/{id :[0-9]+}")
  @DELETE
  public Response delete(@PathParam("id") int id) {
    carService.delete(id);
    return Response.ok().build();
  }
}
