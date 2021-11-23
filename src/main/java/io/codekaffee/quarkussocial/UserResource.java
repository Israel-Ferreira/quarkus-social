package io.codekaffee.quarkussocial;

import io.codekaffee.quarkussocial.dto.CreateUserRequest;
import io.codekaffee.quarkussocial.models.User;
import io.codekaffee.quarkussocial.repositories.UserRepository;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.transaction.Transactional;
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
    @Transactional
    public Response createUser(CreateUserRequest createUserRequest){
        User user = new User(createUserRequest);

        userRepository.persist(user);

        return Response.ok(createUserRequest).build();
    }



    @GET
    @Path("{id}")
    public Response findById(@PathParam("id") Long id){
        var optUser = userRepository.findByIdOptional(id);

        if(optUser.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }


        return Response.ok(optUser.get()).build();
    }


    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id){
        boolean deleted = userRepository.deleteById(id);

        if(deleted){
            return Response.noContent().build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    @GET
    public Response listAllUsers(){
        List<User> users = userRepository.findAll().list();
        return Response.ok(users).build();
    }
}
