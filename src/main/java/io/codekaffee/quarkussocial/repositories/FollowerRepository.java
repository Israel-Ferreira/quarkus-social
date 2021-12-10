package io.codekaffee.quarkussocial.repositories;

import io.codekaffee.quarkussocial.models.Follower;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {
}
