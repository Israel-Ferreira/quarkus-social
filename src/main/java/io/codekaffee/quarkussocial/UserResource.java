package io.codekaffee.quarkussocial;

import io.codekaffee.quarkussocial.dto.CreateUserRequest;
import io.codekaffee.quarkussocial.models.User;
import io.codekaffee.quarkussocial.repositories.UserRepository;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;


@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserRepository userRepository;

    @POST
    public Response createUser(CreateUserRequest createUserRequest){
        System.out.println(createUserRequest);
        return Response.ok(createUserRequest).build();
    }


    @GET
    public Response listAllUsers(){
        List<User> users = userRepository.findAll().list();
        return Response.ok(users).build();
    }
}
