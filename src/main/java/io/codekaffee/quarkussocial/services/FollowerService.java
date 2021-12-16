package io.codekaffee.quarkussocial.services;

import io.codekaffee.quarkussocial.dto.FollowerDTO;
import io.codekaffee.quarkussocial.exceptions.UserNotFoundException;
import io.codekaffee.quarkussocial.models.Follower;
import io.codekaffee.quarkussocial.models.User;
import io.codekaffee.quarkussocial.repositories.FollowerRepository;
import io.codekaffee.quarkussocial.repositories.UserRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class FollowerService {

    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;

    @Inject
    public FollowerService(FollowerRepository followerRepository, UserRepository userRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

    private User searchUser(Long userId) {
        Optional<User> optionalUser = userRepository.findByIdOptional(userId);

        if(optionalUser.isEmpty()){
            throw  new UserNotFoundException();
        }

        return  optionalUser.get();
    }


    @Transactional
    public Follower followUser(FollowerDTO followerDTO, Long userId){

        try {
            User user = searchUser(userId);

            User followerUser = searchUser(followerDTO.getFollowerId());

            if(followerRepository.follows(user, followerUser)) {
                throw new RuntimeException();
            }

            Follower follower = new Follower();
            follower.setUser(user);
            follower.setFollower(followerUser);

            followerRepository.persist(follower);

            return follower;
        }catch (UserNotFoundException userNotFoundException){
            throw new UserNotFoundException(userNotFoundException);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

}
