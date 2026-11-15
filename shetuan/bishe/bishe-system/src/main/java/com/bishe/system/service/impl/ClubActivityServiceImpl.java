package com.bishe.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bishe.system.domain.ClubActivity;
import com.bishe.system.mapper.ClubActivityMapper;
import com.bishe.system.service.IClubActivityService;

/**
 * 社团活动 服务层实现
 */
@Service
public class ClubActivityServiceImpl implements IClubActivityService
{
    @Autowired
    private ClubActivityMapper clubActivityMapper;

    @Override
    public ClubActivity selectClubActivityById(Long activityId)
    {
        return clubActivityMapper.selectClubActivityById(activityId);
    }

    @Override
    public List<ClubActivity> selectClubActivityList(ClubActivity activity)
    {
        return clubActivityMapper.selectClubActivityList(activity);
    }

    @Override
    public int insertClubActivity(ClubActivity activity)
    {
        return clubActivityMapper.insertClubActivity(activity);
    }

    @Override
    public int updateClubActivity(ClubActivity activity)
    {
        return clubActivityMapper.updateClubActivity(activity);
    }

    @Override
    public int deleteClubActivityById(Long activityId)
    {
        return clubActivityMapper.deleteClubActivityById(activityId);
    }

    @Override
    public int deleteClubActivityByIds(Long[] activityIds)
    {
        return clubActivityMapper.deleteClubActivityByIds(activityIds);
    }

    @Override
    public int approveClubActivity(ClubActivity activity)
    {
        return clubActivityMapper.updateClubActivity(activity);
    }
}

