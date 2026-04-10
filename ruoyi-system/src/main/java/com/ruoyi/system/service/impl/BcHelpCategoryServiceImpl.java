package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.BcHelpCategory;
import com.ruoyi.system.mapper.BcHelpCategoryMapper;
import com.ruoyi.system.service.IBcHelpCategoryService;

/**
 * 帮助问题分类Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class BcHelpCategoryServiceImpl implements IBcHelpCategoryService {
    @Autowired
    private BcHelpCategoryMapper categoryMapper;

    @Override
    public List<BcHelpCategory> selectHelpCategoryList(BcHelpCategory category) {
        return categoryMapper.selectHelpCategoryList(category);
    }

    @Override
    public List<BcHelpCategory> selectHelpCategoryAll() {
        return categoryMapper.selectHelpCategoryAll();
    }

    @Override
    public BcHelpCategory selectHelpCategoryById(Long categoryId) {
        return categoryMapper.selectHelpCategoryById(categoryId);
    }

    @Override
    public int insertHelpCategory(BcHelpCategory category) {
        BcHelpCategory exist = categoryMapper.checkCategoryCodeUnique(category.getCategoryCode());
        if (exist != null) {
            throw new ServiceException("分类编码已存在");
        }
        category.setCreateBy(SecurityUtils.getUsername());
        return categoryMapper.insertHelpCategory(category);
    }

    @Override
    public int updateHelpCategory(BcHelpCategory category) {
        BcHelpCategory exist = categoryMapper.checkCategoryCodeUnique(category.getCategoryCode());
        if (exist != null && !exist.getCategoryId().equals(category.getCategoryId())) {
            throw new ServiceException("分类编码已存在");
        }
        category.setUpdateBy(SecurityUtils.getUsername());
        return categoryMapper.updateHelpCategory(category);
    }

    @Override
    public int deleteHelpCategoryByIds(Long[] categoryIds) {
        for (Long categoryId : categoryIds) {
            // 检查是否有子分类
            if (categoryMapper.selectChildrenCountById(categoryId) > 0) {
                throw new ServiceException("存在子分类，不允许删除");
            }
        }
        return categoryMapper.deleteHelpCategoryByIds(categoryIds);
    }

    @Override
    public boolean checkCategoryCodeUnique(BcHelpCategory category) {
        BcHelpCategory exist = categoryMapper.checkCategoryCodeUnique(category.getCategoryCode());
        return exist == null || exist.getCategoryId().equals(category.getCategoryId());
    }

    @Override
    public List<BcHelpCategory> buildHelpCategoryTree(List<BcHelpCategory> categories) {
        List<BcHelpCategory> returnList = new ArrayList<>();
        List<Long> tempList = categories.stream().map(BcHelpCategory::getCategoryId).collect(Collectors.toList());
        for (BcHelpCategory category : categories) {
            if (!tempList.contains(category.getParentId())) {
                recursionFn(categories, category, returnList);
            }
        }
        if (returnList.isEmpty()) {
            returnList = categories;
        }
        return returnList;
    }

    private void recursionFn(List<BcHelpCategory> list, BcHelpCategory t, List<BcHelpCategory> returnList) {
        returnList.add(t);
        List<BcHelpCategory> childList = getChildList(list, t);
        t.setChildren(childList);
        Iterator<BcHelpCategory> it = childList.iterator();
        while (it.hasNext()) {
            BcHelpCategory n = it.next();
            if (getChildList(list, n).size() > 0) {
                recursionFn(list, n, getChildList(list, n));
            }
        }
    }

    private List<BcHelpCategory> getChildList(List<BcHelpCategory> list, BcHelpCategory t) {
        List<BcHelpCategory> childList = new ArrayList<>();
        Iterator<BcHelpCategory> it = list.iterator();
        while (it.hasNext()) {
            BcHelpCategory n = it.next();
            if (n.getParentId() != null && n.getParentId().longValue() == t.getCategoryId().longValue()) {
                childList.add(n);
            }
        }
        return childList;
    }
}
