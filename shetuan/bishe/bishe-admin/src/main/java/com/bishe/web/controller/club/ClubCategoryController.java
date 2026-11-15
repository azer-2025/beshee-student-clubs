package com.bishe.web.controller.club;

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
import com.bishe.system.domain.ClubCategory;
import com.bishe.system.service.IClubCategoryService;

/**
 * 社团分类管理
 */
@RestController
@RequestMapping("/club/category")
public class ClubCategoryController extends BaseController
{
    @Autowired
    private IClubCategoryService clubCategoryService;

    @PreAuthorize("@ss.hasPermi('club:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(ClubCategory category)
    {
        startPage();
        List<ClubCategory> list = clubCategoryService.selectClubCategoryList(category);
        return getDataTable(list);
    }

    /**
     * 前台公开分类列表（仅正常状态）
     */
    @Anonymous
    @GetMapping("/public/list")
    public AjaxResult publicList(ClubCategory category)
    {
        category.setStatus("0");
        List<ClubCategory> list = clubCategoryService.selectClubCategoryList(category);
        return success(list);
    }

    @PreAuthorize("@ss.hasPermi('club:category:query')")
    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable Long categoryId)
    {
        return success(clubCategoryService.selectClubCategoryById(categoryId));
    }

    @PreAuthorize("@ss.hasPermi('club:category:add')")
    @Log(title = "社团分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ClubCategory category)
    {
        category.setCreateBy(getUsername());
        return toAjax(clubCategoryService.insertClubCategory(category));
    }

    @PreAuthorize("@ss.hasPermi('club:category:edit')")
    @Log(title = "社团分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ClubCategory category)
    {
        category.setUpdateBy(getUsername());
        return toAjax(clubCategoryService.updateClubCategory(category));
    }

    @PreAuthorize("@ss.hasPermi('club:category:remove')")
    @Log(title = "社团分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable Long[] categoryIds)
    {
        return toAjax(clubCategoryService.deleteClubCategoryByIds(categoryIds));
    }
}
