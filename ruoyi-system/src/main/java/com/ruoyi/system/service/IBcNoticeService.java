package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BcNotice;

/**
 * 系统通知Service接口
 *
 * @author ruoyi
 */
public interface IBcNoticeService {
    /**
     * 查询通知列表
     */
    public List<BcNotice> selectNoticeList(BcNotice notice);

    /**
     * 查询用户的未读通知数量
     */
    public Long selectUnreadCount(Long userId);

    /**
     * 通过ID查询通知
     */
    public BcNotice selectNoticeById(Long noticeId);

    /**
     * 新增通知
     */
    public int insertNotice(BcNotice notice);

    /**
     * 批量新增通知
     */
    public int insertNotices(List<BcNotice> notices);

    /**
     * 发送审核通过通知
     */
    public void sendPassNotice(Long formId, Long userId, String userName);

    /**
     * 发送审核驳回通知
     */
    public void sendRejectNotice(Long formId, Long userId, String userName, String reason);

    /**
     * 发送补交通知
     */
    public void sendSupplyNotice(Long formId, Long userId, String userName, String reason);

    /**
     * 标记已读
     */
    public int markAsRead(Long noticeId);

    /**
     * 批量标记已读
     */
    public int markAllAsRead(Long userId);

    /**
     * 删除通知
     */
    public int deleteNoticeByIds(Long[] noticeIds);
}
