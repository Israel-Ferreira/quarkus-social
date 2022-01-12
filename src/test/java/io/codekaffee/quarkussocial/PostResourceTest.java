package io.codekaffee.quarkussocial;

import io.codekaffee.quarkussocial.dto.PostRequest;
import io.codekaffee.quarkussocial.models.User;
import io.codekaffee.quarkussocial.repositories.UserRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;


@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    private UserRepository userRepository;

    private Long userId;


    @BeforeEach
    @Transactional
    void setUp() {
        var user = new User();
        user.setName("Example");

        user.setAge(22);


        userRepository.persist(user);
    }


    @Test
    @Order(1)
    @DisplayName("Deve criar um post com usuário")
    void createPost() {

        var postRequest = new PostRequest();
        postRequest.setContent("Some Text");

        Integer userId = 1;

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", userId)
                .when().post()
                .then().statusCode(201);
    }


    @Test
    @Order(4)
    @DisplayName("Não deve criar um post com o texto em branco")
    void shouldNotCreatePostWithContentBlank() {
        var postRequest = new PostRequest();
        postRequest.setContent("");


        Integer userId = 1;

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", userId)
                .when().post()
                .then().statusCode(400);
    }


    @Test
    @Order(3)
    @DisplayName("Não deve criar o post, se o body enviado for nulo")
    void shouldNotCreatePostWhenBodyIsNull() {

        Integer userId = 1;


        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
                .when().post()
                .then().statusCode(400);
    }


//    @Test
//    @DisplayName("Deve Listar os posts do usuário para o seguidor do mesmo")
//    void shouldListUserPostsWhenFollowerFollowsUser(){
//
//    }


    @Test
    @Order(2)
    @DisplayName("Não deve criar o post de um usuário inexistente na base de dados")
    void shouldNotCreatePostWhenUserIsNotFound() {

        var postRequest = new PostRequest();
        postRequest.setContent("Teste Fulano");

        Integer notFoundUserId = 4;

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", notFoundUserId)
                .body(postRequest)
                .when().post()
                .then().statusCode(404);

    }






    @Test
    @Order(6)
    @DisplayName("Não deve listar os posts, se o id do seguidor (FollowerId) não estiver no Banco")
    void  shouldNotListUserPostsWhenFollowerIdOfUserNotExistsInDb() {
        Integer userId = 1;

        Integer followerId = 1260;


        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
                .header("followerId", followerId)
                .when().get()
                .then().statusCode(404)
                .body("message", Matchers.is("Follower not found"));
    }



    @Test
    @Order(5)
    @DisplayName("Não deve listar os posts, se não existir o id do usuário na base de dados")
    void shouldNotListPostsWhenUserIsNotFound() {
        Integer userId = 7;
        Integer followerId = 5;

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
                .header("followerId", followerId)
                .when().get()
                .then().statusCode(404);
    }



    @Test
    @Order(7)
    @DisplayName("Não deve listar posts, se o followerId for nulo")
    void shouldntListUserPostsWhenFollowerIdIsNull() {

        Integer userId = 1;


        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
                .when().get()
                .then().statusCode(403)
                .body("message", Matchers.is("FollowerId nulo"));

    }


}
