package io.codekaffee.quarkussocial;

import io.codekaffee.quarkussocial.dto.CreateUserRequest;
import io.codekaffee.quarkussocial.models.User;
import io.codekaffee.quarkussocial.repositories.UserRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {


    private final UserRepository userRepository;
    private final Validator validator;

    @Inject
    public UserResource(UserRepository  userRepository, Validator validator){
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @POST
    @Transactional
    public Response createUser(CreateUserRequest createUserRequest){

        var validations = validator.validate(createUserRequest);

        if(validations.isEmpty()){

            User user = new User(createUserRequest);

            userRepository.persist(user);

            return Response.ok(user).build();
        }


        var violations = validations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());


        return Response.status(Response.Status.BAD_REQUEST).entity(violations).build();
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

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, CreateUserRequest createUserRequest){
        Optional<User> userOpt =  userRepository.findByIdOptional(id);

        if(userOpt.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var validations = validator.validate(userOpt.get());

        if(validations.isEmpty()) {

            User user = userOpt.get();
            user.setName(createUserRequest.getName());
            user.setAge(createUserRequest.getAge());

            userRepository.persist(user);

            return Response.noContent().build();
        }


        var violations = validations.stream().map(ConstraintViolation::getMessage);


        return Response.status(Response.Status.BAD_REQUEST).entity(violations).build();


    }


    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteById(@PathParam("id") Long id){
        boolean deleted = userRepository.deleteById(id);

        if(deleted){
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }


    @GET
    public Response listAllUsers(){
        List<User> users = userRepository.findAll().list();
        return Response.ok(users).build();
    }
}
