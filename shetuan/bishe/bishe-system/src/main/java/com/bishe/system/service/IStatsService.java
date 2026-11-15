package com.bishe.system.service;

import java.util.Map;

public interface IStatsService
{
    public Map<String, Object> getOverview();

    public Map<String, Object> getChartData();
}
