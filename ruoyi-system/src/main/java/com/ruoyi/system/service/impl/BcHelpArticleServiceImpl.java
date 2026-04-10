package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.BcHelpArticle;
import com.ruoyi.system.domain.BcHelpViewLog;
import com.ruoyi.system.mapper.BcHelpArticleMapper;
import com.ruoyi.system.mapper.BcHelpViewLogMapper;
import com.ruoyi.system.service.IBcHelpArticleService;

/**
 * 帮助文章Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class BcHelpArticleServiceImpl implements IBcHelpArticleService {
    @Autowired
    private BcHelpArticleMapper articleMapper;

    @Autowired
    private BcHelpViewLogMapper viewLogMapper;

    @Override
    public List<BcHelpArticle> selectHelpArticleList(BcHelpArticle article) {
        return articleMapper.selectHelpArticleList(article);
    }

    @Override
    public List<BcHelpArticle> selectHelpArticleAll() {
        return articleMapper.selectHelpArticleAll();
    }

    @Override
    public List<BcHelpArticle> selectHotArticleList(int limit) {
        return articleMapper.selectHotArticleList(limit);
    }

    @Override
    public List<BcHelpArticle> fuzzySearchArticles(String keyword) {
        return articleMapper.fuzzySearchArticles(keyword);
    }

    @Override
    public BcHelpArticle selectHelpArticleById(Long articleId) {
        return articleMapper.selectHelpArticleById(articleId);
    }

    @Override
    public int insertHelpArticle(BcHelpArticle article) {
        article.setCreateBy(getUsername());
        return articleMapper.insertHelpArticle(article);
    }

    @Override
    public int updateHelpArticle(BcHelpArticle article) {
        article.setUpdateBy(getUsername());
        return articleMapper.updateHelpArticle(article);
    }

    @Override
    public int deleteHelpArticleByIds(Long[] articleIds) {
        return articleMapper.deleteHelpArticleByIds(articleIds);
    }

    @Override
    public void incrementViewCount(Long articleId) {
        articleMapper.incrementViewCount(articleId);
    }

    private String getUsername() {
        try {
            return com.ruoyi.common.utils.SecurityUtils.getUsername();
        } catch (Exception e) {
            return "anonymous";
        }
    }
}
