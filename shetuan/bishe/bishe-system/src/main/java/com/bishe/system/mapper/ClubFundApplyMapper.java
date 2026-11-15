package com.bishe.system.mapper;

import java.util.List;
import com.bishe.system.domain.ClubFundApply;

/**
 * 经费申请 数据层
 */
public interface ClubFundApplyMapper
{
    public ClubFundApply selectClubFundApplyById(Long fundId);

    public List<ClubFundApply> selectClubFundApplyList(ClubFundApply fund);

    public int insertClubFundApply(ClubFundApply fund);

    public int updateClubFundApply(ClubFundApply fund);

    public int deleteClubFundApplyById(Long fundId);

    public int deleteClubFundApplyByIds(Long[] fundIds);
}

