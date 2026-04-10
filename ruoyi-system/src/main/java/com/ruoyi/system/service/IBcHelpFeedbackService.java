package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BcHelpFeedback;

/**
 * 问题反馈Service接口
 *
 * @author ruoyi
 */
public interface IBcHelpFeedbackService {
    /**
     * 查询反馈列表
     */
    public List<BcHelpFeedback> selectHelpFeedbackList(BcHelpFeedback feedback);

    /**
     * 通过ID查询反馈
     */
    public BcHelpFeedback selectHelpFeedbackById(Long feedbackId);

    /**
     * 新增反馈（用户提交问题）
     */
    public int insertHelpFeedback(BcHelpFeedback feedback);

    /**
     * 回复反馈（管理员）
     */
    public int replyHelpFeedback(BcHelpFeedback feedback);

    /**
     * 删除反馈
     */
    public int deleteHelpFeedbackByIds(Long[] feedbackIds);

    /**
     * 统计待回复数量
     */
    public int countPendingFeedback();
}
