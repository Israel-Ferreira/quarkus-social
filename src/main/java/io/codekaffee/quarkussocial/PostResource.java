package io.codekaffee.quarkussocial;

import io.codekaffee.quarkussocial.dto.PostRequest;
import io.codekaffee.quarkussocial.dto.ResponseError;
import io.codekaffee.quarkussocial.exceptions.UserNotFoundException;
import io.codekaffee.quarkussocial.models.Post;
import io.codekaffee.quarkussocial.services.PostService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBodySchema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;

import javax.inject.Inject;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;


@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private final PostService postService;
    private final Validator validator;


    @Inject
    public PostResource(PostService postService, Validator validator) {
        this.postService = postService;
        this.validator = validator;
    }

    @POST
    @Operation(summary= "Criar post de um usuário especifico")
    @RequestBodySchema(PostRequest.class)
    public Response createPost(@PathParam("userId") Long userId, PostRequest postRequest){

        var violations = validator.validate(postRequest);

        if(!violations.isEmpty()){
            ResponseError responseError = ResponseError.createFromValidation(violations);

            return responseError.withStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        }

        try {
            Post post = postService.savePost(userId, postRequest);
            return Response.status(Response.Status.CREATED).build();
        }catch (UserNotFoundException userNotFoundException){
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (RuntimeException exception){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }



    }

    @GET
    @APIResponseSchema(Object.class)
    @Operation(summary = "Listar posts de um usuário especifico")
    public Response listPosts(@PathParam("userId") Long userId){
        List<Post> posts = this.postService.listAllUserPosts(userId);
        return Response.ok(posts).build();
    }

}
