package io.codekaffee.quarkussocial.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class PostRequest implements Serializable {

    @NotNull
    @NotBlank
    private String content;
}
