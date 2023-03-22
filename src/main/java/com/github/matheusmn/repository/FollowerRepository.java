package com.github.matheusmn.repository;

import com.github.matheusmn.entity.Follower;
import com.github.matheusmn.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    public boolean follows(User follower, User user) {

        Map<String, Object> params = new HashMap<>();
        params.put("follower", follower);
        params.put("user", user);

        PanacheQuery<Follower> query = find("follower = :follower and user = :user ", params);
        Optional<Follower> result = query.firstResultOptional();

        return result.isPresent();

    }

    public List<Follower> followers(User user) {
        PanacheQuery<Follower> query = find("user", user);
        return query.list();
    }

    public void deleteByFollowerAndUser(Long followerId, Long userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("followerId", followerId);
        params.put("userId", userId);

        delete("follower.id = :followerId and user.id = :userId ", params);
    }
}
