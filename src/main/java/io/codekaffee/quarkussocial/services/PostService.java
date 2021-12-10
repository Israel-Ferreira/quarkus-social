package io.codekaffee.quarkussocial.services;

import io.codekaffee.quarkussocial.dto.PostRequest;
import io.codekaffee.quarkussocial.exceptions.UserNotFoundException;
import io.codekaffee.quarkussocial.models.Post;
import io.codekaffee.quarkussocial.models.User;
import io.codekaffee.quarkussocial.repositories.PostRepository;
import io.codekaffee.quarkussocial.repositories.UserRepository;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Validator;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final Validator validator;

    @Inject
    public PostService(PostRepository postRepository, UserRepository userRepository, Validator validator) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.validator = validator;
    }

    public List<Post> listAllUserPosts(Long userId){
        Optional<User> userOpt = userRepository.findByIdOptional(userId);

        if(userOpt.isEmpty()){
            throw new UserNotFoundException();
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
