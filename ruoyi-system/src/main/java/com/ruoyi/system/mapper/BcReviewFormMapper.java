package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.BcReviewForm;
import com.ruoyi.system.domain.vo.BcStatisticsVO;

/**
 * 资料审核申请单Mapper接口
 *
 * @author ruoyi
 */
public interface BcReviewFormMapper {
    /**
     * 查询申请单列表
     */
    public List<BcReviewForm> selectReviewFormList(BcReviewForm form);

    /**
     * 查询学生的当前版本申请单
     */
    public BcReviewForm selectCurrentFormByStudentId(Long studentId);

    /**
     * 查询申请单详情（含附件）
     */
    public BcReviewForm selectReviewFormWithAttachments(Long formId);

    /**
     * 通过ID查询申请单
     */
    public BcReviewForm selectReviewFormById(Long formId);

    /**
     * 新增申请单
     */
    public int insertReviewForm(BcReviewForm form);

    /**
     * 修改申请单
     */
    public int updateReviewForm(BcReviewForm form);

    /**
     * 批量删除申请单（逻辑删除）
     */
    public int deleteReviewFormByIds(Long[] formIds);

    /**
     * 统计各状态数量
     */
    public Map<String, Long> selectStatusCounts();

    /**
     * 查询审核员待审核数量
     */
    public Long selectPendingCountByReviewerId(Long reviewerId);

    /**
     * 统计数据
     */
    public BcStatisticsVO selectStatistics();

    /**
     * 更新附件数量
     */
    public int updateAttachmentCount(Long formId);

    /**
     * 根据申请单ID更新审核员信息
     */
    public int updateReviewer(Long formId, Long reviewerId, String reviewerName);

    /**
     * 查询需要分配的申请单列表（状态为已提交且未分配审核员）
     */
    public List<BcReviewForm> selectUnassignedFormList();

    /**
     * 查询历史版本申请单
     */
    public List<BcReviewForm> selectHistoryFormsByStudentId(Long studentId);
}
