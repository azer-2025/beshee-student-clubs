package com.bishe.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bishe.system.domain.ActivitySignup;
import com.bishe.system.mapper.ActivitySignupMapper;
import com.bishe.system.service.IActivitySignupService;

/**
 * 活动报名 服务层实现
 */
@Service
public class ActivitySignupServiceImpl implements IActivitySignupService
{
    @Autowired
    private ActivitySignupMapper activitySignupMapper;

    @Override
    public ActivitySignup selectActivitySignupById(Long signupId)
    {
        return activitySignupMapper.selectActivitySignupById(signupId);
    }

    @Override
    public List<ActivitySignup> selectActivitySignupList(ActivitySignup signup)
    {
        return activitySignupMapper.selectActivitySignupList(signup);
    }

    @Override
    public int insertActivitySignup(ActivitySignup signup)
    {
        return activitySignupMapper.insertActivitySignup(signup);
    }

    @Override
    public int updateActivitySignup(ActivitySignup signup)
    {
        return activitySignupMapper.updateActivitySignup(signup);
    }

    @Override
    public int deleteActivitySignupById(Long signupId)
    {
        return activitySignupMapper.deleteActivitySignupById(signupId);
    }

    @Override
    public int deleteActivitySignupByIds(Long[] signupIds)
    {
        return activitySignupMapper.deleteActivitySignupByIds(signupIds);
    }

    @Override
    public int approveActivitySignup(ActivitySignup signup)
    {
        return activitySignupMapper.updateActivitySignup(signup);
    }

    @Override
    public int countActivitySignupByActivityIdAndUserId(Long activityId, Long userId)
    {
        return activitySignupMapper.countActivitySignupByActivityIdAndUserId(activityId, userId);
    }
}
