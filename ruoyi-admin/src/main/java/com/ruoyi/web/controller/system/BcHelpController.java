package com.ruoyi.web.controller.system;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.system.domain.BcHelpArticle;
import com.ruoyi.system.domain.BcHelpCategory;
import com.ruoyi.system.domain.BcHelpFeedback;
import com.ruoyi.system.domain.BcHelpViewLog;
import com.ruoyi.system.service.IBcHelpArticleService;
import com.ruoyi.system.service.IBcHelpCategoryService;
import com.ruoyi.system.service.IBcHelpFeedbackService;
import com.ruoyi.system.mapper.BcHelpViewLogMapper;

/**
 * 学生端咨询帮助 Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/help")
public class BcHelpController extends BaseController {

    @Autowired
    private IBcHelpArticleService articleService;

    @Autowired
    private IBcHelpCategoryService categoryService;

    @Autowired
    private IBcHelpFeedbackService feedbackService;

    @Autowired
    private BcHelpViewLogMapper viewLogMapper;

    /**
     * 获取热门问题
     */
    @GetMapping("/hot")
    public AjaxResult getHotArticles() {
        List<BcHelpArticle> hotList = articleService.selectHotArticleList(8);
        return success(hotList);
    }

    /**
     * 获取所有分类
     */
    @GetMapping("/categories")
    public AjaxResult getCategories() {
        List<BcHelpCategory> categories = categoryService.selectHelpCategoryAll();
        return success(categories);
    }

    /**
     * 按分类查询文章
     */
    @GetMapping("/byCategory/{categoryId}")
    public AjaxResult getArticlesByCategory(@PathVariable Long categoryId) {
        BcHelpArticle article = new BcHelpArticle();
        article.setCategoryId(categoryId);
        List<BcHelpArticle> list = articleService.selectHelpArticleAll();
        return success(list);
    }

    /**
     * 关键词模糊搜索文章
     */
    @GetMapping("/search")
    public AjaxResult searchArticles(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return success();
        }
        List<BcHelpArticle> list = articleService.fuzzySearchArticles(keyword.trim());
        return success(list);
    }

    /**
     * 获取文章详情（增加浏览次数）
     */
    @GetMapping("/detail/{articleId}")
    public AjaxResult getArticleDetail(@PathVariable Long articleId) {
        BcHelpArticle article = articleService.selectHelpArticleById(articleId);
        if (article == null) {
            return error("文章不存在");
        }
        // 增加浏览次数
        articleService.incrementViewCount(articleId);
        // 记录浏览日志
        recordViewLog(articleId);
        return success(article);
    }

    /**
     * 提交问题反馈（当知识库未匹配到答案时）
     */
    @Log(title = "问题反馈", businessType = BusinessType.INSERT)
    @PostMapping("/feedback")
    public AjaxResult submitFeedback(@RequestBody BcHelpFeedback feedback) {
        // 获取当前登录用户信息
        try {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            if (loginUser != null && loginUser.getUser() != null) {
                feedback.setUserId(loginUser.getUser().getUserId());
                feedback.setUserName(loginUser.getUser().getNickName());
            }
        } catch (Exception e) {
            // 未登录用户也可以提交反馈
        }
        feedback.setReplyStatus("0");
        int rows = feedbackService.insertHelpFeedback(feedback);
        if (rows > 0) {
            return success("反馈已提交，我们会尽快回复您！");
        }
        return error("提交失败，请稍后重试");
    }

    /**
     * 获取我的反馈记录
     */
    @GetMapping("/myFeedback")
    public AjaxResult getMyFeedback() {
        try {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            if (loginUser != null && loginUser.getUser() != null) {
                BcHelpFeedback feedback = new BcHelpFeedback();
                feedback.setUserId(loginUser.getUser().getUserId());
                List<BcHelpFeedback> list = feedbackService.selectHelpFeedbackList(feedback);
                return success(list);
            }
        } catch (Exception e) {
        }
        return success();
    }

    /**
     * 记录浏览日志
     */
    private void recordViewLog(Long articleId) {
        try {
            BcHelpViewLog viewLog = new BcHelpViewLog();
            viewLog.setArticleId(articleId);
            viewLog.setIpAddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
            viewLog.setCreateTime(new Date());
            try {
                LoginUser loginUser = SecurityUtils.getLoginUser();
                if (loginUser != null && loginUser.getUser() != null) {
                    viewLog.setUserId(loginUser.getUser().getUserId());
                    viewLog.setUserName(loginUser.getUser().getNickName());
                }
            } catch (Exception e) {
                // 未登录
            }
            viewLogMapper.insertViewLog(viewLog);
        } catch (Exception e) {
            // 忽略日志记录失败
        }
    }
}
