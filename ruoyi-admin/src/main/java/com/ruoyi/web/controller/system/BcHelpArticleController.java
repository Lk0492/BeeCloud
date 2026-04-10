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
import com.ruoyi.system.domain.BcHelpArticle;
import com.ruoyi.system.service.IBcHelpArticleService;

/**
 * 知识库管理 Controller（管理员）
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/help/article")
public class BcHelpArticleController extends BaseController {
    @Autowired
    private IBcHelpArticleService articleService;

    /**
     * 查询知识库列表
     */
    @PreAuthorize("@ss.hasPermi('help:article:list')")
    @GetMapping("/list")
    public TableDataInfo list(BcHelpArticle article) {
        startPage();
        List<BcHelpArticle> list = articleService.selectHelpArticleList(article);
        return getDataTable(list);
    }

    /**
     * 导出知识库
     */
    @Log(title = "知识库", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('help:article:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, BcHelpArticle article) {
        List<BcHelpArticle> list = articleService.selectHelpArticleList(article);
        ExcelUtil<BcHelpArticle> util = new ExcelUtil<BcHelpArticle>(BcHelpArticle.class);
        util.exportExcel(response, list, "知识库数据");
    }

    /**
     * 获取知识库详情
     */
    @PreAuthorize("@ss.hasPermi('help:article:list')")
    @GetMapping(value = "/{articleId}")
    public AjaxResult getInfo(@PathVariable Long articleId) {
        return success(articleService.selectHelpArticleById(articleId));
    }

    /**
     * 新增知识库
     */
    @Log(title = "知识库", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('help:article:add')")
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BcHelpArticle article) {
        return toAjax(articleService.insertHelpArticle(article));
    }

    /**
     * 修改知识库
     */
    @Log(title = "知识库", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('help:article:edit')")
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BcHelpArticle article) {
        return toAjax(articleService.updateHelpArticle(article));
    }

    /**
     * 删除知识库
     */
    @Log(title = "知识库", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('help:article:remove')")
    @DeleteMapping("/{articleIds}")
    public AjaxResult remove(@PathVariable Long[] articleIds) {
        return toAjax(articleService.deleteHelpArticleByIds(articleIds));
    }
}
