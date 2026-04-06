package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.BcReviewAttachment;
import com.ruoyi.system.domain.BcReviewForm;
import com.ruoyi.system.domain.BcReviewRecord;
import com.ruoyi.system.domain.vo.BcStatisticsVO;
import com.ruoyi.system.mapper.BcReviewAttachmentMapper;
import com.ruoyi.system.mapper.BcReviewFormMapper;
import com.ruoyi.system.mapper.BcReviewRecordMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.IBcNoticeService;
import com.ruoyi.system.service.IBcReviewFormService;

/**
 * 资料审核申请单Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class BcReviewFormServiceImpl implements IBcReviewFormService {
    @Autowired
    private BcReviewFormMapper formMapper;

    @Autowired
    private BcReviewAttachmentMapper attachmentMapper;

    @Autowired
    private BcReviewRecordMapper recordMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private IBcNoticeService bcNoticeService;

    @Override
    public List<BcReviewForm> selectReviewFormList(BcReviewForm form) {
        return formMapper.selectReviewFormList(form);
    }

    @Override
    public BcReviewForm selectCurrentFormByStudentId(Long studentId) {
        return formMapper.selectCurrentFormByStudentId(studentId);
    }

    @Override
    public BcReviewForm selectReviewFormWithAttachments(Long formId) {
        return formMapper.selectReviewFormWithAttachments(formId);
    }

    @Override
    public BcReviewForm selectReviewFormById(Long formId) {
        return formMapper.selectReviewFormById(formId);
    }

    @Override
    @Transactional
    public BcReviewForm getOrCreateCurrentForm(Long studentId) {
        SysUser student = userMapper.selectUserById(studentId);
        if (student == null) {
            throw new ServiceException("学生信息不存在");
        }
        BcReviewForm currentForm = formMapper.selectCurrentFormByStudentId(studentId);
        if (currentForm != null) {
            List<BcReviewAttachment> attachments = attachmentMapper.selectAttachmentsByFormId(currentForm.getFormId());
            currentForm.setAttachmentList(attachments);
            return currentForm;
        }
        BcReviewForm newForm = new BcReviewForm();
        newForm.setFormNo(generateFormNo());
        newForm.setStudentId(studentId);
        newForm.setStudentName(student.getNickName());
        newForm.setStudentNo(student.getStudentNo());
        newForm.setIdCard(student.getIdCard());
        newForm.setDeptId(student.getDeptId());
        newForm.setAdmissionYear(student.getAdmissionYear());
        newForm.setFormStatus("0");
        newForm.setVersionNum(1);
        newForm.setIsCurrent("1");
        newForm.setTotalAttachments(0);
        newForm.setReviewedAttachments(0);
        newForm.setCreateBy(SecurityUtils.getUsername());
        formMapper.insertReviewForm(newForm);
        return newForm;
    }

    @Override
    @Transactional
    public int submitForm(Long formId, Long studentId) {
        BcReviewForm form = formMapper.selectReviewFormById(formId);
        if (form == null) {
            throw new ServiceException("申请单不存在");
        }
        if (!form.getStudentId().equals(studentId)) {
            throw new ServiceException("无权操作此申请单");
        }
        if (!"0".equals(form.getFormStatus()) && !"4".equals(form.getFormStatus()) && !"5".equals(form.getFormStatus())) {
            throw new ServiceException("当前状态不允许提交");
        }
        List<BcReviewAttachment> attachments = attachmentMapper.selectAttachmentsByFormId(formId);
        if (attachments == null || attachments.isEmpty()) {
            throw new ServiceException("请先上传至少一个附件再提交");
        }
        form.setFormStatus("1");
        form.setSubmitTime(new Date());
        form.setUpdateBy(SecurityUtils.getUsername());
        int rows = formMapper.updateReviewForm(form);
        formMapper.updateAttachmentCount(formId);
        BcReviewRecord record = buildRecord(form, "submit", null, form.getFormStatus(), "1", null);
        recordMapper.insertRecord(record);
        return rows;
    }

    @Override
    @Transactional
    public int resubmitForm(Long formId, Long studentId) {
        BcReviewForm form = formMapper.selectReviewFormById(formId);
        if (form == null) {
            throw new ServiceException("申请单不存在");
        }
        if (!form.getStudentId().equals(studentId)) {
            throw new ServiceException("无权操作此申请单");
        }
        if (!"4".equals(form.getFormStatus()) && !"5".equals(form.getFormStatus())) {
            throw new ServiceException("只有被驳回或补交中的申请才能重新提交");
        }
        List<BcReviewAttachment> attachments = attachmentMapper.selectAttachmentsByFormId(formId);
        if (attachments == null || attachments.isEmpty()) {
            throw new ServiceException("请先上传附件再重新提交");
        }
        String oldStatus = form.getFormStatus();
        form.setFormStatus("6");
        form.setSubmitTime(new Date());
        form.setUpdateBy(SecurityUtils.getUsername());
        int rows = formMapper.updateReviewForm(form);
        formMapper.updateAttachmentCount(formId);
        BcReviewRecord record = buildRecord(form, "resubmit", null, oldStatus, "6", "学生补交资料重新提交");
        recordMapper.insertRecord(record);
        return rows;
    }

    @Override
    @Transactional
    public int deleteReviewFormByIds(Long[] formIds) {
        int rows = formMapper.deleteReviewFormByIds(formIds);
        for (Long formId : formIds) {
            attachmentMapper.deleteAttachmentsByFormId(formId);
        }
        return rows;
    }

    @Override
    public BcStatisticsVO selectStatistics() {
        BcStatisticsVO stats = formMapper.selectStatistics();
        if (stats == null) {
            stats = new BcStatisticsVO();
            stats.setTotalForms(0L);
            stats.setDraftCount(0L);
            stats.setSubmittedCount(0L);
            stats.setReviewingCount(0L);
            stats.setPassedCount(0L);
            stats.setRejectedCount(0L);
            stats.setSupplyCount(0L);
            stats.setReReviewCount(0L);
            stats.setPassRate(0.0);
            stats.setRejectRate(0.0);
        } else {
            long total = stats.getTotalForms() == null ? 0 : stats.getTotalForms();
            long passed = stats.getPassedCount() == null ? 0 : stats.getPassedCount();
            long rejected = stats.getRejectedCount() == null ? 0 : stats.getRejectedCount();
            stats.setPassRate(total > 0 ? Math.round(passed * 10000.0 / total) / 100.0 : 0.0);
            stats.setRejectRate(total > 0 ? Math.round(rejected * 10000.0 / total) / 100.0 : 0.0);
        }
        return stats;
    }

    @Override
    public Map<String, Long> selectStatusCounts() {
        return formMapper.selectStatusCounts();
    }

    @Override
    @Transactional
    public int autoAssignReviewer(Long formId) {
        BcReviewForm form = formMapper.selectReviewFormById(formId);
        if (form == null || !"1".equals(form.getFormStatus())) {
            return 0;
        }
        List<SysUser> reviewers = userMapper.selectUserListByUserType("02");
        if (reviewers == null || reviewers.isEmpty()) {
            return 0;
        }
        SysUser reviewer = reviewers.get(0);
        form.setReviewerId(reviewer.getUserId());
        form.setReviewerName(reviewer.getNickName());
        form.setFormStatus("2");
        formMapper.updateReviewForm(form);
        BcReviewRecord record = buildRecord(form, "assign", null, "1", "2", "系统自动分配审核员");
        record.setReviewerId(reviewer.getUserId());
        record.setReviewerName(reviewer.getNickName());
        recordMapper.insertRecord(record);
        return 1;
    }

    @Override
    @Transactional
    public int assignReviewer(Long formId, Long reviewerId, String reviewerName) {
        BcReviewForm form = formMapper.selectReviewFormById(formId);
        if (form == null) {
            throw new ServiceException("申请单不存在");
        }
        String oldStatus = form.getFormStatus();
        form.setReviewerId(reviewerId);
        form.setReviewerName(reviewerName);
        if ("1".equals(form.getFormStatus())) {
            form.setFormStatus("2");
        }
        formMapper.updateReviewForm(form);
        BcReviewRecord record = buildRecord(form, "assign", null, oldStatus, form.getFormStatus(), "指定分配审核员：" + reviewerName);
        record.setReviewerId(reviewerId);
        record.setReviewerName(reviewerName);
        recordMapper.insertRecord(record);
        return 1;
    }

    @Override
    public List<BcReviewForm> selectHistoryFormsByStudentId(Long studentId) {
        return formMapper.selectHistoryFormsByStudentId(studentId);
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

    private String generateFormNo() {
        return "BC" + System.currentTimeMillis();
    }
}
