package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BcReviewAttachment;

/**
 * 资料审核附件Service接口
 *
 * @author ruoyi
 */
public interface IBcReviewAttachmentService {
    /**
     * 查询附件列表
     */
    public List<BcReviewAttachment> selectAttachmentList(BcReviewAttachment attachment);

    /**
     * 通过申请单ID查询附件列表
     */
    public List<BcReviewAttachment> selectAttachmentsByFormId(Long formId);

    /**
     * 通过ID查询附件
     */
    public BcReviewAttachment selectAttachmentById(Long detailId);

    /**
     * 新增附件
     */
    public int insertAttachment(BcReviewAttachment attachment);

    /**
     * 批量新增附件
     */
    public int insertAttachments(List<BcReviewAttachment> attachments);

    /**
     * 修改附件
     */
    public int updateAttachment(BcReviewAttachment attachment);

    /**
     * 删除附件（逻辑删除）
     */
    public int deleteAttachmentByIds(Long[] detailIds);

    /**
     * 通过申请单ID删除所有附件（逻辑删除）
     */
    public int deleteAttachmentsByFormId(Long formId);

    /**
     * 统计某类型的附件数量
     */
    public int countByFormIdAndCategory(Long formId, String fileCategory);
}
