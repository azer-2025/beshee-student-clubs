package com.bishe.system.domain;

import java.util.Date;
import com.bishe.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 社团信息
 */
public class ClubInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long clubId;
    private Long categoryId;
    private String clubName;
    private String clubLogo;
    private String clubIntro;
    private String charterContent;
    private String recruitPoster;
    private String honorWall;
    private Long leaderUserId;
    private Long applyUserId;
    private Long teacherUserId;
    private String teacherAuditStatus;
    private Date teacherAuditTime;
    private String teacherAuditRemark;
    private Date applyTime;
    private String applyStatus;
    private Long auditUserId;
    private Date auditTime;
    private String auditRemark;
    private String status;

    public Long getClubId()
    {
        return clubId;
    }

    public void setClubId(Long clubId)
    {
        this.clubId = clubId;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getClubName()
    {
        return clubName;
    }

    public void setClubName(String clubName)
    {
        this.clubName = clubName;
    }

    public String getClubLogo()
    {
        return clubLogo;
    }

    public void setClubLogo(String clubLogo)
    {
        this.clubLogo = clubLogo;
    }

    public String getClubIntro()
    {
        return clubIntro;
    }

    public void setClubIntro(String clubIntro)
    {
        this.clubIntro = clubIntro;
    }

    public String getCharterContent()
    {
        return charterContent;
    }

    public void setCharterContent(String charterContent)
    {
        this.charterContent = charterContent;
    }

    public String getRecruitPoster()
    {
        return recruitPoster;
    }

    public void setRecruitPoster(String recruitPoster)
    {
        this.recruitPoster = recruitPoster;
    }

    public String getHonorWall()
    {
        return honorWall;
    }

    public void setHonorWall(String honorWall)
    {
        this.honorWall = honorWall;
    }

    public Long getLeaderUserId()
    {
        return leaderUserId;
    }

    public void setLeaderUserId(Long leaderUserId)
    {
        this.leaderUserId = leaderUserId;
    }

    public Long getApplyUserId()
    {
        return applyUserId;
    }

    public void setApplyUserId(Long applyUserId)
    {
        this.applyUserId = applyUserId;
    }

    public Long getTeacherUserId()
    {
        return teacherUserId;
    }

    public void setTeacherUserId(Long teacherUserId)
    {
        this.teacherUserId = teacherUserId;
    }

    public String getTeacherAuditStatus()
    {
        return teacherAuditStatus;
    }

    public void setTeacherAuditStatus(String teacherAuditStatus)
    {
        this.teacherAuditStatus = teacherAuditStatus;
    }

    public Date getTeacherAuditTime()
    {
        return teacherAuditTime;
    }

    public void setTeacherAuditTime(Date teacherAuditTime)
    {
        this.teacherAuditTime = teacherAuditTime;
    }

    public String getTeacherAuditRemark()
    {
        return teacherAuditRemark;
    }

    public void setTeacherAuditRemark(String teacherAuditRemark)
    {
        this.teacherAuditRemark = teacherAuditRemark;
    }

    public Date getApplyTime()
    {
        return applyTime;
    }

    public void setApplyTime(Date applyTime)
    {
        this.applyTime = applyTime;
    }

    public String getApplyStatus()
    {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus)
    {
        this.applyStatus = applyStatus;
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

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("clubId", getClubId())
            .append("categoryId", getCategoryId())
            .append("clubName", getClubName())
            .append("clubLogo", getClubLogo())
            .append("clubIntro", getClubIntro())
            .append("charterContent", getCharterContent())
            .append("recruitPoster", getRecruitPoster())
            .append("honorWall", getHonorWall())
            .append("leaderUserId", getLeaderUserId())
            .append("applyUserId", getApplyUserId())
            .append("teacherUserId", getTeacherUserId())
            .append("teacherAuditStatus", getTeacherAuditStatus())
            .append("teacherAuditTime", getTeacherAuditTime())
            .append("teacherAuditRemark", getTeacherAuditRemark())
            .append("applyTime", getApplyTime())
            .append("applyStatus", getApplyStatus())
            .append("auditUserId", getAuditUserId())
            .append("auditTime", getAuditTime())
            .append("auditRemark", getAuditRemark())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
