package com.bishe.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bishe.common.utils.DateUtils;
import com.bishe.system.domain.ClubMember;
import com.bishe.system.mapper.ClubMemberMapper;
import com.bishe.system.service.IClubMemberService;

/**
 * 社团成员 服务层实现
 */
@Service
public class ClubMemberServiceImpl implements IClubMemberService
{
    @Autowired
    private ClubMemberMapper clubMemberMapper;

    @Override
    public ClubMember selectClubMemberById(Long memberId)
    {
        return clubMemberMapper.selectClubMemberById(memberId);
    }

    @Override
    public List<ClubMember> selectClubMemberList(ClubMember clubMember)
    {
        return clubMemberMapper.selectClubMemberList(clubMember);
    }

    @Override
    public int insertClubMember(ClubMember clubMember)
    {
        return clubMemberMapper.insertClubMember(clubMember);
    }

    @Override
    public int updateClubMember(ClubMember clubMember)
    {
        return clubMemberMapper.updateClubMember(clubMember);
    }

    @Override
    public int deleteClubMemberById(Long memberId)
    {
        return clubMemberMapper.deleteClubMemberById(memberId);
    }

    @Override
    public int deleteClubMemberByIds(Long[] memberIds)
    {
        return clubMemberMapper.deleteClubMemberByIds(memberIds);
    }

    @Override
    public int approveClubMember(ClubMember clubMember)
    {
        if ("1".equals(clubMember.getStatus()) && clubMember.getJoinTime() == null)
        {
            clubMember.setJoinTime(DateUtils.getNowDate());
        }
        if ("3".equals(clubMember.getStatus()) && clubMember.getLeaveTime() == null)
        {
            clubMember.setLeaveTime(DateUtils.getNowDate());
        }
        return clubMemberMapper.updateClubMember(clubMember);
    }

    @Override
    public int countClubMemberByClubIdAndUserId(Long clubId, Long userId)
    {
        return clubMemberMapper.countClubMemberByClubIdAndUserId(clubId, userId);
    }
}
