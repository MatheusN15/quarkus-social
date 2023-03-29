package com.github.matheusmn.service;

import com.github.matheusmn.entity.User;
import com.github.matheusmn.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import io.quarkus.example.*;
import io.quarkus.grpc.GrpcService;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Blocking
@GrpcService
public class HelloService implements Greeter {

    @Inject
    public UserRepository userRepository;

    @Override
    public Uni<HelloReply> sayHello(HelloRequest request) {
        return Uni.createFrom().item("Hello " + request.getName() + "!")
                .map(msg -> HelloReply.newBuilder().setMessage(msg).build());
    }

    @Override
    public Uni<UserList> getListUsers(Empty request) {
        List<User> users = userRepository.listAll();
        if (users.isEmpty()){
            return null;
        }
        List<io.quarkus.example.User> hj = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            io.quarkus.example.User userToList = io.quarkus.example.User.newBuilder()
                    .setName(users.get(i).getName())
                    .setAge(users.get(i).getAge())
                    .setEmail(users.get(i).getEmail())
                    .build();
            hj.add(userToList);
        }

        return Uni.createFrom().item(users).map(user -> UserList.newBuilder().addAllUsers(hj).build());
    }
}
