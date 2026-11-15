package com.bishe.system.service;

import java.util.List;
import com.bishe.system.domain.ClubMember;

/**
 * 社团成员 服务层
 */
public interface IClubMemberService
{
    public ClubMember selectClubMemberById(Long memberId);

    public List<ClubMember> selectClubMemberList(ClubMember clubMember);

    public int insertClubMember(ClubMember clubMember);

    public int updateClubMember(ClubMember clubMember);

    public int deleteClubMemberById(Long memberId);

    public int deleteClubMemberByIds(Long[] memberIds);

    /**
     * 审核入社申请
     */
    public int approveClubMember(ClubMember clubMember);

    public int countClubMemberByClubIdAndUserId(Long clubId, Long userId);
}
