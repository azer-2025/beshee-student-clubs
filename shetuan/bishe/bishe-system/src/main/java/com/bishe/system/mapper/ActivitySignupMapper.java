package com.bishe.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.bishe.system.domain.ActivitySignup;

/**
 * 活动报名 数据层
 */
public interface ActivitySignupMapper
{
    public ActivitySignup selectActivitySignupById(Long signupId);

    public List<ActivitySignup> selectActivitySignupList(ActivitySignup signup);

    public int insertActivitySignup(ActivitySignup signup);

    public int updateActivitySignup(ActivitySignup signup);

    public int deleteActivitySignupById(Long signupId);

    public int deleteActivitySignupByIds(Long[] signupIds);

    public int countActivitySignupByActivityIdAndUserId(@Param("activityId") Long activityId, @Param("userId") Long userId);
}
