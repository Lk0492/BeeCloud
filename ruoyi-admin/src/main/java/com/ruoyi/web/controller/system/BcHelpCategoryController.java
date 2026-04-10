package com.ruoyi.web.controller.system;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BcHelpCategory;
import com.ruoyi.system.service.IBcHelpCategoryService;

/**
 * 问题分类管理 Controller（管理员）
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/help/category")
public class BcHelpCategoryController extends BaseController {
    @Autowired
    private IBcHelpCategoryService categoryService;

    /**
     * 查询分类列表
     */
    @PreAuthorize("@ss.hasPermi('help:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(BcHelpCategory category) {
        startPage();
        List<BcHelpCategory> list = categoryService.selectHelpCategoryList(category);
        return getDataTable(list);
    }

    /**
     * 导出分类
     */
    @Log(title = "问题分类", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('help:category:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, BcHelpCategory category) {
        List<BcHelpCategory> list = categoryService.selectHelpCategoryList(category);
        ExcelUtil<BcHelpCategory> util = new ExcelUtil<BcHelpCategory>(BcHelpCategory.class);
        util.exportExcel(response, list, "问题分类数据");
    }

    /**
     * 获取分类详情
     */
    @PreAuthorize("@ss.hasPermi('help:category:list')")
    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable Long categoryId) {
        return success(categoryService.selectHelpCategoryById(categoryId));
    }

    /**
     * 新增分类
     */
    @Log(title = "问题分类", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('help:category:add')")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BcHelpCategory category) {
        if (!categoryService.checkCategoryCodeUnique(category)) {
            return error("新增分类'" + category.getCategoryName() + "'失败，分类编码已存在");
        }
        return toAjax(categoryService.insertHelpCategory(category));
    }

    /**
     * 修改分类
     */
    @Log(title = "问题分类", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('help:category:edit')")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BcHelpCategory category) {
        if (!categoryService.checkCategoryCodeUnique(category)) {
            return error("修改分类'" + category.getCategoryName() + "'失败，分类编码已存在");
        }
        return toAjax(categoryService.updateHelpCategory(category));
    }

    /**
     * 删除分类
     */
    @Log(title = "问题分类", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('help:category:remove')")
    @DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable Long[] categoryIds) {
        return toAjax(categoryService.deleteHelpCategoryByIds(categoryIds));
    }

    /**
     * 获取分类下拉树列表
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(BcHelpCategory category) {
        List<BcHelpCategory> categories = categoryService.selectHelpCategoryAll();
        return success(categoryService.buildHelpCategoryTree(categories));
    }
}
