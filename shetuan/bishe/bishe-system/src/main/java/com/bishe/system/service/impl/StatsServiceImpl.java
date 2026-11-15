package com.bishe.system.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bishe.system.mapper.StatsMapper;
import com.bishe.system.service.IStatsService;

@Service
public class StatsServiceImpl implements IStatsService
{
    @Autowired
    private StatsMapper statsMapper;

    @Override
    public Map<String, Object> getOverview()
    {
        Map<String, Object> data = new HashMap<String, Object>(16);
        data.put("clubTotal", statsMapper.countClubTotal());
        data.put("clubPending", statsMapper.countClubPending());
        data.put("memberTotal", statsMapper.countMemberTotal());
        data.put("activityTotal", statsMapper.countActivityTotal());
        data.put("activityPending", statsMapper.countActivityPending());
        data.put("signupTotal", statsMapper.countSignupTotal());
        data.put("fundingPending", statsMapper.countFundingPending());
        data.put("venuePending", statsMapper.countVenuePending());
        return data;
    }

    @Override
    public Map<String, Object> getChartData()
    {
        Map<String, Object> data = new HashMap<>();
        data.put("topClubs", statsMapper.selectTopClubs());
        data.put("categories", statsMapper.selectClubCategories());
        
        List<Map<String, Object>> trends = statsMapper.selectActivityTrends();
        Collections.reverse(trends);
        data.put("trends", trends);
        
        return data;
    }
}
