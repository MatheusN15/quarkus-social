package com.github.matheusmn.resource;

import com.github.matheusmn.entity.Follower;
import com.github.matheusmn.entity.User;
import com.github.matheusmn.entity.dto.FollowerRequestDto;
import com.github.matheusmn.entity.dto.FollowersResponseDto;
import com.github.matheusmn.repository.FollowerRepository;
import com.github.matheusmn.repository.UserRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

    @Inject
    private FollowerRepository repository;

    @Inject
    public UserRepository userRepository;

    @GET
    public Response getAll(@PathParam("userId") Long userId){
        User user = userRepository.findById(userId);

        if (user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Follower> followersList = repository.followers(user);
        FollowersResponseDto dto = FollowersResponseDto.getList(followersList);

        return Response.ok().entity(dto).build();
    }

    @PUT
    @Transactional
    public Response follow(@PathParam("userId") Long userId, FollowerRequestDto request){

        if(userId.equals(request.getFollowerId())){
            return Response.status(Response.Status.CONFLICT).entity("Why are you trying follow yourself?").build();
        }

        User user = userRepository.findById(userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        User follower = userRepository.findById(request.getFollowerId());

        boolean follows = repository.follows(follower, user);
        if(!follows){
            Follower entity = new Follower();
            entity.setUser(user);
            entity.setFollower(follower);
            repository.persist(entity);
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Transactional
    public Response unFollow(@PathParam("userId") Long userId, @QueryParam("followerId") Long followerId){

        User user = userRepository.findById(userId);
        if (user == null && followerId == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        repository.deleteByFollowerAndUser(followerId, userId);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
