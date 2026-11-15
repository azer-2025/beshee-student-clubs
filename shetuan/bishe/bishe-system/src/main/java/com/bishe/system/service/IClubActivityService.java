package com.bishe.system.service;

import java.util.List;
import com.bishe.system.domain.ClubActivity;

/**
 * 社团活动 服务层
 */
public interface IClubActivityService
{
    public ClubActivity selectClubActivityById(Long activityId);

    public List<ClubActivity> selectClubActivityList(ClubActivity activity);

    public int insertClubActivity(ClubActivity activity);

    public int updateClubActivity(ClubActivity activity);

    public int deleteClubActivityById(Long activityId);

    public int deleteClubActivityByIds(Long[] activityIds);

    /**
     * 审核活动
     */
    public int approveClubActivity(ClubActivity activity);
}

