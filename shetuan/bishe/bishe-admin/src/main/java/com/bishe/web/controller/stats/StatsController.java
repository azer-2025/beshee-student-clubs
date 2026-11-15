package com.bishe.web.controller.stats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bishe.common.core.controller.BaseController;
import com.bishe.common.core.domain.AjaxResult;
import com.bishe.system.service.IStatsService;

/**
 * 数据统计
 */
@RestController
@RequestMapping("/stats")
public class StatsController extends BaseController
{
    @Autowired
    private IStatsService statsService;

    @GetMapping("/overview")
    public AjaxResult overview()
    {
        return success(statsService.getOverview());
    }

    @GetMapping("/chart")
    public AjaxResult chart()
    {
        return success(statsService.getChartData());
    }
}
