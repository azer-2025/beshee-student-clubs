package com.bishe.web.controller.activity;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bishe.common.annotation.Log;
import com.bishe.common.core.controller.BaseController;
import com.bishe.common.core.domain.AjaxResult;
import com.bishe.common.core.page.TableDataInfo;
import com.bishe.common.enums.BusinessType;
import com.bishe.common.utils.DateUtils;
import com.bishe.system.domain.ActivitySignup;
import com.bishe.system.service.IActivitySignupService;

/**
 * 活动报名管理
 */
@RestController
@RequestMapping("/activity/signup")
public class ActivitySignupController extends BaseController
{
    @Autowired
    private IActivitySignupService activitySignupService;

    @PreAuthorize("@ss.hasPermi('activity:signup:list')")
    @GetMapping("/list")
    public TableDataInfo list(ActivitySignup signup)
    {
        startPage();
        List<ActivitySignup> list = activitySignupService.selectActivitySignupList(signup);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('activity:signup:my')")
    @GetMapping("/my")
    public TableDataInfo my(ActivitySignup signup)
    {
        signup.setUserId(getUserId());
        startPage();
        List<ActivitySignup> list = activitySignupService.selectActivitySignupList(signup);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('activity:signup:query')")
    @GetMapping(value = "/{signupId}")
    public AjaxResult getInfo(@PathVariable Long signupId)
    {
        return success(activitySignupService.selectActivitySignupById(signupId));
    }

    @PreAuthorize("@ss.hasPermi('activity:signup:add')")
    @Log(title = "活动报名", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ActivitySignup signup)
    {
        if (signup.getActivityId() == null)
        {
            return AjaxResult.error("请选择活动");
        }
        if (signup.getUserId() == null)
        {
            signup.setUserId(getUserId());
        }
        if (activitySignupService.countActivitySignupByActivityIdAndUserId(signup.getActivityId(), signup.getUserId()) > 0)
        {
            return AjaxResult.error("已报名该活动");
        }
        if (signup.getStatus() == null || "".equals(signup.getStatus()))
        {
            signup.setStatus("0");
        }
        if (signup.getCheckinStatus() == null || "".equals(signup.getCheckinStatus()))
        {
            signup.setCheckinStatus("0");
        }
        signup.setCreateBy(getUsername());
        if (signup.getSignupTime() == null)
        {
            signup.setSignupTime(DateUtils.getNowDate());
        }
        return toAjax(activitySignupService.insertActivitySignup(signup));
    }

    @PreAuthorize("@ss.hasPermi('activity:signup:edit')")
    @Log(title = "活动报名", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ActivitySignup signup)
    {
        signup.setUpdateBy(getUsername());
        return toAjax(activitySignupService.updateActivitySignup(signup));
    }

    @PreAuthorize("@ss.hasPermi('activity:signup:remove')")
    @Log(title = "活动报名", businessType = BusinessType.DELETE)
    @DeleteMapping("/{signupIds}")
    public AjaxResult remove(@PathVariable Long[] signupIds)
    {
        return toAjax(activitySignupService.deleteActivitySignupByIds(signupIds));
    }

    @PreAuthorize("@ss.hasPermi('activity:signup:approve')")
    @Log(title = "活动报名审核", businessType = BusinessType.UPDATE)
    @PutMapping("/approve")
    public AjaxResult approve(@RequestBody ActivitySignup signup)
    {
        signup.setAuditUserId(getUserId());
        signup.setAuditTime(DateUtils.getNowDate());
        signup.setUpdateBy(getUsername());
        return toAjax(activitySignupService.approveActivitySignup(signup));
    }
}
