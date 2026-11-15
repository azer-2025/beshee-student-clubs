package com.bishe.system.domain;

import java.util.Date;
import com.bishe.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 社团成员
 */
public class ClubMember extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long memberId;
    private Long clubId;
    private String clubName;
    private Long userId;
    private String memberRole;
    private String status;
    private Date applyTime;
    private Date joinTime;
    private String positionName;
    private String honorRecord;
    private Date leaveTime;
    private String leaveReason;
    private Long auditUserId;
    private Date auditTime;
    private String auditRemark;

    public Long getMemberId()
    {
        return memberId;
    }

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    public Long getClubId()
    {
        return clubId;
    }

    public void setClubId(Long clubId)
    {
        this.clubId = clubId;
    }

    public String getClubName()
    {
        return clubName;
    }

    public void setClubName(String clubName)
    {
        this.clubName = clubName;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getMemberRole()
    {
        return memberRole;
    }

    public void setMemberRole(String memberRole)
    {
        this.memberRole = memberRole;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Date getApplyTime()
    {
        return applyTime;
    }

    public void setApplyTime(Date applyTime)
    {
        this.applyTime = applyTime;
    }

    public Date getJoinTime()
    {
        return joinTime;
    }

    public void setJoinTime(Date joinTime)
    {
        this.joinTime = joinTime;
    }

    public String getPositionName()
    {
        return positionName;
    }

    public void setPositionName(String positionName)
    {
        this.positionName = positionName;
    }

    public String getHonorRecord()
    {
        return honorRecord;
    }

    public void setHonorRecord(String honorRecord)
    {
        this.honorRecord = honorRecord;
    }

    public Date getLeaveTime()
    {
        return leaveTime;
    }

    public void setLeaveTime(Date leaveTime)
    {
        this.leaveTime = leaveTime;
    }

    public String getLeaveReason()
    {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason)
    {
        this.leaveReason = leaveReason;
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
            .append("memberId", getMemberId())
            .append("clubId", getClubId())
            .append("clubName", getClubName())
            .append("userId", getUserId())
            .append("memberRole", getMemberRole())
            .append("status", getStatus())
            .append("applyTime", getApplyTime())
            .append("joinTime", getJoinTime())
            .append("positionName", getPositionName())
            .append("honorRecord", getHonorRecord())
            .append("leaveTime", getLeaveTime())
            .append("leaveReason", getLeaveReason())
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
