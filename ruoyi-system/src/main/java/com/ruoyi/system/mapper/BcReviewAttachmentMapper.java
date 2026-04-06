package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BcReviewAttachment;

/**
 * 资料审核附件Mapper接口
 *
 * @author ruoyi
 */
public interface BcReviewAttachmentMapper {
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
     * 批量更新附件状态
     */
    public int updateAttachmentsStatus(List<Long> detailIds, String status, Long reviewerId, String reviewerName, String remark);

    /**
     * 删除附件（逻辑删除）
     */
    public int deleteAttachmentByIds(Long[] detailIds);

    /**
     * 通过申请单ID删除附件（逻辑删除）
     */
    public int deleteAttachmentsByFormId(Long formId);

    /**
     * 通过申请单ID删除所有附件
     */
    public int physicallyDeleteAttachmentsByFormId(Long formId);

    /**
     * 统计某类型的附件数量
     */
    public int countByFormIdAndCategory(Long formId, String fileCategory);
}
