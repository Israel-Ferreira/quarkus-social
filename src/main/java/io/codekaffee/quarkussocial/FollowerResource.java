package io.codekaffee.quarkussocial;

import io.codekaffee.quarkussocial.dto.FollowerDTO;
import io.codekaffee.quarkussocial.exceptions.UserNotFoundException;
import io.codekaffee.quarkussocial.models.Follower;
import io.codekaffee.quarkussocial.repositories.FollowerRepository;
import io.codekaffee.quarkussocial.services.FollowerService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users/{userId}/followers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FollowerResource {

    private final FollowerService followerService;

    @Inject
    public FollowerResource(FollowerService followerService) {
        this.followerService = followerService;
    }

    @PUT
    public Response followUser(@PathParam("userId") Long userId, FollowerDTO followerDTO){
        try {
            Follower follower = followerService.followUser(followerDTO, userId);
            return Response.noContent().build();
        }catch (UserNotFoundException e){
            return Response.status(Response.Status.NOT_FOUND).build();
        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    public Response unfollowUser(@PathParam("userId") Long userId){
        return Response.ok().build();
    }
}
