package com.bishe.system.service;

import java.util.List;
import com.bishe.system.domain.ClubFundApply;

/**
 * 经费申请 服务层
 */
public interface IClubFundApplyService
{
    public ClubFundApply selectClubFundApplyById(Long fundId);

    public List<ClubFundApply> selectClubFundApplyList(ClubFundApply fund);

    public int insertClubFundApply(ClubFundApply fund);

    public int updateClubFundApply(ClubFundApply fund);

    public int deleteClubFundApplyById(Long fundId);

    public int deleteClubFundApplyByIds(Long[] fundIds);

    /**
     * 审核经费申请
     */
    public int approveClubFundApply(ClubFundApply fund);
}

