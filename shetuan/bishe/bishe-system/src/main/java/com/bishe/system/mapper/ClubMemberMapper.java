package com.bishe.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.bishe.system.domain.ClubMember;

/**
 * 社团成员 数据层
 */
public interface ClubMemberMapper
{
    public ClubMember selectClubMemberById(Long memberId);

    public List<ClubMember> selectClubMemberList(ClubMember clubMember);

    public int insertClubMember(ClubMember clubMember);

    public int updateClubMember(ClubMember clubMember);

    public int deleteClubMemberById(Long memberId);

    public int deleteClubMemberByIds(Long[] memberIds);

    /**
     * 统计成员是否存在
     */
    public int countClubMemberByClubIdAndUserId(@Param("clubId") Long clubId, @Param("userId") Long userId);
}
