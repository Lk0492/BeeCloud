package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BcReviewAttachment;
import com.ruoyi.system.domain.BcReviewForm;
import com.ruoyi.system.domain.BcReviewRecord;
import com.ruoyi.system.mapper.BcReviewAttachmentMapper;
import com.ruoyi.system.mapper.BcReviewFormMapper;
import com.ruoyi.system.mapper.BcReviewRecordMapper;
import com.ruoyi.system.service.IBcReviewAttachmentService;
import com.ruoyi.system.service.IBcReviewFormService;
import com.ruoyi.system.service.IBcNoticeService;

/**
 * 资料审核申请单（含审核动作）Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class BcReviewServiceImpl {
    @Autowired
    private BcReviewFormMapper formMapper;

    @Autowired
    private BcReviewAttachmentMapper attachmentMapper;

    @Autowired
    private BcReviewRecordMapper recordMapper;

    @Autowired
    private IBcNoticeService bcNoticeService;

    /**
     * 审核通过（单表单批审核）
     */
    @Transactional
    public int acceptForm(Long formId, String remark) {
        BcReviewForm form = formMapper.selectReviewFormById(formId);
        if (form == null) {
            throw new ServiceException("申请单不存在");
        }
        if (!"2".equals(form.getFormStatus()) && !"6".equals(form.getFormStatus())) {
            throw new ServiceException("当前状态不允许审核通过");
        }
        String oldStatus = form.getFormStatus();
        form.setFormStatus("3");
        form.setReviewTime(new Date());
        form.setReviewedAttachments(form.getTotalAttachments());
        formMapper.updateReviewForm(form);
        BcReviewRecord record = buildRecord(form, "accept", null, oldStatus, "3", remark);
        recordMapper.insertRecord(record);
        bcNoticeService.sendPassNotice(formId, form.getStudentId(), form.getStudentName());
        return 1;
    }

    /**
     * 审核驳回
     */
    @Transactional
    public int rejectForm(Long formId, String remark) {
        if (StringUtils.isEmpty(remark)) {
            throw new ServiceException("驳回原因不能为空");
        }
        BcReviewForm form = formMapper.selectReviewFormById(formId);
        if (form == null) {
            throw new ServiceException("申请单不存在");
        }
        if (!"2".equals(form.getFormStatus()) && !"6".equals(form.getFormStatus())) {
            throw new ServiceException("当前状态不允许驳回");
        }
        String oldStatus = form.getFormStatus();
        form.setFormStatus("4");
        form.setReviewTime(new Date());
        form.setRejectReason(remark);
        formMapper.updateReviewForm(form);
        BcReviewRecord record = buildRecord(form, "reject", null, oldStatus, "4", remark);
        recordMapper.insertRecord(record);
        bcNoticeService.sendRejectNotice(formId, form.getStudentId(), form.getStudentName(), remark);
        return 1;
    }

    /**
     * 批量审核通过
     */
    @Transactional
    public int batchAccept(Long[] formIds, String remark) {
        int count = 0;
        for (Long formId : formIds) {
            try {
                acceptForm(formId, remark);
                count++;
            } catch (ServiceException ignored) {
            }
        }
        return count;
    }

    /**
     * 批量审核驳回
     */
    @Transactional
    public int batchReject(Long[] formIds, String remark) {
        int count = 0;
        for (Long formId : formIds) {
            try {
                rejectForm(formId, remark);
                count++;
            } catch (ServiceException ignored) {
            }
        }
        return count;
    }

    /**
     * 要求补交
     */
    @Transactional
    public int requestSupply(Long formId, String remark) {
        if (StringUtils.isEmpty(remark)) {
            throw new ServiceException("补交说明不能为空");
        }
        BcReviewForm form = formMapper.selectReviewFormById(formId);
        if (form == null) {
            throw new ServiceException("申请单不存在");
        }
        if (!"2".equals(form.getFormStatus()) && !"6".equals(form.getFormStatus())) {
            throw new ServiceException("当前状态不允许要求补交");
        }
        String oldStatus = form.getFormStatus();
        form.setFormStatus("5");
        form.setReviewTime(new Date());
        form.setRejectReason(remark);
        formMapper.updateReviewForm(form);
        BcReviewRecord record = buildRecord(form, "reject", null, oldStatus, "5", remark);
        recordMapper.insertRecord(record);
        bcNoticeService.sendSupplyNotice(formId, form.getStudentId(), form.getStudentName(), remark);
        return 1;
    }

    private BcReviewRecord buildRecord(BcReviewForm form, String actionType, Long detailId,
            String statusBefore, String statusAfter, String remark) {
        BcReviewRecord record = new BcReviewRecord();
        record.setFormId(form.getFormId());
        record.setDetailId(detailId);
        record.setStudentId(form.getStudentId());
        record.setStudentName(form.getStudentName());
        record.setReviewerId(SecurityUtils.getUserId());
        record.setReviewerName(SecurityUtils.getUsername());
        record.setActionType(actionType);
        record.setActionRemark(remark);
        record.setFormStatusBefore(statusBefore);
        record.setFormStatusAfter(statusAfter);
        record.setVersionNum(form.getVersionNum());
        record.setCreateBy(SecurityUtils.getUsername());
        return record;
    }
}
