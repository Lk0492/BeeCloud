package com.ruoyi.web.controller.system;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BcReviewAttachment;
import com.ruoyi.system.domain.BcReviewForm;
import com.ruoyi.system.domain.vo.BcStatisticsVO;
import com.ruoyi.system.service.IBcReviewAttachmentService;
import com.ruoyi.system.service.IBcReviewFormService;
import com.ruoyi.system.service.impl.BcReviewServiceImpl;

/**
 * 资料提审 Controller（学生端）
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/review/submit")
public class BcSubmitController extends BaseController {
    @Autowired
    private IBcReviewFormService formService;

    @Autowired
    private IBcReviewAttachmentService attachmentService;

    @Autowired
    private BcReviewServiceImpl reviewService;

    /**
     * 获取当前学生的申请单（含附件）
     */
    @GetMapping("/current")
    public AjaxResult getCurrentForm() {
        Long studentId = SecurityUtils.getUserId();
        BcReviewForm form = formService.getOrCreateCurrentForm(studentId);
        return success(form);
    }

    /**
     * 提交申请单
     */
    @Log(title = "资料提审", businessType = BusinessType.UPDATE)
    @PostMapping("/doSubmit")
    public AjaxResult doSubmit(@RequestBody BcReviewForm form) {
        Long studentId = SecurityUtils.getUserId();
        return toAjax(formService.submitForm(form.getFormId(), studentId));
    }

    /**
     * 补交（重新提交被驳回的申请）
     */
    @Log(title = "资料补交", businessType = BusinessType.UPDATE)
    @PostMapping("/resubmit")
    public AjaxResult resubmit(@RequestBody BcReviewForm form) {
        Long studentId = SecurityUtils.getUserId();
        return toAjax(formService.resubmitForm(form.getFormId(), studentId));
    }

    /**
     * 查询历史版本
     */
    @GetMapping("/history")
    public AjaxResult getHistory() {
        Long studentId = SecurityUtils.getUserId();
        List<BcReviewForm> list = formService.selectHistoryFormsByStudentId(studentId);
        return success(list);
    }

    /**
     * 查询状态统计
     */
    @GetMapping("/statusCounts")
    public AjaxResult getStatusCounts() {
        Long studentId = SecurityUtils.getUserId();
        BcReviewForm form = new BcReviewForm();
        form.setStudentId(studentId);
        Map<String, Long> counts = formService.selectStatusCounts();
        return success(counts);
    }
}
