package io.codekaffee.quarkussocial;

import io.codekaffee.quarkussocial.dto.CreateUserRequest;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;


@Resource
@Path("/users")
public class UserResource {

    @POST
    public Response createUser(CreateUserRequest createUserRequest){
        System.out.println(createUserRequest);
        return Response.status(Response.Status.CREATED).build();
    }
}
