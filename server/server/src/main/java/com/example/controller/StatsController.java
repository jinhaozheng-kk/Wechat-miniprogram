package com.example.controller;

import com.example.common.Result;
import com.example.dto.StatsResponse;
import com.example.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台首页统计数据接口
 */
@RestController
@RequestMapping("/api/admin/stats")
public class StatsController {

    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    /** 首页 dashboard 一次性拉取所有数据 */
    @GetMapping
    public Result<StatsResponse> summary() {
        return Result.ok(statsService.summary());
    }
}
