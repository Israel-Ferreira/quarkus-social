package io.codekaffee.quarkussocial.dto;

import io.codekaffee.quarkussocial.models.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponse {
    private String content;
    private LocalDateTime dateTime;

    public static PostResponse fromEntity(Post post){
        var response = new PostResponse();
        response.setContent(post.getPostContent());
        response.setDateTime(post.getDateTime());

        return response;
    }
}
