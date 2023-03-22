package com.github.matheusmn.repository;

import com.github.matheusmn.entity.Follower;
import com.github.matheusmn.entity.Post;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post> {
    public List<Post> findPostByFollows(List<Long> collect) {
        List<Post> posts = new ArrayList<>();
        for (Long id: collect) {
            PanacheQuery<Post> query = find("user.id", id);
                posts.addAll(query.list());
        }
        return posts;
    }
}
