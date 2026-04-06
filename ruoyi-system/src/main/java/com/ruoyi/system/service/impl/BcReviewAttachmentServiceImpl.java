package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.BcReviewAttachment;
import com.ruoyi.system.mapper.BcReviewAttachmentMapper;
import com.ruoyi.system.service.IBcReviewAttachmentService;

/**
 * 资料审核附件Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class BcReviewAttachmentServiceImpl implements IBcReviewAttachmentService {
    @Autowired
    private BcReviewAttachmentMapper attachmentMapper;

    @Override
    public List<BcReviewAttachment> selectAttachmentList(BcReviewAttachment attachment) {
        return attachmentMapper.selectAttachmentList(attachment);
    }

    @Override
    public List<BcReviewAttachment> selectAttachmentsByFormId(Long formId) {
        return attachmentMapper.selectAttachmentsByFormId(formId);
    }

    @Override
    public BcReviewAttachment selectAttachmentById(Long detailId) {
        return attachmentMapper.selectAttachmentById(detailId);
    }

    @Override
    public int insertAttachment(BcReviewAttachment attachment) {
        return attachmentMapper.insertAttachment(attachment);
    }

    @Override
    public int insertAttachments(List<BcReviewAttachment> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            return 0;
        }
        attachmentMapper.insertAttachments(attachments);
        return attachments.size();
    }

    @Override
    public int updateAttachment(BcReviewAttachment attachment) {
        return attachmentMapper.updateAttachment(attachment);
    }

    @Override
    public int deleteAttachmentByIds(Long[] detailIds) {
        return attachmentMapper.deleteAttachmentByIds(detailIds);
    }

    @Override
    public int deleteAttachmentsByFormId(Long formId) {
        return attachmentMapper.deleteAttachmentsByFormId(formId);
    }

    @Override
    public int countByFormIdAndCategory(Long formId, String fileCategory) {
        return attachmentMapper.countByFormIdAndCategory(formId, fileCategory);
    }
}
