package com.bishe.system.service;

import java.util.List;
import com.bishe.system.domain.ClubNotice;

/**
 * 社团公告 服务层
 */
public interface IClubNoticeService
{
    public ClubNotice selectClubNoticeById(Long noticeId);

    public List<ClubNotice> selectClubNoticeList(ClubNotice notice);

    public int insertClubNotice(ClubNotice notice);

    public int updateClubNotice(ClubNotice notice);

    public int deleteClubNoticeById(Long noticeId);

    public int deleteClubNoticeByIds(Long[] noticeIds);
}

