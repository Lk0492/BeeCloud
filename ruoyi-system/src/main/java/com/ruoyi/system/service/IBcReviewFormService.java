package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.BcReviewForm;
import com.ruoyi.system.domain.vo.BcStatisticsVO;

/**
 * 资料审核申请单Service接口
 *
 * @author ruoyi
 */
public interface IBcReviewFormService {
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
     * 新增或获取当前申请单（学生端）
     */
    public BcReviewForm getOrCreateCurrentForm(Long studentId);

    /**
     * 提交申请单
     */
    public int submitForm(Long formId, Long studentId);

    /**
     * 补交（重新提交被驳回的申请）
     */
    public int resubmitForm(Long formId, Long studentId);

    /**
     * 删除申请单（逻辑删除）
     */
    public int deleteReviewFormByIds(Long[] formIds);

    /**
     * 统计数据
     */
    public BcStatisticsVO selectStatistics();

    /**
     * 统计各状态数量
     */
    public Map<String, Long> selectStatusCounts();

    /**
     * 自动分配审核员（简单策略：随机分配或按学院分配）
     */
    public int autoAssignReviewer(Long formId);

    /**
     * 手动分配审核员
     */
    public int assignReviewer(Long formId, Long reviewerId, String reviewerName);

    /**
     * 查询历史版本
     */
    public List<BcReviewForm> selectHistoryFormsByStudentId(Long studentId);
}
