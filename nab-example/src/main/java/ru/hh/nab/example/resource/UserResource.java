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
import ru.hh.nab.example.dto.UserDto;
import ru.hh.nab.example.entity.User;
import ru.hh.nab.example.service.UserService;

@Path("/user")
public class UserResource {

  private UserService userService;

  @Inject
  public UserResource(UserService userService) {
    this.userService = userService;
  }

  @Path("{id :[0-9]+}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUser(@PathParam("id") int id) {
    UserDto user = userService.getUser(id);
    return Response.ok(user).build();
  }

  @POST
  @Path("/save")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response saveUser(User user) {
    userService.saveUser(user);
    return Response.ok().build();
  }

  @PUT
  @Path("/update")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateUser(User user) {
    userService.updateUser(user);
    return Response.ok().build();
  }
  @DELETE
  @Path("/delete/{id :[0-9]+}")
  public Response deleteUser(@PathParam("id") int id) {
    userService.deleteUser(id);
    return Response.ok().build();
  }
}
