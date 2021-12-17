package io.codekaffee.quarkussocial.dto;

import io.codekaffee.quarkussocial.models.Follower;
import lombok.Data;

@Data
public class FollowerResponse {
    private Long id;
    private String name;

    public FollowerResponse(Follower follower){
        this.id = follower.getId();
        this.name = follower.getUser().getName();
    }
}
