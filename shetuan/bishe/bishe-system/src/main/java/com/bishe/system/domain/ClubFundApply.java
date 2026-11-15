package com.bishe.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.bishe.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 经费申请
 */
public class ClubFundApply extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long fundId;
    private Long clubId;
    private Long activityId;
    private BigDecimal amount;
    private String reason;
    private String status;
    private Long applyUserId;
    private Date applyTime;
    private Long auditUserId;
    private Date auditTime;
    private String auditRemark;

    public Long getFundId()
    {
        return fundId;
    }

    public void setFundId(Long fundId)
    {
        this.fundId = fundId;
    }

    public Long getClubId()
    {
        return clubId;
    }

    public void setClubId(Long clubId)
    {
        this.clubId = clubId;
    }

    public Long getActivityId()
    {
        return activityId;
    }

    public void setActivityId(Long activityId)
    {
        this.activityId = activityId;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Long getApplyUserId()
    {
        return applyUserId;
    }

    public void setApplyUserId(Long applyUserId)
    {
        this.applyUserId = applyUserId;
    }

    public Date getApplyTime()
    {
        return applyTime;
    }

    public void setApplyTime(Date applyTime)
    {
        this.applyTime = applyTime;
    }

    public Long getAuditUserId()
    {
        return auditUserId;
    }

    public void setAuditUserId(Long auditUserId)
    {
        this.auditUserId = auditUserId;
    }

    public Date getAuditTime()
    {
        return auditTime;
    }

    public void setAuditTime(Date auditTime)
    {
        this.auditTime = auditTime;
    }

    public String getAuditRemark()
    {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark)
    {
        this.auditRemark = auditRemark;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("fundId", getFundId())
            .append("clubId", getClubId())
            .append("activityId", getActivityId())
            .append("amount", getAmount())
            .append("reason", getReason())
            .append("status", getStatus())
            .append("applyUserId", getApplyUserId())
            .append("applyTime", getApplyTime())
            .append("auditUserId", getAuditUserId())
            .append("auditTime", getAuditTime())
            .append("auditRemark", getAuditRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}

