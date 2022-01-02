package io.codekaffee.quarkussocial.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserFollowersDTO {
    private Long followerCount;
    private List<FollowerResponse> followers = new ArrayList<>();
}
