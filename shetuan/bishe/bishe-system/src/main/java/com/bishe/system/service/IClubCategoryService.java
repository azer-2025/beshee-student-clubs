package com.bishe.system.service;

import java.util.List;
import com.bishe.system.domain.ClubCategory;

/**
 * 社团分类 服务层
 */
public interface IClubCategoryService
{
    public ClubCategory selectClubCategoryById(Long categoryId);

    public List<ClubCategory> selectClubCategoryList(ClubCategory category);

    public int insertClubCategory(ClubCategory category);

    public int updateClubCategory(ClubCategory category);

    public int deleteClubCategoryById(Long categoryId);

    public int deleteClubCategoryByIds(Long[] categoryIds);
}

