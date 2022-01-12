package io.codekaffee.quarkussocial;

import io.codekaffee.quarkussocial.dto.CreateUserRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import javax.ws.rs.core.Response.Status;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class UserResourceTest {


    @Test
    @Order(1)
    @DisplayName("Deve criar um usuário com sucesso")
    void shouldCreateUserWhenIsBodyValid() {
        var user = new CreateUserRequest("Matheus Faria", 24);

        var response = given()
            .contentType(ContentType.JSON)
            .body(user)
            .when().post("/users")
            .then().extract().response();


        Assertions.assertEquals(Status.CREATED.getStatusCode(), response.getStatusCode());
        Assertions.assertNotNull(response.jsonPath().getString("id"));

    }




    @Test
    @Order(2)
    @DisplayName("Não deve criar o usuário, se o nome do usuário estiver em branco")
    void shouldntCreateUserWhenNameIsBlank() {
        var user = new CreateUserRequest("", 24);

        var response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when().post("/users")
                .then().extract().response();


        Assertions.assertEquals(422, response.getStatusCode());

    }


    @Test
    @Order(3)
    @DisplayName("Não deve criar um usuário, se o nome do usuário estiver nulo")
    void shouldNotCreateUserWithNullValue() {
        var user =  new CreateUserRequest(null, 24);

        var response = given()
                .contentType(ContentType.JSON)
                .body(user)
                .when().post("/users")
                .then().extract().response();


        Assertions.assertEquals(422, response.getStatusCode());
    }


    @Test
    @DisplayName("Não deve criar o usuário, se campos do json nõa forem válidos")
    void shouldntCreateUserWithInvalidJson(){
        var user =  new CreateUserRequest(null, null);

        var response = given().when()
                .contentType(ContentType.JSON)
                .body(user)
                .when().post("/users")
                .then().extract().response();


        List<Map<String, String>> erros =  response.jsonPath().getList("fieldErrors");


        Assertions.assertEquals(422, response.getStatusCode());
        Assertions.assertEquals("Validation Error", response.jsonPath().getString("message"));

        Assertions.assertNotNull(erros);
        Assertions.assertNotEquals(0, erros.size());
    }



    @Test
    @Order(4)
    @DisplayName("Deve retornar uma lista de usuarios")
    void shouldReturnAListOfUsers() {

        given().contentType(ContentType.JSON)
                .when().get("/users")
                .then()
                .statusCode(200)
                .body("size()", Matchers.is(1));



    }


}
