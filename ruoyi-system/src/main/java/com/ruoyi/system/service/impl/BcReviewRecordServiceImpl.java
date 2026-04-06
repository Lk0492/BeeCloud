package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.BcReviewRecord;
import com.ruoyi.system.mapper.BcReviewRecordMapper;
import com.ruoyi.system.service.IBcReviewRecordService;

/**
 * 资料审核记录Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class BcReviewRecordServiceImpl implements IBcReviewRecordService {
    @Autowired
    private BcReviewRecordMapper recordMapper;

    @Override
    public List<BcReviewRecord> selectRecordList(BcReviewRecord record) {
        return recordMapper.selectRecordList(record);
    }

    @Override
    public List<BcReviewRecord> selectRecordsByFormId(Long formId) {
        return recordMapper.selectRecordsByFormId(formId);
    }

    @Override
    public BcReviewRecord selectRecordById(Long recordId) {
        return recordMapper.selectRecordById(recordId);
    }

    @Override
    public int insertRecord(BcReviewRecord record) {
        return recordMapper.insertRecord(record);
    }

    @Override
    public int deleteRecordByIds(Long[] recordIds) {
        return recordMapper.deleteRecordByIds(recordIds);
    }
}
