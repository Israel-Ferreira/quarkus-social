package io.codekaffee.quarkussocial;

import static io.restassured.RestAssured.given;

import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.codekaffee.quarkussocial.dto.CreateUserRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class UserResourceTest {

    @Test
    @DisplayName("Deve criar um usuário com sucesso")
    public void shouldCreateUserWhenIsBodyValid() {
        var user = new CreateUserRequest("Matheus Faria", 24);

        var response = given()
            .contentType(ContentType.JSON)
            .body(user)
            .when().post("/users")
            .then().extract().response();


        Assertions.assertEquals(Status.CREATED.getStatusCode(), response.getStatusCode());
        Assertions.assertNotNull(response.jsonPath().getString("id"));

    }
}
