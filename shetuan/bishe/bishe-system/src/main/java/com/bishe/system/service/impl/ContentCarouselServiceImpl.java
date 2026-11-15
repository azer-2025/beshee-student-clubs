package com.bishe.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bishe.system.domain.ContentCarousel;
import com.bishe.system.mapper.ContentCarouselMapper;
import com.bishe.system.service.IContentCarouselService;

/**
 * 轮播图 服务层实现
 */
@Service
public class ContentCarouselServiceImpl implements IContentCarouselService
{
    @Autowired
    private ContentCarouselMapper contentCarouselMapper;

    @Override
    public ContentCarousel selectContentCarouselById(Long carouselId)
    {
        return contentCarouselMapper.selectContentCarouselById(carouselId);
    }

    @Override
    public List<ContentCarousel> selectContentCarouselList(ContentCarousel carousel)
    {
        return contentCarouselMapper.selectContentCarouselList(carousel);
    }

    @Override
    public int insertContentCarousel(ContentCarousel carousel)
    {
        return contentCarouselMapper.insertContentCarousel(carousel);
    }

    @Override
    public int updateContentCarousel(ContentCarousel carousel)
    {
        return contentCarouselMapper.updateContentCarousel(carousel);
    }

    @Override
    public int deleteContentCarouselById(Long carouselId)
    {
        return contentCarouselMapper.deleteContentCarouselById(carouselId);
    }

    @Override
    public int deleteContentCarouselByIds(Long[] carouselIds)
    {
        return contentCarouselMapper.deleteContentCarouselByIds(carouselIds);
    }
}

