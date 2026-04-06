package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BcDocType;

/**
 * 资料类型Mapper接口
 *
 * @author ruoyi
 */
public interface BcDocTypeMapper {
    /**
     * 查询资料类型列表
     */
    public List<BcDocType> selectDocTypeList(BcDocType docType);

    /**
     * 查询所有资料类型
     */
    public List<BcDocType> selectDocTypeAll();

    /**
     * 通过ID查询资料类型
     */
    public BcDocType selectDocTypeById(Long typeId);

    /**
     * 通过编码查询资料类型
     */
    public BcDocType selectDocTypeByCode(String typeCode);

    /**
     * 新增资料类型
     */
    public int insertDocType(BcDocType docType);

    /**
     * 修改资料类型
     */
    public int updateDocType(BcDocType docType);

    /**
     * 删除资料类型
     */
    public int deleteDocTypeByIds(Long[] typeIds);

    /**
     * 校验类型编码唯一性
     */
    public BcDocType checkTypeCodeUnique(String typeCode);
}
