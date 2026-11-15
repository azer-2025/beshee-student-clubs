package com.bishe.system.service;

import java.util.List;
import com.bishe.system.domain.ActivitySignup;

/**
 * 活动报名 服务层
 */
public interface IActivitySignupService
{
    public ActivitySignup selectActivitySignupById(Long signupId);

    public List<ActivitySignup> selectActivitySignupList(ActivitySignup signup);

    public int insertActivitySignup(ActivitySignup signup);

    public int updateActivitySignup(ActivitySignup signup);

    public int deleteActivitySignupById(Long signupId);

    public int deleteActivitySignupByIds(Long[] signupIds);

    /**
     * 审核报名
     */
    public int approveActivitySignup(ActivitySignup signup);

    public int countActivitySignupByActivityIdAndUserId(Long activityId, Long userId);
}
