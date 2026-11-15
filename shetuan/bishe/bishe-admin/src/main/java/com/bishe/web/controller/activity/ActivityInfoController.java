package com.bishe.web.controller.activity;

import java.util.ArrayList;
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
import com.bishe.common.annotation.Anonymous;
import com.bishe.common.annotation.Log;
import com.bishe.common.core.controller.BaseController;
import com.bishe.common.core.domain.AjaxResult;
import com.bishe.common.core.page.TableDataInfo;
import com.bishe.common.enums.BusinessType;
import com.bishe.common.utils.DateUtils;
import com.bishe.common.utils.SecurityUtils;
import com.bishe.system.domain.ClubActivity;
import com.bishe.system.service.IClubInfoService;
import com.bishe.system.service.IClubActivityService;

/**
 * 活动管理
 */
@RestController
@RequestMapping("/activity/info")
public class ActivityInfoController extends BaseController
{
    @Autowired
    private IClubActivityService clubActivityService;

    @Autowired
    private IClubInfoService clubInfoService;

    @PreAuthorize("@ss.hasPermi('activity:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(ClubActivity activity)
    {
        Long leaderClubId = resolveLeaderClubId();
        if (leaderClubId != null)
        {
            activity.setClubId(leaderClubId);
        }
        else if (isLeader())
        {
            return getDataTable(new ArrayList<ClubActivity>());
        }
        startPage();
        List<ClubActivity> list = clubActivityService.selectClubActivityList(activity);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('activity:approve:list')")
    @GetMapping("/approve/list")
    public TableDataInfo approveList(ClubActivity activity)
    {
        startPage();
        List<ClubActivity> list = clubActivityService.selectClubActivityList(activity);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('activity:info:query')")
    @GetMapping(value = "/{activityId}")
    public AjaxResult getInfo(@PathVariable Long activityId)
    {
        return success(clubActivityService.selectClubActivityById(activityId));
    }

    @PreAuthorize("@ss.hasPermi('activity:info:detail')")
    @GetMapping(value = "/detail/{activityId}")
    public AjaxResult detail(@PathVariable Long activityId)
    {
        return success(clubActivityService.selectClubActivityById(activityId));
    }

    @Anonymous
    @GetMapping("/public/list")
    public TableDataInfo publicList(ClubActivity activity)
    {
        activity.setStatus("1");
        startPage();
        List<ClubActivity> list = clubActivityService.selectClubActivityList(activity);
        return getDataTable(list);
    }

    @Anonymous
    @GetMapping("/public/detail/{activityId}")
    public AjaxResult publicDetail(@PathVariable Long activityId)
    {
        ClubActivity activity = clubActivityService.selectClubActivityById(activityId);
        if (activity == null || !"1".equals(activity.getStatus()))
        {
            return AjaxResult.error("活动不存在或未通过审核");
        }
        return success(activity);
    }

    @PreAuthorize("@ss.hasPermi('activity:info:add')")
    @Log(title = "活动", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ClubActivity activity)
    {
        if (isLeader())
        {
            Long leaderClubId = resolveLeaderClubId();
            if (leaderClubId == null)
            {
                return AjaxResult.error("暂无可管理社团");
            }
            if (activity.getClubId() == null)
            {
                activity.setClubId(leaderClubId);
            }
            else if (!leaderClubId.equals(activity.getClubId()))
            {
                return AjaxResult.error("无权限操作该社团活动");
            }
            activity.setStatus("0");
        }
        activity.setCreateBy(getUsername());
        return toAjax(clubActivityService.insertClubActivity(activity));
    }

    @PreAuthorize("@ss.hasPermi('activity:info:edit')")
    @Log(title = "活动", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ClubActivity activity)
    {
        if (!checkLeaderActivityPermission(activity.getActivityId()))
        {
            return AjaxResult.error("无权限操作该社团活动");
        }
        activity.setUpdateBy(getUsername());
        return toAjax(clubActivityService.updateClubActivity(activity));
    }

    @PreAuthorize("@ss.hasPermi('activity:info:remove')")
    @Log(title = "活动", businessType = BusinessType.DELETE)
    @DeleteMapping("/{activityIds}")
    public AjaxResult remove(@PathVariable Long[] activityIds)
    {
        if (isLeader())
        {
            for (Long activityId : activityIds)
            {
                if (!checkLeaderActivityPermission(activityId))
                {
                    return AjaxResult.error("无权限操作该社团活动");
                }
            }
        }
        return toAjax(clubActivityService.deleteClubActivityByIds(activityIds));
    }

    @PreAuthorize("@ss.hasPermi('activity:approve:approve')")
    @Log(title = "活动审核", businessType = BusinessType.UPDATE)
    @PutMapping("/approve")
    public AjaxResult approve(@RequestBody ClubActivity activity)
    {
        if (!checkLeaderActivityPermission(activity.getActivityId()))
        {
            return AjaxResult.error("无权限操作该社团活动");
        }
        activity.setAuditUserId(getUserId());
        activity.setAuditTime(DateUtils.getNowDate());
        activity.setUpdateBy(getUsername());
        return toAjax(clubActivityService.approveClubActivity(activity));
    }

    private boolean isLeader()
    {
        return !SecurityUtils.isAdmin() && SecurityUtils.hasRole("leader");
    }

    private Long resolveLeaderClubId()
    {
        if (!isLeader())
        {
            return null;
        }
        return clubInfoService.selectClubIdByLeaderUserId(getUserId());
    }

    private boolean checkLeaderActivityPermission(Long activityId)
    {
        if (!isLeader())
        {
            return true;
        }
        Long leaderClubId = resolveLeaderClubId();
        if (leaderClubId == null || activityId == null)
        {
            return false;
        }
        ClubActivity current = clubActivityService.selectClubActivityById(activityId);
        return current != null && leaderClubId.equals(current.getClubId());
    }
}
