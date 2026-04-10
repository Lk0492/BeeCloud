package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.BcHelpFeedback;
import com.ruoyi.system.mapper.BcHelpFeedbackMapper;
import com.ruoyi.system.service.IBcHelpFeedbackService;

/**
 * 问题反馈Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class BcHelpFeedbackServiceImpl implements IBcHelpFeedbackService {
    @Autowired
    private BcHelpFeedbackMapper feedbackMapper;

    @Override
    public List<BcHelpFeedback> selectHelpFeedbackList(BcHelpFeedback feedback) {
        return feedbackMapper.selectHelpFeedbackList(feedback);
    }

    @Override
    public BcHelpFeedback selectHelpFeedbackById(Long feedbackId) {
        return feedbackMapper.selectHelpFeedbackById(feedbackId);
    }

    @Override
    public int insertHelpFeedback(BcHelpFeedback feedback) {
        return feedbackMapper.insertHelpFeedback(feedback);
    }

    @Override
    public int replyHelpFeedback(BcHelpFeedback feedback) {
        feedback.setReplyBy(SecurityUtils.getUsername());
        feedback.setReplyTime(new Date());
        feedback.setReplyStatus("1");
        feedback.setUpdateBy(SecurityUtils.getUsername());
        return feedbackMapper.updateHelpFeedback(feedback);
    }

    @Override
    public int deleteHelpFeedbackByIds(Long[] feedbackIds) {
        return feedbackMapper.deleteHelpFeedbackByIds(feedbackIds);
    }

    @Override
    public int countPendingFeedback() {
        return feedbackMapper.countPendingFeedback();
    }
}
