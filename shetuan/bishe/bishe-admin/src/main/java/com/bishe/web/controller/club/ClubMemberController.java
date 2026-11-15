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
import com.bishe.system.domain.ClubMember;
import com.bishe.system.service.IClubInfoService;
import com.bishe.system.service.IClubMemberService;

/**
 * 社团成员管理
 */
@RestController
@RequestMapping("/club/member")
public class ClubMemberController extends BaseController
{
    @Autowired
    private IClubMemberService clubMemberService;

    @Autowired
    private IClubInfoService clubInfoService;

    @PreAuthorize("@ss.hasPermi('club:member:list')")
    @GetMapping("/list")
    public TableDataInfo list(ClubMember clubMember)
    {
        Long leaderClubId = resolveLeaderClubId();
        if (leaderClubId != null)
        {
            clubMember.setClubId(leaderClubId);
        }
        else if (isLeader())
        {
            return getDataTable(new ArrayList<ClubMember>());
        }
        startPage();
        List<ClubMember> list = clubMemberService.selectClubMemberList(clubMember);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('club:member:my')")
    @GetMapping("/my")
    public TableDataInfo myList(ClubMember clubMember)
    {
        clubMember.setUserId(getUserId());
        startPage();
        List<ClubMember> list = clubMemberService.selectClubMemberList(clubMember);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('club:member:apply')")
    @Log(title = "入社申请", businessType = BusinessType.INSERT)
    @PostMapping("/apply")
    public AjaxResult apply(@RequestBody ClubMember clubMember)
    {
        if (clubMember.getClubId() == null)
        {
            return AjaxResult.error("请选择社团");
        }
        if (clubMemberService.countClubMemberByClubIdAndUserId(clubMember.getClubId(), getUserId()) > 0)
        {
            return AjaxResult.error("已提交申请，请勿重复申请");
        }
        clubMember.setUserId(getUserId());
        clubMember.setMemberRole("member");
        clubMember.setStatus("0");
        clubMember.setApplyTime(DateUtils.getNowDate());
        clubMember.setCreateBy(getUsername());
        return toAjax(clubMemberService.insertClubMember(clubMember));
    }

    @PreAuthorize("@ss.hasPermi('club:member:approve')")
    @Log(title = "入社审核", businessType = BusinessType.UPDATE)
    @PutMapping("/approve")
    public AjaxResult approve(@RequestBody ClubMember clubMember)
    {
        if (!checkLeaderClubPermission(clubMember.getMemberId()))
        {
            return AjaxResult.error("无权限操作该成员");
        }
        clubMember.setAuditUserId(getUserId());
        clubMember.setAuditTime(DateUtils.getNowDate());
        clubMember.setUpdateBy(getUsername());
        return toAjax(clubMemberService.approveClubMember(clubMember));
    }

    @PreAuthorize("@ss.hasPermi('club:member:edit')")
    @Log(title = "社团成员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ClubMember clubMember)
    {
        if (!checkLeaderClubPermission(clubMember.getMemberId()))
        {
            return AjaxResult.error("无权限操作该成员");
        }
        clubMember.setUpdateBy(getUsername());
        return toAjax(clubMemberService.updateClubMember(clubMember));
    }

    @PreAuthorize("@ss.hasPermi('club:member:remove')")
    @Log(title = "社团成员", businessType = BusinessType.DELETE)
    @DeleteMapping("/{memberIds}")
    public AjaxResult remove(@PathVariable Long[] memberIds)
    {
        if (isLeader())
        {
            for (Long memberId : memberIds)
            {
                if (!checkLeaderClubPermission(memberId))
                {
                    return AjaxResult.error("无权限操作该成员");
                }
            }
        }
        return toAjax(clubMemberService.deleteClubMemberByIds(memberIds));
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

    private boolean checkLeaderClubPermission(Long memberId)
    {
        if (!isLeader())
        {
            return true;
        }
        Long leaderClubId = resolveLeaderClubId();
        if (leaderClubId == null || memberId == null)
        {
            return false;
        }
        ClubMember current = clubMemberService.selectClubMemberById(memberId);
        return current != null && leaderClubId.equals(current.getClubId());
    }
}
