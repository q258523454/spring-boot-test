package redisson.controller;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import redisson.utis.RedissonZSetService;

import org.redisson.client.protocol.ScoredEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
public class RedisZSetController {


    @Autowired
    private RedissonZSetService redissonZSetService;


    @GetMapping(value = "/redisson/zset/get/keys")
    public String getKeys(@RequestParam("prefix") String prefix) {
        Set<String> keys = redissonZSetService.getKeys(prefix + "*");
        log.info(JSON.toJSONString(keys));
        return JSON.toJSONString(keys);
    }


    @PostMapping(value = "/redisson/zset/add")
    public String add(@RequestParam("key") String key, @RequestParam("member") String member, @RequestParam("score") double score) {
        redissonZSetService.zscoreAddAsync(key, score, member, null);
        return "OK";
    }

    @GetMapping(value = "/redisson/zset/get/members")
    public String getZSetMembers(@RequestParam("key") String key) {
        Collection<Object> zSetMembers = redissonZSetService.getZSetMembers(key, 0, -1);
        log.info(JSON.toJSONString(zSetMembers));
        return JSON.toJSONString(zSetMembers);
    }

    @GetMapping(value = "/redisson/zset/get/entry")
    public String entryrange(@RequestParam("key") String key) {
        Collection<ScoredEntry<Object>> entryRange = redissonZSetService.getZSetEntryRange(key, 0, -1);
        log.info(JSON.toJSONString(entryRange));
        return JSON.toJSONString(entryRange);
    }

    @GetMapping(value = "/redisson/zset/get/member/score")
    public String getZSetMemberScore(@RequestParam("key") String key, @RequestParam("member") String member) {
        Double zSetMemberScore = redissonZSetService.getZSetMemberScore(key, member);
        log.info(JSON.toJSONString(zSetMemberScore));
        return JSON.toJSONString(zSetMemberScore);
    }

    @GetMapping(value = "/redisson/zset/get/member/rank")
    public String getZSetMemberRank(@RequestParam("key") String key, @RequestParam("member") String member) {
        Integer zSetMemberRank = redissonZSetService.getZSetMemberRank(key, member);
        log.info(JSON.toJSONString(zSetMemberRank));
        return JSON.toJSONString(zSetMemberRank);
    }

    @PostMapping(value = "/redisson/zset/remove/member")
    public String removeZSetMemberAsync(@RequestParam("key") String key, @RequestParam("member") String member) {
        redissonZSetService.removeZSetMemberAsync(key, member);
        return "ok";
    }

    @PostMapping(value = "/redisson/zset/remove/members")
    public String removeZSetMemberAsync2(@RequestParam("key") String key, @RequestParam String memberArr) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, memberArr.split(","));
        redissonZSetService.removeZSetMemberAsync(key, list);
        return "ok";
    }

    @GetMapping(value = "/redisson/zset/get/range/count")
    public String getZSetCountByScoresInclusive(@RequestParam("key") String key, @RequestParam("startScore") double startScore,
            @RequestParam("endScore") double endScore) {
        int zSetCountByScoresInclusive = redissonZSetService.getZSetCountByScoresInclusive(key, startScore, endScore);
        return zSetCountByScoresInclusive + "";
    }

    @GetMapping(value = "/redisson/zset/get/range/members")
    public String getZSetMembersByScoresInclusive(@RequestParam("key") String key, @RequestParam("startScore") double startScore,
            @RequestParam("endScore") double endScore) {
        Collection<Object> zSetMembersByScoresInclusive = redissonZSetService.getZSetMembersByScoresInclusive(key, startScore, endScore);
        return JSON.toJSONString(zSetMembersByScoresInclusive);
    }

}