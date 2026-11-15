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
import com.bishe.common.utils.SecurityUtils;
import com.bishe.system.domain.ClubNotice;
import com.bishe.system.service.IClubInfoService;
import com.bishe.system.service.IClubNoticeService;

/**
 * 社团公告管理
 */
@RestController
@RequestMapping("/club/notice")
public class ClubNoticeController extends BaseController
{
    @Autowired
    private IClubNoticeService clubNoticeService;

    @Autowired
    private IClubInfoService clubInfoService;

    @PreAuthorize("@ss.hasPermi('club:notice:list')")
    @GetMapping("/list")
    public TableDataInfo list(ClubNotice notice)
    {
        Long leaderClubId = resolveLeaderClubId();
        if (leaderClubId != null)
        {
            notice.setClubId(leaderClubId);
        }
        else if (isLeader())
        {
            return getDataTable(new ArrayList<ClubNotice>());
        }
        startPage();
        List<ClubNotice> list = clubNoticeService.selectClubNoticeList(notice);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('club:notice:query')")
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable Long noticeId)
    {
        return success(clubNoticeService.selectClubNoticeById(noticeId));
    }

    @PreAuthorize("@ss.hasPermi('club:notice:add')")
    @Log(title = "社团公告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ClubNotice notice)
    {
        if (isLeader())
        {
            Long leaderClubId = resolveLeaderClubId();
            if (leaderClubId == null)
            {
                return AjaxResult.error("暂无可管理社团");
            }
            if (notice.getClubId() == null)
            {
                notice.setClubId(leaderClubId);
            }
            else if (!leaderClubId.equals(notice.getClubId()))
            {
                return AjaxResult.error("无权限操作该社团公告");
            }
        }
        notice.setCreateBy(getUsername());
        return toAjax(clubNoticeService.insertClubNotice(notice));
    }

    @PreAuthorize("@ss.hasPermi('club:notice:edit')")
    @Log(title = "社团公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ClubNotice notice)
    {
        if (!checkLeaderNoticePermission(notice.getNoticeId()))
        {
            return AjaxResult.error("无权限操作该社团公告");
        }
        notice.setUpdateBy(getUsername());
        return toAjax(clubNoticeService.updateClubNotice(notice));
    }

    @PreAuthorize("@ss.hasPermi('club:notice:remove')")
    @Log(title = "社团公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public AjaxResult remove(@PathVariable Long[] noticeIds)
    {
        if (isLeader())
        {
            for (Long noticeId : noticeIds)
            {
                if (!checkLeaderNoticePermission(noticeId))
                {
                    return AjaxResult.error("无权限操作该社团公告");
                }
            }
        }
        return toAjax(clubNoticeService.deleteClubNoticeByIds(noticeIds));
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

    private boolean checkLeaderNoticePermission(Long noticeId)
    {
        if (!isLeader())
        {
            return true;
        }
        Long leaderClubId = resolveLeaderClubId();
        if (leaderClubId == null || noticeId == null)
        {
            return false;
        }
        ClubNotice current = clubNoticeService.selectClubNoticeById(noticeId);
        return current != null && leaderClubId.equals(current.getClubId());
    }
}
