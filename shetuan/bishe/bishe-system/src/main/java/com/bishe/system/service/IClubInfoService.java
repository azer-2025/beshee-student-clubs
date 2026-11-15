package com.bishe.system.service;

import java.util.List;
import com.bishe.system.domain.ClubInfo;

/**
 * 社团信息 服务层
 */
public interface IClubInfoService
{
    public ClubInfo selectClubInfoById(Long clubId);

    public List<ClubInfo> selectClubInfoList(ClubInfo clubInfo);

    public int insertClubInfo(ClubInfo clubInfo);

    public int updateClubInfo(ClubInfo clubInfo);

    public int deleteClubInfoById(Long clubId);

    public int deleteClubInfoByIds(Long[] clubIds);

    /**
     * 审核社团申请
     */
    public int approveClub(ClubInfo clubInfo);

    public Long selectClubIdByLeaderUserId(Long leaderUserId);
}
