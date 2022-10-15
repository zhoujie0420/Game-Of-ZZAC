package com.kob.matchingsystem.service.impl;

import com.kob.matchingsystem.service.MatchingService;
import com.kob.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {
    public static final MatchingPool matchingPool = new MatchingPool();

    @Override
    public String addPlayer(Integer userId, Integer rating) {
        System.out.println("Add Player: " + userId + " " + rating);
        matchingPool.addPlayer(userId,rating);
        return "add player success";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("Remove Player: " + userId);
        matchingPool.removePlayer(userId);
        return "Remove player success";
    }
}
