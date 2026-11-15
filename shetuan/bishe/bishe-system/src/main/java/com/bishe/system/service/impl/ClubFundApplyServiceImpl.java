package com.bishe.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bishe.system.domain.ClubFundApply;
import com.bishe.system.mapper.ClubFundApplyMapper;
import com.bishe.system.service.IClubFundApplyService;

/**
 * 经费申请 服务层实现
 */
@Service
public class ClubFundApplyServiceImpl implements IClubFundApplyService
{
    @Autowired
    private ClubFundApplyMapper clubFundApplyMapper;

    @Override
    public ClubFundApply selectClubFundApplyById(Long fundId)
    {
        return clubFundApplyMapper.selectClubFundApplyById(fundId);
    }

    @Override
    public List<ClubFundApply> selectClubFundApplyList(ClubFundApply fund)
    {
        return clubFundApplyMapper.selectClubFundApplyList(fund);
    }

    @Override
    public int insertClubFundApply(ClubFundApply fund)
    {
        return clubFundApplyMapper.insertClubFundApply(fund);
    }

    @Override
    public int updateClubFundApply(ClubFundApply fund)
    {
        return clubFundApplyMapper.updateClubFundApply(fund);
    }

    @Override
    public int deleteClubFundApplyById(Long fundId)
    {
        return clubFundApplyMapper.deleteClubFundApplyById(fundId);
    }

    @Override
    public int deleteClubFundApplyByIds(Long[] fundIds)
    {
        return clubFundApplyMapper.deleteClubFundApplyByIds(fundIds);
    }

    @Override
    public int approveClubFundApply(ClubFundApply fund)
    {
        return clubFundApplyMapper.updateClubFundApply(fund);
    }
}

