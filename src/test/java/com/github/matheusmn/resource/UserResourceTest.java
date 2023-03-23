package com.github.matheusmn.resource;

import com.github.matheusmn.entity.dto.UserCreateDto;
import com.github.matheusmn.error.ResponseError;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserResourceTest {


    @Test
    @DisplayName("should create an user successfully")
    public void createUserTest(){
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setName("Mathews");
        userCreateDto.setEmail("mm@gmail.com.uk");
        userCreateDto.setAge(25);

        var response = given()
                .contentType(ContentType.JSON)
                .body(userCreateDto)
            .when()
                .post("/users")
            .then()
                .extract()
                .response();

        assertEquals(201, response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));

    }

    @Test
    @DisplayName("should send status 400")
    public void createUserWithErrorValidationTest(){
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setName("Mathews");
        userCreateDto.setEmail("mm@gmail.com.uk");
        userCreateDto.setAge(null);

        var response = given()
                .contentType(ContentType.JSON)
                .body(userCreateDto)
                .when()
                .post("/users")
                .then()
                .extract()
                .response();

        assertEquals(400, response.statusCode());
        assertNotNull(response.jsonPath().getString("message"));

    }
}