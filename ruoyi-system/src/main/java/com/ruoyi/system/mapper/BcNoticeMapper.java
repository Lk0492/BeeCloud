package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BcNotice;

/**
 * 系统通知Mapper接口
 *
 * @author ruoyi
 */
public interface BcNoticeMapper {
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
