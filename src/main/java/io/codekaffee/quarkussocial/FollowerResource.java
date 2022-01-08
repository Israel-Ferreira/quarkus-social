package io.codekaffee.quarkussocial;

import io.codekaffee.quarkussocial.dto.FollowerDTO;
import io.codekaffee.quarkussocial.dto.FollowerResponse;
import io.codekaffee.quarkussocial.dto.ResponseError;
import io.codekaffee.quarkussocial.dto.UserFollowersDTO;
import io.codekaffee.quarkussocial.exceptions.FollowerIdIsEqualToUserException;
import io.codekaffee.quarkussocial.exceptions.UserNotFoundException;
import io.codekaffee.quarkussocial.services.FollowerService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Path("/users/{userId}/followers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FollowerResource {

    private final FollowerService followerService;

    @Inject
    public FollowerResource(FollowerService followerService) {
        this.followerService = followerService;
    }

    @GET
    public Response getFollowers(@PathParam("userId") Long  userId){
        var followers = followerService.getUserFollowers(userId)
                .stream().map(FollowerResponse::new)
                .collect(Collectors.toList());

        UserFollowersDTO ufsd = new UserFollowersDTO();
        ufsd.setFollowerCount((long) followers.size());
        ufsd.setFollowers(followers);

        return Response.ok().entity(ufsd).build();
    }

    @PUT
    public Response followUser(@PathParam("userId") Long userId, FollowerDTO followerDTO){
        try {
            followerService.followUser(followerDTO, userId);
            return Response.noContent().build();
        }catch (UserNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }catch (FollowerIdIsEqualToUserException exception){
            ResponseError rer = new ResponseError(exception.getMessage(), new ArrayList<>());
            return Response.status(Response.Status.CONFLICT).entity(rer).build();
        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    public Response unfollowUser(@PathParam("userId") Long userId, @QueryParam("followerId") Long followerId){

        if(userId == followerId) {
            return Response.status(Status.CONFLICT).build();
        }


        try {
            this.followerService.unfollowUser(userId, followerId);
            return Response.status(Status.NO_CONTENT).build();
        }catch(UserNotFoundException e){
            return Response.status(Status.NOT_FOUND).build();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

    }
}
