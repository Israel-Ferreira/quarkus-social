package io.codekaffee.quarkussocial;

import io.codekaffee.quarkussocial.dto.PostRequest;
import io.codekaffee.quarkussocial.dto.PostResponse;
import io.codekaffee.quarkussocial.dto.RespError;
import io.codekaffee.quarkussocial.dto.ResponseError;
import io.codekaffee.quarkussocial.exceptions.UnauthorizedUserException;
import io.codekaffee.quarkussocial.exceptions.UserNotFoundException;
import io.codekaffee.quarkussocial.models.Post;
import io.codekaffee.quarkussocial.services.PostService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBodySchema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.inject.Inject;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.util.List;
import java.util.stream.Collectors;


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
            return Response.status(Response.Status.CREATED).entity(post).build();
        }catch (UserNotFoundException userNotFoundException){
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (RuntimeException exception){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }



    }

    @GET
    @Produces(value = MediaType.APPLICATION_JSON)
    @Operation(summary = "Listar posts de um usuário especifico")
    @APIResponses(
        value = {

            @APIResponse(
                responseCode = "200",
                description = "Posts do Usuário",
                content = @Content(
                    schema = @Schema(
                        minItems = 1, 
                        type = SchemaType.ARRAY, 
                        implementation = PostResponse.class
                    )
                )
            ),

            @APIResponse(
                responseCode = "404", 
                description = "Usuário ou seguidor não encontrado",
                content = @Content( 
                    schema = @Schema(implementation = RespError.class)
                )
            ),

            @APIResponse(
                responseCode = "403",
                description = "Seguidor não autorizado a ver posts deste usuario",
                content = @Content(
                    schema = @Schema(implementation = RespError.class)
                )
                
            )
        }
    )
    public Response listPosts(@PathParam("userId") Long userId, @HeaderParam("followerId") Long followerId){


        try {
            
            List<PostResponse> posts = this.postService.listAllUserPosts(userId, followerId).stream()
            .map(PostResponse::fromEntity)
            .collect(Collectors.toList());

            return Response.ok(posts).build();
        } catch (UnauthorizedUserException uex) {
            RespError respError = new RespError(uex.getLocalizedMessage(), Status.FORBIDDEN.getStatusCode());
            return Response.status(Status.FORBIDDEN).entity(respError).build();
        } catch(UserNotFoundException exception) {
            RespError respError = new RespError(exception.getLocalizedMessage(), Status.NOT_FOUND.getStatusCode());
            return Response.status(Status.NOT_FOUND).entity(respError).build();
        }


       
    }

}
