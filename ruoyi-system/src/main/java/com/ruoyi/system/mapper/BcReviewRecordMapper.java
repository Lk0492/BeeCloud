package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BcReviewRecord;

/**
 * 资料审核记录Mapper接口
 *
 * @author ruoyi
 */
public interface BcReviewRecordMapper {
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

    /**
     * 统计审核员审核数量
     */
    public Long countByReviewerId(Long reviewerId);
}
