package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BcHelpFeedback;

/**
 * 问题反馈Mapper接口
 *
 * @author ruoyi
 */
public interface BcHelpFeedbackMapper {
    /**
     * 查询反馈列表
     */
    public List<BcHelpFeedback> selectHelpFeedbackList(BcHelpFeedback feedback);

    /**
     * 通过ID查询反馈
     */
    public BcHelpFeedback selectHelpFeedbackById(Long feedbackId);

    /**
     * 新增反馈
     */
    public int insertHelpFeedback(BcHelpFeedback feedback);

    /**
     * 修改反馈（回复）
     */
    public int updateHelpFeedback(BcHelpFeedback feedback);

    /**
     * 删除反馈
     */
    public int deleteHelpFeedbackByIds(Long[] feedbackIds);

    /**
     * 统计待回复数量
     */
    public int countPendingFeedback();
}
