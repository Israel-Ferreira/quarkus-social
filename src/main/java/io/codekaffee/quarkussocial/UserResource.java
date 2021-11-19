package io.codekaffee.quarkussocial;

import io.codekaffee.quarkussocial.dto.CreateUserRequest;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;


@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @POST
    public Response createUser(CreateUserRequest createUserRequest){
        System.out.println(createUserRequest);
        return Response.ok(createUserRequest).build();
    }


    @GET
    public Response listAllUsers(){
        List<CreateUserRequest> users = Arrays.asList(
                new CreateUserRequest("Israel", 22),
                new CreateUserRequest("Alexsander Vader", 22),
                new CreateUserRequest("Ciro V.", 24)
        );

        return Response.ok(users).build();
    }
}
