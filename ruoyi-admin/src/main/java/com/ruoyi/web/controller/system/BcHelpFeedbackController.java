package com.ruoyi.web.controller.system;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import org.springframework.security.access.prepost.PreAuthorize;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BcHelpFeedback;
import com.ruoyi.system.service.IBcHelpFeedbackService;

/**
 * 问题反馈管理 Controller（管理员）
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/help/feedback")
public class BcHelpFeedbackController extends BaseController {
    @Autowired
    private IBcHelpFeedbackService feedbackService;

    /**
     * 查询反馈列表
     */
    @PreAuthorize("@ss.hasPermi('help:feedback:list')")
    @GetMapping("/list")
    public TableDataInfo list(BcHelpFeedback feedback) {
        startPage();
        List<BcHelpFeedback> list = feedbackService.selectHelpFeedbackList(feedback);
        return getDataTable(list);
    }

    /**
     * 导出反馈
     */
    @Log(title = "问题反馈", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('help:feedback:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, BcHelpFeedback feedback) {
        List<BcHelpFeedback> list = feedbackService.selectHelpFeedbackList(feedback);
        ExcelUtil<BcHelpFeedback> util = new ExcelUtil<BcHelpFeedback>(BcHelpFeedback.class);
        util.exportExcel(response, list, "问题反馈数据");
    }

    /**
     * 获取反馈详情
     */
    @PreAuthorize("@ss.hasPermi('help:feedback:list')")
    @GetMapping(value = "/{feedbackId}")
    public AjaxResult getInfo(@PathVariable Long feedbackId) {
        return success(feedbackService.selectHelpFeedbackById(feedbackId));
    }

    /**
     * 回复反馈
     */
    @Log(title = "问题反馈", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('help:feedback:reply')")
    @PutMapping
    public AjaxResult reply(@Validated @RequestBody BcHelpFeedback feedback) {
        return toAjax(feedbackService.replyHelpFeedback(feedback));
    }

    /**
     * 删除反馈
     */
    @Log(title = "问题反馈", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('help:feedback:remove')")
    @DeleteMapping("/{feedbackIds}")
    public AjaxResult remove(@PathVariable Long[] feedbackIds) {
        return toAjax(feedbackService.deleteHelpFeedbackByIds(feedbackIds));
    }

    /**
     * 统计待回复数量
     */
    @GetMapping("/pendingCount")
    public AjaxResult pendingCount() {
        return success(feedbackService.countPendingFeedback());
    }
}
