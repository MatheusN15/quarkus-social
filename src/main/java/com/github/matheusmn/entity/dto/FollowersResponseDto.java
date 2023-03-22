package com.github.matheusmn.entity.dto;

import com.github.matheusmn.entity.Follower;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FollowersResponseDto {

    private Integer counter;
    private List<FollowersDto> followersList;

    public static FollowersResponseDto getList(List<Follower> list){
        FollowersResponseDto response = new FollowersResponseDto();
        List<FollowersDto> followList = new ArrayList<>();
        for (Follower follow: list) {
            FollowersDto followersDto = new FollowersDto();
            followersDto.setId(follow.getFollower().getId());
            followersDto.setAge(follow.getFollower().getAge());
            followersDto.setName(follow.getFollower().getName());
            followersDto.setEmail(follow.getFollower().getEmail());

            followList.add(followersDto);
        }

        response.setCounter(list.size());
        response.setFollowersList(followList);

        return response;
    }
}
