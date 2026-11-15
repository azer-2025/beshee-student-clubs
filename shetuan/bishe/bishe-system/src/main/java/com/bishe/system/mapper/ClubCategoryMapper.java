package com.bishe.system.mapper;

import java.util.List;
import com.bishe.system.domain.ClubCategory;

/**
 * 社团分类 数据层
 */
public interface ClubCategoryMapper
{
    public ClubCategory selectClubCategoryById(Long categoryId);

    public List<ClubCategory> selectClubCategoryList(ClubCategory category);

    public int insertClubCategory(ClubCategory category);

    public int updateClubCategory(ClubCategory category);

    public int deleteClubCategoryById(Long categoryId);

    public int deleteClubCategoryByIds(Long[] categoryIds);
}

