package com.bishe.system.mapper;

import java.util.List;
import com.bishe.system.domain.ClubInfo;

/**
 * 社团信息 数据层
 */
public interface ClubInfoMapper
{
    public ClubInfo selectClubInfoById(Long clubId);

    public List<ClubInfo> selectClubInfoList(ClubInfo clubInfo);

    public int insertClubInfo(ClubInfo clubInfo);

    public int updateClubInfo(ClubInfo clubInfo);

    public int deleteClubInfoById(Long clubId);

    public int deleteClubInfoByIds(Long[] clubIds);

    public Long selectClubIdByLeaderUserId(Long leaderUserId);
}
