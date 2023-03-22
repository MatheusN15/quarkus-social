package com.github.matheusmn.resource;

import com.github.matheusmn.entity.Post;
import com.github.matheusmn.entity.User;
import com.github.matheusmn.entity.dto.PostCreateDto;
import com.github.matheusmn.entity.dto.PostResponseDto;
import com.github.matheusmn.entity.dto.UserCreateDto;
import com.github.matheusmn.error.ResponseError;
import com.github.matheusmn.repository.PostRepository;
import com.github.matheusmn.repository.UserRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    public UserRepository userRepository;

    @Inject
    public PostRepository postRepository;

    @Inject
    public Validator validator;

    @POST
    @Transactional
    public Response post(UserCreateDto request){

        Set<ConstraintViolation<UserCreateDto>> violations = validator.validate(request);
        if(!violations.isEmpty()){
            ResponseError responseError = ResponseError.createFromValidation(violations);

            return Response.status(400).entity(responseError).build();
        }

        User user = new User();
        user.name = request.getName();
        user.age = request.getAge();
        user.email = request.getEmail();
        userRepository.persist(user);
        return Response.status(Response.Status.CREATED.getStatusCode())
                .entity(user)
                .build();
    }

    @GET
    public Response get(){
        List<User> users = userRepository.listAll();
        if (users.isEmpty()){
            Response.status(400).build();
        }
        return Response.ok().entity(users).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id){

        User user = userRepository.findById(id);

        if (user != null){
            return Response.ok().entity(userRepository.findById(id)).build();
        }
        return Response.status(Response.Status.NOT_FOUND.getStatusCode())
                    .build();
    }

    @PUT
    @Transactional
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, UserCreateDto request){
        User user = User.findById(id);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        user.name = request.getName();
        user.age = request.getAge();
        user.email = request.getEmail();
        userRepository.persist(user);
        return Response.ok(user).build();
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id){
        User user = User.findById(id);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        userRepository.delete(user);
        return Response.ok().build();
    }

    @POST
    @Transactional
    @Path("/{userId}/post")
    public Response postPosts(@PathParam("userId") Long userId, PostCreateDto postDto){
        User user = userRepository.findById(userId);

        if (user == null){
            return Response.status(Response.Status.NOT_FOUND.getStatusCode())
                    .build();
        }

        Post post = new Post();
        post.setUser(user);
        post.setText(postDto.getText());
        post.setDateTime(LocalDateTime.now());

        postRepository.persist(post);

        return Response.ok().entity(post).build();

    }

    @GET
    @Path("/{userId}/post")
    public Response getPost(@PathParam("userId") Long userId){
        User user = userRepository.findById(userId);
        if (user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        PanacheQuery<Post> query = postRepository.find("user", Sort.by("dateTime", Sort.Direction.Descending), user);
        List<Post> postList = query.list();

        List<PostResponseDto> response = postList.stream()
                .map(post -> PostResponseDto.fromEntity(post)).collect(Collectors.toList());
        return Response.ok(response).build();
    }

}
