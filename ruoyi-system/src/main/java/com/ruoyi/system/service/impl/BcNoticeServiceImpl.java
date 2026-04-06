package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.enums.BcNoticeType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.BcNotice;
import com.ruoyi.system.mapper.BcNoticeMapper;
import com.ruoyi.system.service.IBcNoticeService;

/**
 * 系统通知Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class BcNoticeServiceImpl implements IBcNoticeService {
    @Autowired
    private BcNoticeMapper noticeMapper;

    @Override
    public List<BcNotice> selectNoticeList(BcNotice notice) {
        return noticeMapper.selectNoticeList(notice);
    }

    @Override
    public Long selectUnreadCount(Long userId) {
        return noticeMapper.selectUnreadCount(userId);
    }

    @Override
    public BcNotice selectNoticeById(Long noticeId) {
        return noticeMapper.selectNoticeById(noticeId);
    }

    @Override
    public int insertNotice(BcNotice notice) {
        return noticeMapper.insertNotice(notice);
    }

    @Override
    public int insertNotices(List<BcNotice> notices) {
        if (notices == null || notices.isEmpty()) {
            return 0;
        }
        return noticeMapper.insertNotices(notices);
    }

    @Override
    public void sendPassNotice(Long formId, Long userId, String userName) {
        BcNotice notice = new BcNotice();
        notice.setNoticeType(BcNoticeType.REVIEW_PASS.getCode());
        notice.setTitle("资料审核通过");
        notice.setContent("您的入学资料审核已通过，请注意后续入学安排。申请单编号：" + formId);
        notice.setFormId(formId);
        notice.setReceiveUserId(userId);
        notice.setReceiveUserName(userName);
        notice.setCreateBy(SecurityUtils.getUsername());
        noticeMapper.insertNotice(notice);
    }

    @Override
    public void sendRejectNotice(Long formId, Long userId, String userName, String reason) {
        BcNotice notice = new BcNotice();
        notice.setNoticeType(BcNoticeType.REVIEW_REJECT.getCode());
        notice.setTitle("资料审核驳回");
        notice.setContent("您的入学资料审核未通过，原因：" + reason + "。请登录系统查看详情并重新提交。申请单编号：" + formId);
        notice.setFormId(formId);
        notice.setReceiveUserId(userId);
        notice.setReceiveUserName(userName);
        notice.setCreateBy(SecurityUtils.getUsername());
        noticeMapper.insertNotice(notice);
    }

    @Override
    public void sendSupplyNotice(Long formId, Long userId, String userName, String reason) {
        BcNotice notice = new BcNotice();
        notice.setNoticeType(BcNoticeType.SUPPLY.getCode());
        notice.setTitle("需要补交资料");
        notice.setContent("您的入学资料需要补交，原因：" + reason + "。请登录系统查看详情并补交相关材料。申请单编号：" + formId);
        notice.setFormId(formId);
        notice.setReceiveUserId(userId);
        notice.setReceiveUserName(userName);
        notice.setCreateBy(SecurityUtils.getUsername());
        noticeMapper.insertNotice(notice);
    }

    @Override
    public int markAsRead(Long noticeId) {
        return noticeMapper.markAsRead(noticeId);
    }

    @Override
    public int markAllAsRead(Long userId) {
        return noticeMapper.markAllAsRead(userId);
    }

    @Override
    public int deleteNoticeByIds(Long[] noticeIds) {
        return noticeMapper.deleteNoticeByIds(noticeIds);
    }
}
