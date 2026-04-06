package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.BcDocType;
import com.ruoyi.system.mapper.BcDocTypeMapper;
import com.ruoyi.system.service.IBcDocTypeService;

/**
 * 资料类型Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class BcDocTypeServiceImpl implements IBcDocTypeService {
    @Autowired
    private BcDocTypeMapper docTypeMapper;

    @Override
    public List<BcDocType> selectDocTypeList(BcDocType docType) {
        return docTypeMapper.selectDocTypeList(docType);
    }

    @Override
    public List<BcDocType> selectDocTypeAll() {
        return docTypeMapper.selectDocTypeAll();
    }

    @Override
    public BcDocType selectDocTypeById(Long typeId) {
        return docTypeMapper.selectDocTypeById(typeId);
    }

    @Override
    public BcDocType selectDocTypeByCode(String typeCode) {
        return docTypeMapper.selectDocTypeByCode(typeCode);
    }

    @Override
    public int insertDocType(BcDocType docType) {
        BcDocType exist = docTypeMapper.checkTypeCodeUnique(docType.getTypeCode());
        if (exist != null) {
            throw new ServiceException("类型编码已存在");
        }
        docType.setCreateBy(SecurityUtils.getUsername());
        return docTypeMapper.insertDocType(docType);
    }

    @Override
    public int updateDocType(BcDocType docType) {
        BcDocType exist = docTypeMapper.checkTypeCodeUnique(docType.getTypeCode());
        if (exist != null && !exist.getTypeId().equals(docType.getTypeId())) {
            throw new ServiceException("类型编码已存在");
        }
        docType.setUpdateBy(SecurityUtils.getUsername());
        return docTypeMapper.updateDocType(docType);
    }

    @Override
    public int deleteDocTypeByIds(Long[] typeIds) {
        return docTypeMapper.deleteDocTypeByIds(typeIds);
    }

    @Override
    public boolean checkTypeCodeUnique(BcDocType docType) {
        BcDocType exist = docTypeMapper.checkTypeCodeUnique(docType.getTypeCode());
        return exist == null || exist.getTypeId().equals(docType.getTypeId());
    }
}
