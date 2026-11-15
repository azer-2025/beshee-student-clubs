package com.bishe.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bishe.system.domain.ClubCategory;
import com.bishe.system.mapper.ClubCategoryMapper;
import com.bishe.system.service.IClubCategoryService;

/**
 * 社团分类 服务层实现
 */
@Service
public class ClubCategoryServiceImpl implements IClubCategoryService
{
    @Autowired
    private ClubCategoryMapper clubCategoryMapper;

    @Override
    public ClubCategory selectClubCategoryById(Long categoryId)
    {
        return clubCategoryMapper.selectClubCategoryById(categoryId);
    }

    @Override
    public List<ClubCategory> selectClubCategoryList(ClubCategory category)
    {
        return clubCategoryMapper.selectClubCategoryList(category);
    }

    @Override
    public int insertClubCategory(ClubCategory category)
    {
        return clubCategoryMapper.insertClubCategory(category);
    }

    @Override
    public int updateClubCategory(ClubCategory category)
    {
        return clubCategoryMapper.updateClubCategory(category);
    }

    @Override
    public int deleteClubCategoryById(Long categoryId)
    {
        return clubCategoryMapper.deleteClubCategoryById(categoryId);
    }

    @Override
    public int deleteClubCategoryByIds(Long[] categoryIds)
    {
        return clubCategoryMapper.deleteClubCategoryByIds(categoryIds);
    }
}

