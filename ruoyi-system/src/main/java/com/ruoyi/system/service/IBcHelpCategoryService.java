package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BcHelpCategory;

/**
 * 帮助问题分类Service接口
 *
 * @author ruoyi
 */
public interface IBcHelpCategoryService {
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
    public boolean checkCategoryCodeUnique(BcHelpCategory category);

    /**
     * 构建分类树
     */
    public List<BcHelpCategory> buildHelpCategoryTree(List<BcHelpCategory> categories);
}
