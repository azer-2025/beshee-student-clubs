package com.bishe.web.controller.content;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bishe.common.annotation.Anonymous;
import com.bishe.common.annotation.Log;
import com.bishe.common.core.controller.BaseController;
import com.bishe.common.core.domain.AjaxResult;
import com.bishe.common.core.page.TableDataInfo;
import com.bishe.common.enums.BusinessType;
import com.bishe.system.domain.ContentCarousel;
import com.bishe.system.service.IContentCarouselService;

/**
 * 轮播图管理
 */
@RestController
@RequestMapping("/content/carousel")
public class ContentCarouselController extends BaseController
{
    @Autowired
    private IContentCarouselService contentCarouselService;

    @PreAuthorize("@ss.hasPermi('content:carousel:list')")
    @GetMapping("/list")
    public TableDataInfo list(ContentCarousel carousel)
    {
        startPage();
        List<ContentCarousel> list = contentCarouselService.selectContentCarouselList(carousel);
        return getDataTable(list);
    }

    /**
     * 前台轮播图列表（仅正常状态）
     */
    @Anonymous
    @GetMapping("/public/list")
    public AjaxResult publicList(ContentCarousel carousel)
    {
        carousel.setStatus("0");
        List<ContentCarousel> list = contentCarouselService.selectContentCarouselList(carousel);
        return success(list);
    }

    @PreAuthorize("@ss.hasPermi('content:carousel:query')")
    @GetMapping(value = "/{carouselId}")
    public AjaxResult getInfo(@PathVariable Long carouselId)
    {
        return success(contentCarouselService.selectContentCarouselById(carouselId));
    }

    @PreAuthorize("@ss.hasPermi('content:carousel:add')")
    @Log(title = "轮播图", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ContentCarousel carousel)
    {
        carousel.setCreateBy(getUsername());
        return toAjax(contentCarouselService.insertContentCarousel(carousel));
    }

    @PreAuthorize("@ss.hasPermi('content:carousel:edit')")
    @Log(title = "轮播图", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ContentCarousel carousel)
    {
        carousel.setUpdateBy(getUsername());
        return toAjax(contentCarouselService.updateContentCarousel(carousel));
    }

    @PreAuthorize("@ss.hasPermi('content:carousel:remove')")
    @Log(title = "轮播图", businessType = BusinessType.DELETE)
    @DeleteMapping("/{carouselIds}")
    public AjaxResult remove(@PathVariable Long[] carouselIds)
    {
        return toAjax(contentCarouselService.deleteContentCarouselByIds(carouselIds));
    }
}
