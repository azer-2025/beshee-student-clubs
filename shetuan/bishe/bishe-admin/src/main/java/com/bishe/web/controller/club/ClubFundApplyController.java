package com.bishe.web.controller.club;

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
import com.bishe.system.domain.ClubFundApply;
import com.bishe.system.service.IClubInfoService;
import com.bishe.system.service.IClubFundApplyService;

/**
 * 经费申请管理
 */
@RestController
@RequestMapping("/club/fund")
public class ClubFundApplyController extends BaseController
{
    @Autowired
    private IClubFundApplyService clubFundApplyService;

    @Autowired
    private IClubInfoService clubInfoService;

    @PreAuthorize("@ss.hasPermi('club:fund:list')")
    @GetMapping("/list")
    public TableDataInfo list(ClubFundApply fund)
    {
        Long leaderClubId = resolveLeaderClubId();
        if (leaderClubId != null)
        {
            fund.setClubId(leaderClubId);
        }
        else if (isLeader())
        {
            return getDataTable(new ArrayList<ClubFundApply>());
        }
        startPage();
        List<ClubFundApply> list = clubFundApplyService.selectClubFundApplyList(fund);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('club:fund:query')")
    @GetMapping(value = "/{fundId}")
    public AjaxResult getInfo(@PathVariable Long fundId)
    {
        return success(clubFundApplyService.selectClubFundApplyById(fundId));
    }

    @PreAuthorize("@ss.hasPermi('club:fund:apply')")
    @Log(title = "经费申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ClubFundApply fund)
    {
        if (isLeader())
        {
            Long leaderClubId = resolveLeaderClubId();
            if (leaderClubId == null)
            {
                return AjaxResult.error("暂无可管理社团");
            }
            if (fund.getClubId() == null)
            {
                fund.setClubId(leaderClubId);
            }
            else if (!leaderClubId.equals(fund.getClubId()))
            {
                return AjaxResult.error("无权限操作该经费申请");
            }
        }
        if (fund.getApplyUserId() == null)
        {
            fund.setApplyUserId(getUserId());
        }
        fund.setApplyTime(DateUtils.getNowDate());
        if (fund.getStatus() == null || "".equals(fund.getStatus()))
        {
            fund.setStatus("0");
        }
        fund.setCreateBy(getUsername());
        return toAjax(clubFundApplyService.insertClubFundApply(fund));
    }

    @PreAuthorize("@ss.hasPermi('club:fund:edit')")
    @Log(title = "经费申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ClubFundApply fund)
    {
        if (!checkLeaderFundPermission(fund.getFundId()))
        {
            return AjaxResult.error("无权限操作该经费申请");
        }
        fund.setUpdateBy(getUsername());
        return toAjax(clubFundApplyService.updateClubFundApply(fund));
    }

    @PreAuthorize("@ss.hasPermi('club:fund:remove')")
    @Log(title = "经费申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{fundIds}")
    public AjaxResult remove(@PathVariable Long[] fundIds)
    {
        if (isLeader())
        {
            for (Long fundId : fundIds)
            {
                if (!checkLeaderFundPermission(fundId))
                {
                    return AjaxResult.error("无权限操作该经费申请");
                }
            }
        }
        return toAjax(clubFundApplyService.deleteClubFundApplyByIds(fundIds));
    }

    @PreAuthorize("@ss.hasPermi('club:fund:approve')")
    @Log(title = "经费申请审核", businessType = BusinessType.UPDATE)
    @PutMapping("/approve")
    public AjaxResult approve(@RequestBody ClubFundApply fund)
    {
        if (!checkLeaderFundPermission(fund.getFundId()))
        {
            return AjaxResult.error("无权限操作该经费申请");
        }
        fund.setAuditUserId(getUserId());
        fund.setAuditTime(DateUtils.getNowDate());
        fund.setUpdateBy(getUsername());
        return toAjax(clubFundApplyService.approveClubFundApply(fund));
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

    private boolean checkLeaderFundPermission(Long fundId)
    {
        if (!isLeader())
        {
            return true;
        }
        Long leaderClubId = resolveLeaderClubId();
        if (leaderClubId == null || fundId == null)
        {
            return false;
        }
        ClubFundApply current = clubFundApplyService.selectClubFundApplyById(fundId);
        return current != null && leaderClubId.equals(current.getClubId());
    }
}
