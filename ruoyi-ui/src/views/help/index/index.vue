<template>
  <div class="help-container">
    <!-- 顶部搜索区 -->
    <div class="help-header">
      <div class="header-content">
        <h2 class="header-title">
          <i class="el-icon-question"></i> 咨询帮助
        </h2>
        <p class="header-subtitle">快速解决您在使用系统中遇到的问题</p>
        <div class="search-box">
          <el-input
            v-model="searchKeyword"
            placeholder="请输入问题关键词搜索..."
            size="large"
            clearable
            @keyup.enter.native="handleSearch"
            @clear="handleClear"
          >
            <i slot="prefix" class="el-input__icon el-icon-search"></i>
            <el-button slot="append" type="primary" @click="handleSearch" icon="el-icon-search">搜索</el-button>
          </el-input>
        </div>
      </div>
    </div>

    <!-- 主体内容区 -->
    <div class="help-body">
      <!-- 搜索结果 -->
      <div v-if="isSearching">
        <div class="section-title">
          <span>搜索结果 "<span class="highlight">{{ searchKeyword }}</span>"</span>
          <span class="result-count">共 {{ searchResults.length }} 条</span>
        </div>
        <div v-if="searchResults.length > 0" class="result-list">
          <div v-for="item in searchResults" :key="item.articleId" class="result-item" @click="viewArticle(item)">
            <div class="result-question">{{ item.question }}</div>
            <div class="result-answer" v-html="getAnswerSnippet(item.answer)"></div>
            <div class="result-meta">
              <el-tag size="mini" type="info">{{ item.categoryName }}</el-tag>
              <span class="view-count"><i class="el-icon-view"></i> {{ item.viewCount }}</span>
            </div>
          </div>
        </div>
        <el-empty v-else description="未找到相关问题，您可以提交反馈，我们会尽快回复您"></el-empty>

        <div class="no-answer-tip">
          <el-alert title="没有找到满意答案？" type="info" :closable="false" show-icon>
            <template slot>
              <span>您可以 <el-button type="text" @click="showFeedbackDialog">提交问题反馈</el-button>，管理员将尽快回复您。</span>
            </template>
          </el-alert>
        </div>
      </div>

      <!-- 常规浏览 -->
      <div v-else>
        <!-- 热门问题 -->
        <div class="section">
          <div class="section-title">
            <i class="el-icon-star-on"></i> 热门问题
            <span class="section-hint">点击问题查看答案</span>
          </div>
          <div class="hot-list">
            <div v-for="item in hotList" :key="item.articleId" class="hot-item" @click="viewArticle(item)">
              <div class="hot-question">{{ item.question }}</div>
              <div class="hot-views"><i class="el-icon-view"></i> {{ item.viewCount }}</div>
            </div>
          </div>
        </div>

        <!-- 问题分类 -->
        <div class="section">
          <div class="section-title">
            <i class="el-icon-folder-opened"></i> 问题分类
          </div>
          <div class="category-list">
            <el-card
              v-for="cat in categoryList"
              :key="cat.categoryId"
              shadow="hover"
              class="category-card"
              @click.native="selectCategory(cat)"
            >
              <div class="category-content">
                <div class="category-icon">
                  <i :class="'el-icon-' + (cat.icon || 'folder')"></i>
                </div>
                <div class="category-info">
                  <div class="category-name">{{ cat.categoryName }}</div>
                  <div class="category-desc">{{ cat.remark }}</div>
                </div>
              </div>
            </el-card>
          </div>
        </div>

        <!-- 分类文章列表 -->
        <div v-if="selectedCategory" class="section">
          <div class="section-title">
            <el-button type="text" icon="el-icon-arrow-left" @click="selectedCategory = null">返回分类</el-button>
            <span class="ml10">{{ selectedCategory.categoryName }}</span>
          </div>
          <div class="article-list">
            <div v-for="item in categoryArticleList" :key="item.articleId" class="article-item" @click="viewArticle(item)">
              <div class="article-question">
                <i class="el-icon-question"></i> {{ item.question }}
              </div>
              <div class="article-answer" v-html="getAnswerSnippet(item.answer)"></div>
              <div class="article-meta">
                <span class="view-count"><i class="el-icon-view"></i> {{ item.viewCount }}次浏览</span>
                <el-tag v-if="item.isHot === '1'" size="mini" type="danger">热门</el-tag>
              </div>
            </div>
            <el-empty v-if="categoryArticleList.length === 0" description="该分类下暂无问题"></el-empty>
          </div>
        </div>
      </div>
    </div>

    <!-- 文章详情弹窗 -->
    <el-dialog
      :title="currentArticle && currentArticle.question"
      :visible.sync="articleVisible"
      width="700px"
      append-to-body
      class="article-dialog"
    >
      <div class="article-detail">
        <div class="article-meta-top">
          <el-tag size="small">{{ currentArticle && currentArticle.categoryName }}</el-tag>
          <span class="view-count"><i class="el-icon-view"></i> {{ currentArticle && currentArticle.viewCount }} 次浏览</span>
        </div>
        <el-divider />
        <div class="article-answer-content" v-html="currentArticle && currentArticle.answer"></div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="articleVisible = false">关闭</el-button>
        <el-button type="primary" @click="showFeedbackDialog">还有疑问？提交反馈</el-button>
      </div>
    </el-dialog>

    <!-- 反馈弹窗 -->
    <el-dialog title="提交问题反馈" :visible.sync="feedbackVisible" width="500px" append-to-body>
      <el-form ref="feedbackForm" :model="feedbackForm" :rules="feedbackRules" label-width="80px">
        <el-form-item label="问题描述" prop="question">
          <el-input
            v-model="feedbackForm.question"
            type="textarea"
            :rows="4"
            placeholder="请详细描述您遇到的问题，我们会尽快回复您"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="您的搜索词">
          <el-input v-model="feedbackForm.searchKeywords" placeholder="请填写刚才搜索的关键词" maxlength="200" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="feedbackVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitFeedback" :loading="feedbackLoading">提交反馈</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getHotArticles, searchArticles, getArticlesByCategory, getArticleDetail, submitFeedback } from "@/api/help/article"

export default {
  name: "BcHelpIndex",
  data() {
    return {
      searchKeyword: '',
      searchResults: [],
      isSearching: false,
      hotList: [],
      categoryList: [],
      selectedCategory: null,
      categoryArticleList: [],
      articleVisible: false,
      currentArticle: null,
      feedbackVisible: false,
      feedbackLoading: false,
      feedbackForm: { question: '', searchKeywords: '' },
      feedbackRules: {
        question: [{ required: true, message: '请输入问题描述', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.loadHotList()
    this.loadCategories()
  },
  methods: {
    async loadHotList() {
      try {
        const res = await getHotArticles()
        this.hotList = res.data || []
      } catch (e) {}
    },
    async loadCategories() {
      try {
        const res = await this.$request({ url: '/help/categories', method: 'get' })
        this.categoryList = res.data || []
      } catch (e) {
        this.categoryList = []
      }
    },
    handleSearch() {
      if (!this.searchKeyword.trim()) {
        this.$modal.msgWarning("请输入搜索关键词")
        return
      }
      this.isSearching = true
      this.selectedCategory = null
      this.loadSearchResults()
    },
    async loadSearchResults() {
      try {
        const res = await searchArticles(this.searchKeyword)
        this.searchResults = res.data || []
      } catch (e) {
        this.searchResults = []
      }
    },
    handleClear() {
      this.isSearching = false
      this.searchResults = []
    },
    async selectCategory(cat) {
      this.selectedCategory = cat
      try {
        const res = await getArticlesByCategory(cat.categoryId)
        this.categoryArticleList = (res.data || []).filter(a => a.categoryId == cat.categoryId)
      } catch (e) {
        this.categoryArticleList = []
      }
    },
    async viewArticle(item) {
      try {
        const res = await getArticleDetail(item.articleId)
        this.currentArticle = res.data
        this.articleVisible = true
        // 刷新热门列表
        this.loadHotList()
      } catch (e) {
        this.$modal.msgError("加载失败")
      }
    },
    getAnswerSnippet(html) {
      if (!html) return ''
      // 去掉HTML标签，取前100个字符
      const text = html.replace(/<[^>]+>/g, '')
      return text.length > 100 ? text.substring(0, 100) + '...' : text
    },
    showFeedbackDialog() {
      this.feedbackForm.searchKeywords = this.searchKeyword
      this.feedbackVisible = true
    },
    handleSubmitFeedback() {
      this.$refs.feedbackForm.validate(async valid => {
        if (!valid) return
        this.feedbackLoading = true
        try {
          await submitFeedback(this.feedbackForm)
          this.$modal.msgSuccess("反馈已提交，我们会尽快回复您！")
          this.feedbackVisible = false
          this.feedbackForm = { question: '', searchKeywords: '' }
        } catch (e) {
          this.$modal.msgError("提交失败，请稍后重试")
        } finally {
          this.feedbackLoading = false
        }
      })
    }
  }
}
</script>

<style scoped>
.help-container { min-height: 100%; background: #f5f7fa; }
.help-header {
  background: linear-gradient(135deg, #409eff, #66b1ff);
  color: #fff;
  padding: 40px 20px;
  text-align: center;
}
.header-title { font-size: 28px; margin: 0 0 10px; font-weight: 600; }
.header-subtitle { font-size: 14px; margin: 0 0 24px; opacity: 0.9; }
.search-box { max-width: 600px; margin: 0 auto; }
.search-box >>> .el-input__inner { border-radius: 20px 0 0 20px; }
.search-box >>> .el-input-group__append { border-radius: 0 20px 20px 0; }

.help-body { max-width: 1000px; margin: 0 auto; padding: 20px; }
.section { margin-bottom: 30px; }
.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 16px;
  display: flex;
  align-items: center;
}
.section-title i { margin-right: 6px; color: #409eff; }
.section-hint { font-size: 13px; color: #909399; font-weight: normal; margin-left: 10px; }
.result-count { font-size: 13px; color: #909399; font-weight: normal; margin-left: auto; }
.highlight { color: #409eff; }

.hot-list { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 12px; }
.hot-item {
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 14px 16px;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.hot-item:hover { box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2); border-color: #409eff; }
.hot-question { font-size: 14px; color: #303133; flex: 1; margin-right: 10px; }
.hot-views { font-size: 12px; color: #909399; white-space: nowrap; }

.category-list { display: grid; grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); gap: 16px; }
.category-card { cursor: pointer; transition: all 0.3s; }
.category-card:hover { transform: translateY(-2px); }
.category-content { display: flex; align-items: center; }
.category-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: linear-gradient(135deg, #ecf5ff, #f0f9eb);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: #409eff;
  margin-right: 12px;
  flex-shrink: 0;
}
.category-name { font-size: 15px; font-weight: 600; color: #303133; }
.category-desc { font-size: 12px; color: #909399; margin-top: 4px; }

.article-list { display: flex; flex-direction: column; gap: 12px; }
.article-item {
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 16px 20px;
  cursor: pointer;
  transition: all 0.3s;
}
.article-item:hover { box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15); border-color: #409eff; }
.article-question { font-size: 15px; color: #303133; font-weight: 500; margin-bottom: 6px; }
.article-question i { color: #409eff; margin-right: 6px; }
.article-answer { font-size: 13px; color: #606266; line-height: 1.6; margin-bottom: 8px; }
.article-meta { display: flex; align-items: center; gap: 10px; }
.view-count { font-size: 12px; color: #909399; }
.view-count i { margin-right: 3px; }

.result-list { display: flex; flex-direction: column; gap: 12px; }
.result-item {
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 16px 20px;
  cursor: pointer;
  transition: all 0.3s;
}
.result-item:hover { box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15); border-color: #409eff; }
.result-question { font-size: 15px; color: #409eff; font-weight: 500; margin-bottom: 6px; }
.result-answer { font-size: 13px; color: #606266; line-height: 1.6; margin-bottom: 8px; }
.result-meta { display: flex; align-items: center; gap: 10px; }

.no-answer-tip { margin-top: 20px; }

.article-dialog >>> .el-dialog__body { padding-top: 10px; }
.article-detail { font-size: 14px; }
.article-meta-top { display: flex; align-items: center; gap: 16px; }
.article-answer-content {
  line-height: 1.9;
  color: #303133;
  white-space: pre-wrap;
}
.article-answer-content >>> p { margin: 8px 0; }

.ml10 { margin-left: 10px; }
</style>
