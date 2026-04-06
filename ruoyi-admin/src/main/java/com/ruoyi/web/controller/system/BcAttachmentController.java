package com.ruoyi.web.controller.system;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BcReviewAttachment;
import com.ruoyi.system.domain.BcReviewForm;
import com.ruoyi.system.mapper.BcReviewFormMapper;
import com.ruoyi.system.service.IBcReviewAttachmentService;
import com.ruoyi.system.service.IBcReviewFormService;

/**
 * 附件上传 Controller（资料审核模块专用）
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/review/attachment")
public class BcAttachmentController extends BaseController {
    @Autowired
    private IBcReviewAttachmentService attachmentService;

    @Autowired
    private IBcReviewFormService formService;

    @Autowired
    private BcReviewFormMapper formMapper;

    /**
     * 为申请单添加附件记录（上传后调用此接口保存附件信息）
     */
    @Log(title = "上传审核资料", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BcReviewAttachment attachment) {
        Long studentId = SecurityUtils.getUserId();
        BcReviewForm form = formService.getOrCreateCurrentForm(studentId);
        attachment.setFormId(form.getFormId());
        attachment.setAttachStatus("0");
        attachment.setVersionNum(form.getVersionNum());
        attachment.setCreateBy(SecurityUtils.getUsername());
        int rows = attachmentService.insertAttachment(attachment);
        formMapper.updateAttachmentCount(form.getFormId());
        return rows > 0 ? success(attachment) : error("保存附件信息失败");
    }

    /**
     * 批量添加附件记录
     */
    @Log(title = "批量上传审核资料", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public AjaxResult addBatch(@RequestBody List<BcReviewAttachment> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            return error("附件列表不能为空");
        }
        Long studentId = SecurityUtils.getUserId();
        BcReviewForm form = formService.getOrCreateCurrentForm(studentId);
        for (BcReviewAttachment att : attachments) {
            att.setFormId(form.getFormId());
            att.setAttachStatus("0");
            att.setVersionNum(form.getVersionNum());
            att.setCreateBy(SecurityUtils.getUsername());
        }
        int rows = attachmentService.insertAttachments(attachments);
        formMapper.updateAttachmentCount(form.getFormId());
        return toAjax(rows);
    }

    /**
     * 查询申请单的所有附件
     */
    @GetMapping("/list/{formId}")
    public AjaxResult list(@PathVariable Long formId) {
        return success(attachmentService.selectAttachmentsByFormId(formId));
    }

    /**
     * 删除附件（逻辑删除）
     */
    @Log(title = "删除审核资料", businessType = BusinessType.DELETE)
    @DeleteMapping("/{detailIds}")
    public AjaxResult remove(@PathVariable Long[] detailIds) {
        int rows = attachmentService.deleteAttachmentByIds(detailIds);
        if (detailIds != null && detailIds.length > 0) {
            formMapper.updateAttachmentCount(detailIds[0]);
        }
        return toAjax(rows);
    }

    /**
     * 获取某类型的已上传数量
     */
    @GetMapping("/count/{formId}/{category}")
    public AjaxResult countByCategory(@PathVariable Long formId, @PathVariable String category) {
        return success(attachmentService.countByFormIdAndCategory(formId, category));
    }
}
