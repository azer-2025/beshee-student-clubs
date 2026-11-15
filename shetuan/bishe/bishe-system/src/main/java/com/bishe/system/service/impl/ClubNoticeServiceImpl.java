package com.bishe.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bishe.system.domain.ClubNotice;
import com.bishe.system.mapper.ClubNoticeMapper;
import com.bishe.system.service.IClubNoticeService;

/**
 * 社团公告 服务层实现
 */
@Service
public class ClubNoticeServiceImpl implements IClubNoticeService
{
    @Autowired
    private ClubNoticeMapper clubNoticeMapper;

    @Override
    public ClubNotice selectClubNoticeById(Long noticeId)
    {
        return clubNoticeMapper.selectClubNoticeById(noticeId);
    }

    @Override
    public List<ClubNotice> selectClubNoticeList(ClubNotice notice)
    {
        return clubNoticeMapper.selectClubNoticeList(notice);
    }

    @Override
    public int insertClubNotice(ClubNotice notice)
    {
        return clubNoticeMapper.insertClubNotice(notice);
    }

    @Override
    public int updateClubNotice(ClubNotice notice)
    {
        return clubNoticeMapper.updateClubNotice(notice);
    }

    @Override
    public int deleteClubNoticeById(Long noticeId)
    {
        return clubNoticeMapper.deleteClubNoticeById(noticeId);
    }

    @Override
    public int deleteClubNoticeByIds(Long[] noticeIds)
    {
        return clubNoticeMapper.deleteClubNoticeByIds(noticeIds);
    }
}

