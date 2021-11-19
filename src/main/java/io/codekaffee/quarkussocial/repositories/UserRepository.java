package io.codekaffee.quarkussocial.repositories;

import io.codekaffee.quarkussocial.models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
}
