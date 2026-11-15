package com.bishe.system.mapper;

import java.util.List;
import java.util.Map;

public interface StatsMapper
{
    public int countClubTotal();

    public int countClubPending();

    public int countMemberTotal();

    public int countActivityTotal();

    public int countActivityPending();

    public int countSignupTotal();

    public int countFundingPending();

    public int countVenuePending();

    public List<Map<String, Object>> selectTopClubs();

    public List<Map<String, Object>> selectClubCategories();

    public List<Map<String, Object>> selectActivityTrends();
}
