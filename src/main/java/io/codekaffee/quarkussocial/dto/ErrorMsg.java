package io.codekaffee.quarkussocial.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMsg {
    private String msg;
    private Integer status;
}
