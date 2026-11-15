package com.bishe.system.service;

import java.util.List;
import com.bishe.system.domain.VenueApply;

/**
 * 场地申请 服务层
 */
public interface IVenueApplyService
{
    public VenueApply selectVenueApplyById(Long venueApplyId);

    public List<VenueApply> selectVenueApplyList(VenueApply venueApply);

    public int insertVenueApply(VenueApply venueApply);

    public int updateVenueApply(VenueApply venueApply);

    public int deleteVenueApplyById(Long venueApplyId);

    public int deleteVenueApplyByIds(Long[] venueApplyIds);

    /**
     * 审核场地申请
     */
    public int approveVenueApply(VenueApply venueApply);
}

