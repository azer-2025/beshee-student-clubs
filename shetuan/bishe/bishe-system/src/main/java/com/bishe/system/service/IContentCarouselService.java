package com.bishe.system.service;

import java.util.List;
import com.bishe.system.domain.ContentCarousel;

/**
 * 轮播图 服务层
 */
public interface IContentCarouselService
{
    public ContentCarousel selectContentCarouselById(Long carouselId);

    public List<ContentCarousel> selectContentCarouselList(ContentCarousel carousel);

    public int insertContentCarousel(ContentCarousel carousel);

    public int updateContentCarousel(ContentCarousel carousel);

    public int deleteContentCarouselById(Long carouselId);

    public int deleteContentCarouselByIds(Long[] carouselIds);
}

