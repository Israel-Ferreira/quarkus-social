package io.codekaffee.quarkussocial.services;

import io.codekaffee.quarkussocial.dto.PostRequest;
import io.codekaffee.quarkussocial.exceptions.UnauthorizedUserException;
import io.codekaffee.quarkussocial.exceptions.UserNotFoundException;
import io.codekaffee.quarkussocial.models.Post;
import io.codekaffee.quarkussocial.models.User;
import io.codekaffee.quarkussocial.repositories.FollowerRepository;
import io.codekaffee.quarkussocial.repositories.PostRepository;
import io.codekaffee.quarkussocial.repositories.UserRepository;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;

    @Inject
    public PostService(PostRepository postRepository, UserRepository userRepository, FollowerRepository followerRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
    }

    public List<Post> listAllUserPosts(Long userId, Long followerId){
        Optional<User> userOpt = userRepository.findByIdOptional(userId);

        if(userOpt.isEmpty()){
            throw new UserNotFoundException();
        }

        Optional<User> follower =  userRepository.findByIdOptional(followerId);

        if(follower.isEmpty()) {
            throw new UserNotFoundException("Follower not found");
        }

        if(!followerRepository.follows(userOpt.get(), follower.get())) {
            throw new UnauthorizedUserException();
        }

        return postRepository.find("user", Sort.by("dateTime", Sort.Direction.Descending), userOpt.get())
                .list();
    }


    @Transactional
    public Post savePost(Long userId, PostRequest postRequest){

        Optional<User> userOpt = userRepository.findByIdOptional(userId);

        if(userOpt.isEmpty()){
            throw new UserNotFoundException();
        }

        User user = userOpt.get();

        Post post = new Post(postRequest, user);

        postRepository.persist(post);


        return post;
    }
}
