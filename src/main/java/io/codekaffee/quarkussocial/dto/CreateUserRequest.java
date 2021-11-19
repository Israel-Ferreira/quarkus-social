package io.codekaffee.quarkussocial.dto;


import java.io.Serializable;

public class CreateUserRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private Integer age;


    public CreateUserRequest(){}


    public CreateUserRequest(String name, Integer age) {
        this.name = name;
        this.age = age;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
