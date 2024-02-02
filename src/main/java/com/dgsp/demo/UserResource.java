package com.dgsp.demo;

import com.dgsp.demo.dto.User;
import com.dgsp.demo.dto.UserResponse;
import com.dgsp.demo.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/user")
public class UserResource {

    @Inject
    UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{userId}")
    public UserResponse getUser(String userId) {
        return userService.getUser(userId);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public UserResponse registerUser(User user){
        return userService.saveUser(user);
    }

    @GET
    public UserResponse getAllUsers(){
        return userService.getAllUsers();
    }
}
