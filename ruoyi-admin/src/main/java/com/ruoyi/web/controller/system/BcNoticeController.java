package com.ruoyi.web.controller.system;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.BcNotice;
import com.ruoyi.system.service.IBcNoticeService;

/**
 * 资料审核消息通知 Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/review/notice")
public class BcNoticeController extends BaseController {
    @Autowired
    private IBcNoticeService noticeService;

    /**
     * 查询我的消息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BcNotice notice) {
        Long userId = SecurityUtils.getUserId();
        notice.setReceiveUserId(userId);
        startPage();
        List<BcNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 获取未读消息数量
     */
    @GetMapping("/unreadCount")
    public AjaxResult unreadCount() {
        Long userId = SecurityUtils.getUserId();
        return success(noticeService.selectUnreadCount(userId));
    }

    /**
     * 获取消息详情
     */
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable Long noticeId) {
        BcNotice notice = noticeService.selectNoticeById(noticeId);
        if (notice != null && "0".equals(notice.getIsRead())
                && notice.getReceiveUserId().equals(SecurityUtils.getUserId())) {
            noticeService.markAsRead(noticeId);
        }
        return success(notice);
    }

    /**
     * 标记单条已读
     */
    @Log(title = "消息通知", businessType = BusinessType.UPDATE)
    @PutMapping("/read/{noticeId}")
    public AjaxResult markRead(@PathVariable Long noticeId) {
        return toAjax(noticeService.markAsRead(noticeId));
    }

    /**
     * 标记全部已读
     */
    @Log(title = "消息通知", businessType = BusinessType.UPDATE)
    @PutMapping("/readAll")
    public AjaxResult markAllRead() {
        Long userId = SecurityUtils.getUserId();
        return toAjax(noticeService.markAllAsRead(userId));
    }

    /**
     * 删除消息
     */
    @Log(title = "消息通知", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public AjaxResult remove(@PathVariable Long[] noticeIds) {
        return toAjax(noticeService.deleteNoticeByIds(noticeIds));
    }
}
