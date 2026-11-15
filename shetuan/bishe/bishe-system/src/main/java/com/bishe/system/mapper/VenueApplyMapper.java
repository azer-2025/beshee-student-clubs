package com.bishe.system.mapper;

import java.util.List;
import com.bishe.system.domain.VenueApply;

/**
 * 场地申请 数据层
 */
public interface VenueApplyMapper
{
    public VenueApply selectVenueApplyById(Long venueApplyId);

    public List<VenueApply> selectVenueApplyList(VenueApply venueApply);

    public int insertVenueApply(VenueApply venueApply);

    public int updateVenueApply(VenueApply venueApply);

    public int deleteVenueApplyById(Long venueApplyId);

    public int deleteVenueApplyByIds(Long[] venueApplyIds);
}

