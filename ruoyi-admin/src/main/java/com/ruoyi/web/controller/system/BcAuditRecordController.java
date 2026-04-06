package com.ruoyi.web.controller.system;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BcReviewRecord;
import com.ruoyi.system.service.IBcReviewRecordService;

/**
 * 审核记录 Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/review/auditRecord")
public class BcAuditRecordController extends BaseController {
    @Autowired
    private IBcReviewRecordService recordService;

    /**
     * 查询审核记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BcReviewRecord record) {
        startPage();
        List<BcReviewRecord> list = recordService.selectRecordList(record);
        return getDataTable(list);
    }

    /**
     * 导出审核记录
     */
    @Log(title = "审核记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BcReviewRecord record) {
        List<BcReviewRecord> list = recordService.selectRecordList(record);
        ExcelUtil<BcReviewRecord> util = new ExcelUtil<BcReviewRecord>(BcReviewRecord.class);
        util.exportExcel(response, list, "审核记录数据");
    }

    /**
     * 获取审核记录详情
     */
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable Long recordId) {
        return success(recordService.selectRecordById(recordId));
    }

    /**
     * 查询某个申请单的所有审核记录
     */
    @GetMapping(value = "/form/{formId}")
    public AjaxResult getRecordsByFormId(@PathVariable Long formId) {
        return success(recordService.selectRecordsByFormId(formId));
    }
}
