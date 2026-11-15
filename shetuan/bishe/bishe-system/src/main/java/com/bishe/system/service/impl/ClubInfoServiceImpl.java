package com.bishe.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bishe.common.utils.DateUtils;
import com.bishe.system.domain.ClubInfo;
import com.bishe.system.domain.ClubMember;
import com.bishe.system.domain.SysUserRole;
import com.bishe.system.mapper.ClubInfoMapper;
import com.bishe.system.mapper.ClubMemberMapper;
import com.bishe.system.mapper.SysUserRoleMapper;
import com.bishe.system.service.IClubInfoService;

/**
 * 社团信息 服务层实现
 */
@Service
public class ClubInfoServiceImpl implements IClubInfoService
{
    @Autowired
    private ClubInfoMapper clubInfoMapper;

    @Autowired
    private ClubMemberMapper clubMemberMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Override
    public ClubInfo selectClubInfoById(Long clubId)
    {
        return clubInfoMapper.selectClubInfoById(clubId);
    }

    @Override
    public List<ClubInfo> selectClubInfoList(ClubInfo clubInfo)
    {
        return clubInfoMapper.selectClubInfoList(clubInfo);
    }

    @Override
    public int insertClubInfo(ClubInfo clubInfo)
    {
        return clubInfoMapper.insertClubInfo(clubInfo);
    }

    @Override
    public int updateClubInfo(ClubInfo clubInfo)
    {
        return clubInfoMapper.updateClubInfo(clubInfo);
    }

    @Override
    public int deleteClubInfoById(Long clubId)
    {
        return clubInfoMapper.deleteClubInfoById(clubId);
    }

    @Override
    public int deleteClubInfoByIds(Long[] clubIds)
    {
        return clubInfoMapper.deleteClubInfoByIds(clubIds);
    }

    @Override
    public int approveClub(ClubInfo clubInfo)
    {
        int rows = clubInfoMapper.updateClubInfo(clubInfo);
        if (rows <= 0)
        {
            return rows;
        }
        if (!"1".equals(clubInfo.getApplyStatus()))
        {
            return rows;
        }

        ClubInfo current = clubInfoMapper.selectClubInfoById(clubInfo.getClubId());
        Long leaderUserId = clubInfo.getLeaderUserId();
        if (leaderUserId == null && current != null)
        {
            leaderUserId = current.getLeaderUserId();
            if (leaderUserId == null)
            {
                leaderUserId = current.getApplyUserId();
            }
        }
        if (leaderUserId == null)
        {
            return rows;
        }

        if (userRoleMapper.countUserRoleByUserIdRoleId(leaderUserId, 3L) == 0)
        {
            List<SysUserRole> list = new ArrayList<SysUserRole>(1);
            SysUserRole ur = new SysUserRole();
            ur.setUserId(leaderUserId);
            ur.setRoleId(3L);
            list.add(ur);
            userRoleMapper.batchUserRole(list);
        }

        if (clubMemberMapper.countClubMemberByClubIdAndUserId(clubInfo.getClubId(), leaderUserId) == 0)
        {
            ClubMember member = new ClubMember();
            member.setClubId(clubInfo.getClubId());
            member.setUserId(leaderUserId);
            member.setMemberRole("leader");
            member.setStatus("1");
            member.setApplyTime(DateUtils.getNowDate());
            member.setJoinTime(DateUtils.getNowDate());
            member.setCreateBy(clubInfo.getUpdateBy());
            clubMemberMapper.insertClubMember(member);
        }
        return rows;
    }

    @Override
    public Long selectClubIdByLeaderUserId(Long leaderUserId)
    {
        return clubInfoMapper.selectClubIdByLeaderUserId(leaderUserId);
    }
}
