package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BcReviewRecord;

/**
 * 资料审核记录Service接口
 *
 * @author ruoyi
 */
public interface IBcReviewRecordService {
    /**
     * 查询审核记录列表
     */
    public List<BcReviewRecord> selectRecordList(BcReviewRecord record);

    /**
     * 通过申请单ID查询审核记录
     */
    public List<BcReviewRecord> selectRecordsByFormId(Long formId);

    /**
     * 通过ID查询审核记录
     */
    public BcReviewRecord selectRecordById(Long recordId);

    /**
     * 新增审核记录
     */
    public int insertRecord(BcReviewRecord record);

    /**
     * 删除审核记录
     */
    public int deleteRecordByIds(Long[] recordIds);
}
