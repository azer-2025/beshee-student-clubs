package com.bishe.system.mapper;

import java.util.List;
import com.bishe.system.domain.ClubActivity;

/**
 * 社团活动 数据层
 */
public interface ClubActivityMapper
{
    public ClubActivity selectClubActivityById(Long activityId);

    public List<ClubActivity> selectClubActivityList(ClubActivity activity);

    public int insertClubActivity(ClubActivity activity);

    public int updateClubActivity(ClubActivity activity);

    public int deleteClubActivityById(Long activityId);

    public int deleteClubActivityByIds(Long[] activityIds);
}

