package com.github.matheusmn.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.quarkus.example.*;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
@Produces(MediaType.TEXT_PLAIN)
public class GreetingResource {

    @GrpcClient
    Greeter hello;

    @GET
    public Uni<UserList> hello() {
        return hello.getListUsers(Empty.newBuilder().build());
    }

    @GET
    @Path("/{name}")
    public Uni<String> hello(String name) {
        return hello.sayHello(HelloRequest.newBuilder().setName(name).build())
                .onItem().transform(helloReply -> helloReply.getMessage());
    }
}