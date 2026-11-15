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
import com.bishe.common.annotation.Log;
import com.bishe.common.core.controller.BaseController;
import com.bishe.common.core.domain.AjaxResult;
import com.bishe.common.core.page.TableDataInfo;
import com.bishe.common.enums.BusinessType;
import com.bishe.common.utils.DateUtils;
import com.bishe.common.utils.SecurityUtils;
import com.bishe.system.domain.VenueApply;
import com.bishe.system.service.IClubInfoService;
import com.bishe.system.service.IVenueApplyService;

/**
 * 场地申请管理
 */
@RestController
@RequestMapping("/activity/venue")
public class VenueApplyController extends BaseController
{
    @Autowired
    private IVenueApplyService venueApplyService;

    @Autowired
    private IClubInfoService clubInfoService;

    @PreAuthorize("@ss.hasPermi('activity:venue:list')")
    @GetMapping("/list")
    public TableDataInfo list(VenueApply venueApply)
    {
        Long leaderClubId = resolveLeaderClubId();
        if (leaderClubId != null)
        {
            venueApply.setClubId(leaderClubId);
        }
        else if (isLeader())
        {
            return getDataTable(new ArrayList<VenueApply>());
        }
        startPage();
        List<VenueApply> list = venueApplyService.selectVenueApplyList(venueApply);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('activity:venue:query')")
    @GetMapping(value = "/{venueApplyId}")
    public AjaxResult getInfo(@PathVariable Long venueApplyId)
    {
        return success(venueApplyService.selectVenueApplyById(venueApplyId));
    }

    @PreAuthorize("@ss.hasPermi('activity:venue:apply')")
    @Log(title = "场地申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VenueApply venueApply)
    {
        if (isLeader())
        {
            Long leaderClubId = resolveLeaderClubId();
            if (leaderClubId == null)
            {
                return AjaxResult.error("暂无可管理社团");
            }
            if (venueApply.getClubId() == null)
            {
                venueApply.setClubId(leaderClubId);
            }
            else if (!leaderClubId.equals(venueApply.getClubId()))
            {
                return AjaxResult.error("无权限操作该场地申请");
            }
        }
        if (venueApply.getApplyUserId() == null)
        {
            venueApply.setApplyUserId(getUserId());
        }
        venueApply.setApplyTime(DateUtils.getNowDate());
        if (venueApply.getStatus() == null || "".equals(venueApply.getStatus()))
        {
            venueApply.setStatus("0");
        }
        venueApply.setCreateBy(getUsername());
        return toAjax(venueApplyService.insertVenueApply(venueApply));
    }

    @PreAuthorize("@ss.hasPermi('activity:venue:edit')")
    @Log(title = "场地申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VenueApply venueApply)
    {
        if (!checkLeaderVenuePermission(venueApply.getVenueApplyId()))
        {
            return AjaxResult.error("无权限操作该场地申请");
        }
        venueApply.setUpdateBy(getUsername());
        return toAjax(venueApplyService.updateVenueApply(venueApply));
    }

    @PreAuthorize("@ss.hasPermi('activity:venue:remove')")
    @Log(title = "场地申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{venueApplyIds}")
    public AjaxResult remove(@PathVariable Long[] venueApplyIds)
    {
        if (isLeader())
        {
            for (Long venueApplyId : venueApplyIds)
            {
                if (!checkLeaderVenuePermission(venueApplyId))
                {
                    return AjaxResult.error("无权限操作该场地申请");
                }
            }
        }
        return toAjax(venueApplyService.deleteVenueApplyByIds(venueApplyIds));
    }

    @PreAuthorize("@ss.hasPermi('activity:venue:approve')")
    @Log(title = "场地申请审核", businessType = BusinessType.UPDATE)
    @PutMapping("/approve")
    public AjaxResult approve(@RequestBody VenueApply venueApply)
    {
        if (!checkLeaderVenuePermission(venueApply.getVenueApplyId()))
        {
            return AjaxResult.error("无权限操作该场地申请");
        }
        venueApply.setAuditUserId(getUserId());
        venueApply.setAuditTime(DateUtils.getNowDate());
        venueApply.setUpdateBy(getUsername());
        return toAjax(venueApplyService.approveVenueApply(venueApply));
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

    private boolean checkLeaderVenuePermission(Long venueApplyId)
    {
        if (!isLeader())
        {
            return true;
        }
        Long leaderClubId = resolveLeaderClubId();
        if (leaderClubId == null || venueApplyId == null)
        {
            return false;
        }
        VenueApply current = venueApplyService.selectVenueApplyById(venueApplyId);
        return current != null && leaderClubId.equals(current.getClubId());
    }
}
