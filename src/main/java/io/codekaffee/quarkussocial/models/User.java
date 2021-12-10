package io.codekaffee.quarkussocial.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.codekaffee.quarkussocial.dto.CreateUserRequest;
import io.smallrye.common.constraint.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Getter @Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer age;


    public User(){}

    public User(CreateUserRequest createUserRequest){
        this.name = createUserRequest.getName();
        this.age = createUserRequest.getAge();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
