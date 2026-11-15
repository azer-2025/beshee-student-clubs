package com.bishe.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bishe.system.domain.VenueApply;
import com.bishe.system.mapper.VenueApplyMapper;
import com.bishe.system.service.IVenueApplyService;

/**
 * 场地申请 服务层实现
 */
@Service
public class VenueApplyServiceImpl implements IVenueApplyService
{
    @Autowired
    private VenueApplyMapper venueApplyMapper;

    @Override
    public VenueApply selectVenueApplyById(Long venueApplyId)
    {
        return venueApplyMapper.selectVenueApplyById(venueApplyId);
    }

    @Override
    public List<VenueApply> selectVenueApplyList(VenueApply venueApply)
    {
        return venueApplyMapper.selectVenueApplyList(venueApply);
    }

    @Override
    public int insertVenueApply(VenueApply venueApply)
    {
        return venueApplyMapper.insertVenueApply(venueApply);
    }

    @Override
    public int updateVenueApply(VenueApply venueApply)
    {
        return venueApplyMapper.updateVenueApply(venueApply);
    }

    @Override
    public int deleteVenueApplyById(Long venueApplyId)
    {
        return venueApplyMapper.deleteVenueApplyById(venueApplyId);
    }

    @Override
    public int deleteVenueApplyByIds(Long[] venueApplyIds)
    {
        return venueApplyMapper.deleteVenueApplyByIds(venueApplyIds);
    }

    @Override
    public int approveVenueApply(VenueApply venueApply)
    {
        return venueApplyMapper.updateVenueApply(venueApply);
    }
}

