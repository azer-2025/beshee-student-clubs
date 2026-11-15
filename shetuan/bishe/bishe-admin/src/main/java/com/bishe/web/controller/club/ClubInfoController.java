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
import com.bishe.common.annotation.Anonymous;
import com.bishe.common.annotation.Log;
import com.bishe.common.core.controller.BaseController;
import com.bishe.common.core.domain.AjaxResult;
import com.bishe.common.core.page.TableDataInfo;
import com.bishe.common.enums.BusinessType;
import com.bishe.common.utils.DateUtils;
import com.bishe.common.utils.SecurityUtils;
import com.bishe.common.utils.StringUtils;
import com.bishe.system.domain.ClubInfo;
import com.bishe.system.service.IClubInfoService;

/**
 * 社团信息管理
 */
@RestController
@RequestMapping("/club/info")
public class ClubInfoController extends BaseController
{
    @Autowired
    private IClubInfoService clubInfoService;

    @PreAuthorize("@ss.hasPermi('club:info:list') || @ss.hasRole('leader')")
    @GetMapping("/list")
    public TableDataInfo list(ClubInfo clubInfo)
    {
        if (!SecurityUtils.isAdmin() && SecurityUtils.hasRole("leader"))
        {
            Long leaderClubId = clubInfoService.selectClubIdByLeaderUserId(getUserId());
            if (leaderClubId == null)
            {
                return getDataTable(new ArrayList<ClubInfo>());
            }
            clubInfo.setLeaderUserId(getUserId());
        }
        startPage();
        List<ClubInfo> list = clubInfoService.selectClubInfoList(clubInfo);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('club:apply:list')")
    @GetMapping("/apply/list")
    public TableDataInfo applyList(ClubInfo clubInfo)
    {
        if (clubInfo.getApplyStatus() == null || "".equals(clubInfo.getApplyStatus()))
        {
            clubInfo.setApplyStatus("0");
        }
        startPage();
        List<ClubInfo> list = clubInfoService.selectClubInfoList(clubInfo);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('club:info:query') || @ss.hasRole('leader')")
    @GetMapping(value = "/{clubId}")
    public AjaxResult getInfo(@PathVariable Long clubId)
    {
        if (!checkLeaderClubPermission(clubId))
        {
            return AjaxResult.error("无权限查看该社团");
        }
        return success(clubInfoService.selectClubInfoById(clubId));
    }

    @PreAuthorize("@ss.hasPermi('club:info:detail')")
    @GetMapping(value = "/detail/{clubId}")
    public AjaxResult detail(@PathVariable Long clubId)
    {
        return success(clubInfoService.selectClubInfoById(clubId));
    }

    @Anonymous
    @GetMapping("/public/list")
    public TableDataInfo publicList(ClubInfo clubInfo)
    {
        clubInfo.setApplyStatus("1");
        clubInfo.setStatus("0");
        startPage();
        List<ClubInfo> list = clubInfoService.selectClubInfoList(clubInfo);
        return getDataTable(list);
    }

    @Anonymous
    @GetMapping("/public/detail/{clubId}")
    public AjaxResult publicDetail(@PathVariable Long clubId)
    {
        ClubInfo info = clubInfoService.selectClubInfoById(clubId);
        if (info == null || !"1".equals(info.getApplyStatus()) || !"0".equals(info.getStatus()))
        {
            return AjaxResult.error("社团不存在或未通过审核");
        }
        return success(info);
    }

    @PreAuthorize("@ss.hasPermi('club:apply:my')")
    @GetMapping("/my/apply")
    public TableDataInfo myApply(ClubInfo clubInfo)
    {
        clubInfo.setApplyUserId(getUserId());
        startPage();
        List<ClubInfo> list = clubInfoService.selectClubInfoList(clubInfo);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('club:info:add')")
    @Log(title = "社团信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ClubInfo clubInfo)
    {
        clubInfo.setCreateBy(getUsername());
        return toAjax(clubInfoService.insertClubInfo(clubInfo));
    }

    @PreAuthorize("@ss.hasPermi('club:info:edit') || @ss.hasRole('leader')")
    @Log(title = "社团信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ClubInfo clubInfo)
    {
        if (!checkLeaderClubPermission(clubInfo.getClubId()))
        {
            return AjaxResult.error("无权限操作该社团");
        }
        clubInfo.setUpdateBy(getUsername());
        return toAjax(clubInfoService.updateClubInfo(clubInfo));
    }

    @PreAuthorize("@ss.hasPermi('club:info:remove') || @ss.hasRole('leader')")
    @Log(title = "社团信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{clubIds}")
    public AjaxResult remove(@PathVariable Long[] clubIds)
    {
        if (isLeader())
        {
            for (Long clubId : clubIds)
            {
                if (!checkLeaderClubPermission(clubId))
                {
                    return AjaxResult.error("无权限操作该社团");
                }
            }
        }
        return toAjax(clubInfoService.deleteClubInfoByIds(clubIds));
    }

    @PreAuthorize("@ss.hasPermi('club:apply:apply')")
    @Log(title = "社团申请", businessType = BusinessType.INSERT)
    @PostMapping("/apply")
    public AjaxResult apply(@RequestBody ClubInfo clubInfo)
    {
        Long userId = getUserId();
        clubInfo.setApplyUserId(userId);
        if (clubInfo.getLeaderUserId() == null)
        {
            clubInfo.setLeaderUserId(userId);
        }
        clubInfo.setApplyStatus("0");
        if (StringUtils.isEmpty(clubInfo.getTeacherAuditStatus()))
        {
            clubInfo.setTeacherAuditStatus("0");
        }
        clubInfo.setApplyTime(DateUtils.getNowDate());
        clubInfo.setStatus("0");
        clubInfo.setCreateBy(getUsername());
        return toAjax(clubInfoService.insertClubInfo(clubInfo));
    }

    @PreAuthorize("@ss.hasPermi('club:apply:teacher')")
    @Log(title = "指导老师审核", businessType = BusinessType.UPDATE)
    @PutMapping("/teacher/approve")
    public AjaxResult teacherApprove(@RequestBody ClubInfo clubInfo)
    {
        if (StringUtils.isEmpty(clubInfo.getTeacherAuditStatus()))
        {
            return AjaxResult.error("请选择审核结果");
        }
        if (clubInfo.getTeacherUserId() == null)
        {
            clubInfo.setTeacherUserId(getUserId());
        }
        clubInfo.setTeacherAuditTime(DateUtils.getNowDate());
        clubInfo.setUpdateBy(getUsername());
        return toAjax(clubInfoService.updateClubInfo(clubInfo));
    }

    @PreAuthorize("@ss.hasPermi('club:apply:approve')")
    @Log(title = "社团审核", businessType = BusinessType.UPDATE)
    @PutMapping("/approve")
    public AjaxResult approve(@RequestBody ClubInfo clubInfo)
    {
        if ("1".equals(clubInfo.getApplyStatus()))
        {
            String teacherStatus = clubInfo.getTeacherAuditStatus();
            if (StringUtils.isEmpty(teacherStatus))
            {
                ClubInfo current = clubInfoService.selectClubInfoById(clubInfo.getClubId());
                teacherStatus = current == null ? null : current.getTeacherAuditStatus();
            }
            if (!"1".equals(teacherStatus))
            {
                return AjaxResult.error("请先完成指导老师审核");
            }
        }
        clubInfo.setAuditUserId(getUserId());
        clubInfo.setAuditTime(DateUtils.getNowDate());
        clubInfo.setUpdateBy(getUsername());
        if ("1".equals(clubInfo.getApplyStatus()) && (clubInfo.getStatus() == null || "".equals(clubInfo.getStatus())))
        {
            clubInfo.setStatus("0");
        }
        return toAjax(clubInfoService.approveClub(clubInfo));
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

    private boolean checkLeaderClubPermission(Long clubId)
    {
        if (!isLeader())
        {
            return true;
        }
        Long leaderClubId = resolveLeaderClubId();
        return leaderClubId != null && clubId != null && leaderClubId.equals(clubId);
    }
}
