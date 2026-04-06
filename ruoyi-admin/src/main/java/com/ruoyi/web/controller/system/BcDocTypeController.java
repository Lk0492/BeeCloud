package com.ruoyi.web.controller.system;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.BcDocType;
import com.ruoyi.system.service.IBcDocTypeService;

/**
 * 资料类型 Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/review/docType")
public class BcDocTypeController extends BaseController {
    @Autowired
    private IBcDocTypeService docTypeService;

    /**
     * 查询资料类型列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BcDocType docType) {
        startPage();
        List<BcDocType> list = docTypeService.selectDocTypeList(docType);
        return getDataTable(list);
    }

    /**
     * 查询所有资料类型（供下拉选择）
     */
    @GetMapping("/optionselect")
    public AjaxResult optionselect() {
        List<BcDocType> list = docTypeService.selectDocTypeAll();
        return success(list);
    }

    /**
     * 导出资料类型
     */
    @Log(title = "资料类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BcDocType docType) {
        List<BcDocType> list = docTypeService.selectDocTypeList(docType);
        ExcelUtil<BcDocType> util = new ExcelUtil<BcDocType>(BcDocType.class);
        util.exportExcel(response, list, "资料类型数据");
    }

    /**
     * 获取资料类型详情
     */
    @GetMapping(value = "/{typeId}")
    public AjaxResult getInfo(@PathVariable Long typeId) {
        return success(docTypeService.selectDocTypeById(typeId));
    }

    /**
     * 新增资料类型
     */
    @Log(title = "资料类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody BcDocType docType) {
        if (!docTypeService.checkTypeCodeUnique(docType)) {
            return error("新增资料类型'" + docType.getTypeName() + "'失败，类型编码已存在");
        }
        docType.setCreateBy(getUsername());
        return toAjax(docTypeService.insertDocType(docType));
    }

    /**
     * 修改资料类型
     */
    @Log(title = "资料类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody BcDocType docType) {
        if (!docTypeService.checkTypeCodeUnique(docType)) {
            return error("修改资料类型'" + docType.getTypeName() + "'失败，类型编码已存在");
        }
        docType.setUpdateBy(getUsername());
        return toAjax(docTypeService.updateDocType(docType));
    }

    /**
     * 删除资料类型
     */
    @Log(title = "资料类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{typeIds}")
    public AjaxResult remove(@PathVariable Long[] typeIds) {
        return toAjax(docTypeService.deleteDocTypeByIds(typeIds));
    }
}
