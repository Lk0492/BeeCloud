package com.ruoyi.web.controller.system;

import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BcReviewForm;
import com.ruoyi.system.domain.vo.BcStatisticsVO;
import com.ruoyi.system.service.IBcReviewFormService;
import com.ruoyi.system.service.impl.BcReviewServiceImpl;

/**
 * 资料审核 Controller（审核员端）
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/review/audit")
public class BcAuditController extends BaseController {
    @Autowired
    private IBcReviewFormService formService;

    @Autowired
    private BcReviewServiceImpl reviewService;

    /**
     * 查询待审核列表（审核员）
     */
    @GetMapping("/list")
    public TableDataInfo list(BcReviewForm form) {
        startPage();
        List<BcReviewForm> list = formService.selectReviewFormList(form);
        return getDataTable(list);
    }

    /**
     * 导出审核列表
     */
    @Log(title = "资料审核", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BcReviewForm form) {
        List<BcReviewForm> list = formService.selectReviewFormList(form);
        ExcelUtil<BcReviewForm> util = new ExcelUtil<BcReviewForm>(BcReviewForm.class);
        util.exportExcel(response, list, "资料审核数据");
    }

    /**
     * 获取申请单详情（含附件）
     */
    @GetMapping(value = "/{formId}")
    public AjaxResult getInfo(@PathVariable Long formId) {
        BcReviewForm form = formService.selectReviewFormWithAttachments(formId);
        return success(form);
    }

    /**
     * 审核通过
     */
    @Log(title = "资料审核", businessType = BusinessType.UPDATE)
    @PostMapping("/accept/{formId}")
    public AjaxResult accept(@PathVariable Long formId, @RequestBody(required = false) BcReviewForm form) {
        return toAjax(reviewService.acceptForm(formId, form != null ? form.getRejectReason() : null));
    }

    /**
     * 审核驳回
     */
    @Log(title = "资料审核", businessType = BusinessType.UPDATE)
    @PostMapping("/reject/{formId}")
    public AjaxResult reject(@PathVariable Long formId, @RequestBody(required = false) BcReviewForm form) {
        String reason = (form != null && form.getRejectReason() != null) ? form.getRejectReason() : null;
        return toAjax(reviewService.rejectForm(formId, reason));
    }

    /**
     * 要求补交
     */
    @Log(title = "要求补交", businessType = BusinessType.UPDATE)
    @PostMapping("/supply/{formId}")
    public AjaxResult requestSupply(@PathVariable Long formId, @RequestBody(required = false) BcReviewForm form) {
        String reason = (form != null && form.getRejectReason() != null) ? form.getRejectReason() : null;
        return toAjax(reviewService.requestSupply(formId, reason));
    }

    /**
     * 批量审核通过
     */
    @Log(title = "批量审核", businessType = BusinessType.UPDATE)
    @PostMapping("/batchAccept")
    public AjaxResult batchAccept(@RequestBody BcReviewForm form) {
        Long[] formIds = form.getIds();
        if (formIds == null || formIds.length == 0) {
            return error("请选择要审核的记录");
        }
        return toAjax(reviewService.batchAccept(formIds, form.getRejectReason()));
    }

    /**
     * 批量审核驳回
     */
    @Log(title = "批量审核驳回", businessType = BusinessType.UPDATE)
    @PostMapping("/batchReject")
    public AjaxResult batchReject(@RequestBody BcReviewForm form) {
        Long[] formIds = form.getIds();
        if (formIds == null || formIds.length == 0) {
            return error("请选择要驳回的记录");
        }
        return toAjax(reviewService.batchReject(formIds, form.getRejectReason()));
    }

    /**
     * 分配审核员
     */
    @Log(title = "分配审核员", businessType = BusinessType.UPDATE)
    @PostMapping("/assign/{formId}")
    public AjaxResult assign(@PathVariable Long formId, @RequestBody BcReviewForm form) {
        return toAjax(formService.assignReviewer(formId, form.getReviewerId(), form.getReviewerName()));
    }

    /**
     * 统计数据
     */
    @GetMapping("/statistics")
    public AjaxResult statistics() {
        BcStatisticsVO stats = formService.selectStatistics();
        return success(stats);
    }

    /**
     * 状态统计
     */
    @GetMapping("/statusCounts")
    public AjaxResult statusCounts() {
        Map<String, Long> counts = formService.selectStatusCounts();
        return success(counts);
    }
}
