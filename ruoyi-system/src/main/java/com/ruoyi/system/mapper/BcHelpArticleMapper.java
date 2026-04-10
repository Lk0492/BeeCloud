package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BcHelpArticle;

/**
 * 帮助文章Mapper接口
 *
 * @author ruoyi
 */
public interface BcHelpArticleMapper {
    /**
     * 查询文章列表
     */
    public List<BcHelpArticle> selectHelpArticleList(BcHelpArticle article);

    /**
     * 查询所有正常状态的文章
     */
    public List<BcHelpArticle> selectHelpArticleAll();

    /**
     * 查询热门文章
     */
    public List<BcHelpArticle> selectHotArticleList(int limit);

    /**
     * 模糊搜索文章
     */
    public List<BcHelpArticle> fuzzySearchArticles(String keyword);

    /**
     * 通过ID查询文章
     */
    public BcHelpArticle selectHelpArticleById(Long articleId);

    /**
     * 新增文章
     */
    public int insertHelpArticle(BcHelpArticle article);

    /**
     * 修改文章
     */
    public int updateHelpArticle(BcHelpArticle article);

    /**
     * 删除文章
     */
    public int deleteHelpArticleByIds(Long[] articleIds);

    /**
     * 增加浏览次数
     */
    public int incrementViewCount(Long articleId);

    /**
     * 校验分类下是否有文章
     */
    public int countByCategoryId(Long categoryId);
}
