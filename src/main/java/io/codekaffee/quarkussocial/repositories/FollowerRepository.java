package io.codekaffee.quarkussocial.repositories;

import io.codekaffee.quarkussocial.models.Follower;
import io.codekaffee.quarkussocial.models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    public boolean follows(User user, User follower){
        Map<String, Object> params =  new HashMap<>();
        params.put("follower", follower);
        params.put("user", user);

        var query = find("follower = :follower and user = :user", params).firstResultOptional();

        return query.isPresent();
    }


    public void deleteByUserAndFollower(Long userId, Long followerId) {
        var params = Parameters.with("userId", userId)
            .and("followerId", followerId).map();

        delete("follower.id = :followerId and user.id = :userId", params);
    }
}
