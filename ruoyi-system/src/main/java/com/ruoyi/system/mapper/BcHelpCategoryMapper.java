package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BcHelpCategory;

/**
 * 帮助问题分类Mapper接口
 *
 * @author ruoyi
 */
public interface BcHelpCategoryMapper {
    /**
     * 查询分类列表
     */
    public List<BcHelpCategory> selectHelpCategoryList(BcHelpCategory category);

    /**
     * 查询所有正常状态的分类
     */
    public List<BcHelpCategory> selectHelpCategoryAll();

    /**
     * 通过ID查询分类
     */
    public BcHelpCategory selectHelpCategoryById(Long categoryId);

    /**
     * 通过编码查询分类
     */
    public BcHelpCategory selectHelpCategoryByCode(String categoryCode);

    /**
     * 新增分类
     */
    public int insertHelpCategory(BcHelpCategory category);

    /**
     * 修改分类
     */
    public int updateHelpCategory(BcHelpCategory category);

    /**
     * 删除分类
     */
    public int deleteHelpCategoryByIds(Long[] categoryIds);

    /**
     * 校验分类编码唯一性
     */
    public BcHelpCategory checkCategoryCodeUnique(String categoryCode);

    /**
     * 查询子分类数量
     */
    public int selectChildrenCountById(Long categoryId);
}
