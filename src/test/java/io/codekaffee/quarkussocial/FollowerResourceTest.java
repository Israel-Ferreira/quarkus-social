package io.codekaffee.quarkussocial;

import io.codekaffee.quarkussocial.dto.FollowerDTO;
import io.codekaffee.quarkussocial.models.Follower;
import io.codekaffee.quarkussocial.models.User;
import io.codekaffee.quarkussocial.repositories.FollowerRepository;
import io.codekaffee.quarkussocial.repositories.UserRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
class FollowerResourceTest {


    @Inject
    UserRepository userRepository;

    @Inject
    private FollowerRepository followerRepository;

    User user1, user2;



    @BeforeEach
    @Transactional
    void setUp() {
        var u1 = new User();
        u1.setName("Example");

        u1.setAge(22);


        var u2 = new User();
        u2.setAge(33);
        u2.setName("Example2");

        var u3 =  new User();
        u3.setAge(44);
        u3.setName("Example3");


        this.userRepository.persist(u1, u2, u3);

        this.user1 = u1;
        this.user2 = u2;


        Follower follower = new Follower();
        follower.setFollower(u3);
        follower.setUser(u1);

        followerRepository.persist(follower);
    }



    @Test
    @DisplayName("Não deve permitir que o usuário inexistente siga um usuário da base")
    void shouldNotStartFollowerUserWhenFollowerIdIsNotFoundOnDb() {
        Long followerId = 99999L;

        FollowerDTO followerDTO = new FollowerDTO();
        followerDTO.setFollowerId(followerId);

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", user1.getId())
                .body(followerDTO)
                .when().put()
                .then().statusCode(404);


    }


    @Test
    @DisplayName("Deve retornar 204, caso o follower id e o user id estejam na base de dados")
    void userAndFollowerIdValid() {
        FollowerDTO followerDTO = new FollowerDTO();
        followerDTO.setFollowerId(user2.getId());

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", user1.getId())
                .body(followerDTO)
                .when().put()
                .then().statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }


    @Test
    @DisplayName("Deve devolver o status 404, caso o id do usuario não esteja na base de dados")
    void userNotFound() {
        Long userId = 900L;

        FollowerDTO followerDTO = new FollowerDTO();
        followerDTO.setFollowerId(user2.getId());


        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
                .body(followerDTO)
                .when().put()
                .then().statusCode(404);
    }





    @Test
    @DisplayName("Deve retornar uma lista de seguidores")
    void shouldListUserFollowers() {

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", user1.getId())
                .when().get()
                .then().statusCode(Response.Status.OK.getStatusCode())
                .body("followerCount", Matchers.greaterThan(0))
                .body("followers", Matchers.hasSize(Matchers.greaterThan(0)));
    }


    @Test
    @DisplayName("Deve devolver o status 409, caso o follower id seja igual ao id do usuario")
    void followerIdIsEqualToUserId() {

        FollowerDTO followerDTO = new FollowerDTO();
        followerDTO.setFollowerId(user1.getId());

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", user1.getId())
                .body(followerDTO)
                .when().put()
                .then().statusCode(Response.Status.CONFLICT.getStatusCode());

    }



    @Test
    @DisplayName("Deve não fazer o unfollow do usuário, se o usuário não estiver na base")
    void tryUnfollowInexistentser() {
        var inexistentFollowerId =  999L;

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", user1.getId())
                .queryParam("followerId", inexistentFollowerId)
                .when().delete()
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }




    @Test
    @DisplayName("Deve retornar 409, caso o userId for igual ao followerId")
    void dontUnfollowUserWithSameIdOfFollowerId() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", user1.getId())
                .queryParam("followerId", user1.getId())
                .when().delete()
                .then().statusCode(Response.Status.CONFLICT.getStatusCode());
    }



    @Test
    @DisplayName("Deve retornar 409, se o usuário não segue o user id informado")
    void unfollowUserNotWork() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", user1.getId())
                .queryParam("followerId", user2.getId())
                .when().delete()
                .then().statusCode(Response.Status.CONFLICT.getStatusCode());
    }




    @Test
    @DisplayName("Deve retornar 404, se o usuário não estiver na Base")
    void dontShowFollowersWhenUserNotFoundInDb() {

        Long inexistentUserId = 9090L;

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId",  inexistentUserId)
                .when().get()
                .then().statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }


}