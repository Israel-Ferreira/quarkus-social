package io.codekaffee.quarkussocial.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class ResponseError {
    private String message;
    private Collection<FieldError> fieldErrors;


    public ResponseError(String message, Collection<FieldError> fieldErrors) {
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public static <T> ResponseError createFromValidation(Set<ConstraintViolation<T>> violations){
        List<FieldError> errors = violations.stream().map(cv -> new FieldError(cv.getPropertyPath().toString(), cv.getMessage()))
                .collect(Collectors.toList());

        return new ResponseError("Validation Error", errors);
    }


    public Response withStatusCode(int statusCode) {
        return Response.status(statusCode).entity(this).build();
    }

}
