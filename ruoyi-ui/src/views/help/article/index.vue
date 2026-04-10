<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="问题标题" prop="question">
        <el-input v-model="queryParams.question" placeholder="请输入问题标题" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="关键词" prop="keywords">
        <el-input v-model="queryParams.keywords" placeholder="请输入关键词" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="所属分类" prop="categoryId">
        <el-select v-model="queryParams.categoryId" placeholder="请选择分类" clearable>
          <el-option v-for="cat in categoryOptions" :key="cat.categoryId" :label="cat.categoryName" :value="cat.categoryId" />
        </el-select>
      </el-form-item>
      <el-form-item label="是否热门" prop="isHot">
        <el-select v-model="queryParams.isHot" placeholder="是否热门" clearable>
          <el-option label="是" value="1" />
          <el-option label="否" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="状态" clearable>
          <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['help:article:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['help:article:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['help:article:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['help:article:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="articleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="问题" align="left" prop="question" min-width="200" :show-overflow-tooltip="true" />
      <el-table-column label="所属分类" align="center" prop="categoryName" width="120" />
      <el-table-column label="关键词" align="left" prop="keywords" :show-overflow-tooltip="true" />
      <el-table-column label="是否热门" align="center" prop="isHot" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isHot === '1' ? 'danger' : 'info'">{{ scope.row.isHot === '1' ? '热门' : '否' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="是否推荐" align="center" prop="isRecommend" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isRecommend === '1' ? 'success' : 'info'">{{ scope.row.isRecommend === '1' ? '推荐' : '否' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="浏览次数" align="center" prop="viewCount" width="100" />
      <el-table-column label="排序" align="center" prop="sortNum" width="80" />
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['help:article:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" style="color:#f56c6c" @click="handleDelete(scope.row)" v-hasPermi="['help:article:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="所属分类" prop="categoryId">
              <el-select v-model="form.categoryId" placeholder="请选择分类" style="width:100%">
                <el-option v-for="cat in categoryOptions" :key="cat.categoryId" :label="cat.categoryName" :value="cat.categoryId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="问题标题" prop="question">
              <el-input v-model="form.question" placeholder="请输入问题标题" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="关键词" prop="keywords">
          <el-input v-model="form.keywords" placeholder="请输入关键词，多个用逗号分隔，用于模糊匹配搜索" />
        </el-form-item>
        <el-form-item label="答案内容" prop="answer">
          <el-input v-model="form.answer" type="textarea" :rows="8" placeholder="请输入答案内容，支持富文本" />
        </el-form-item>
        <el-row>
          <el-col :span="8">
            <el-form-item label="是否热门">
              <el-radio-group v-model="form.isHot">
                <el-radio label="1">热门</el-radio>
                <el-radio label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否推荐">
              <el-radio-group v-model="form.isRecommend">
                <el-radio label="1">推荐</el-radio>
                <el-radio label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="排序号">
              <el-input-number v-model="form.sortNum" :min="0" controls-position="right" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog title="查看详情" :visible.sync="viewOpen" width="700px" append-to-body>
      <div v-if="viewData">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="问题">{{ viewData.question }}</el-descriptions-item>
          <el-descriptions-item label="所属分类">{{ viewData.categoryName }}</el-descriptions-item>
          <el-descriptions-item label="关键词">{{ viewData.keywords }}</el-descriptions-item>
          <el-descriptions-item label="浏览次数">{{ viewData.viewCount }}</el-descriptions-item>
          <el-descriptions-item label="是否热门">
            <el-tag :type="viewData.isHot === '1' ? 'danger' : 'info'">{{ viewData.isHot === '1' ? '热门' : '否' }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <dict-tag :options="dict.type.sys_normal_disable" :value="viewData.status"/>
          </el-descriptions-item>
        </el-descriptions>
        <el-divider content-position="left">答案内容</el-divider>
        <div class="answer-content" v-html="viewData.answer"></div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listArticle, getArticle, addArticle, updateArticle, delArticle } from "@/api/help/article"

export default {
  name: "BcHelpArticle",
  dicts: ['sys_normal_disable'],
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      articleList: [],
      categoryOptions: [],
      title: "",
      open: false,
      viewOpen: false,
      viewData: null,
      queryParams: { pageNum: 1, pageSize: 10, question: '', keywords: '', categoryId: null, isHot: '', status: '' },
      form: {},
      rules: {
        categoryId: [{ required: true, message: "所属分类不能为空", trigger: "change" }],
        question: [{ required: true, message: "问题标题不能为空", trigger: "blur" }]
      }
    }
  },
  created() {
    this.getList()
    this.loadCategories()
  },
  methods: {
    async getList() {
      this.loading = true
      try {
        const res = await listArticle(this.queryParams)
        this.articleList = res.rows
        this.total = res.total
      } catch (e) { this.$modal.msgError("加载失败") }
      finally { this.loading = false }
    },
    async loadCategories() {
      try {
        const res = await this.$request({ url: '/help/categories', method: 'get' })
        this.categoryOptions = res.data || []
      } catch (e) { this.categoryOptions = [] }
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery() },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.articleId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加知识库"
    },
    handleUpdate(row) {
      const articleId = row.articleId || this.ids[0]
      getArticle(articleId).then(res => {
        this.form = res.data
        this.open = true
        this.title = "修改知识库"
      })
    },
    handleView(row) {
      getArticle(row.articleId).then(res => {
        this.viewData = res.data
        this.viewOpen = true
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.articleId) {
            updateArticle(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addArticle(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    cancel() { this.open = false; this.reset() },
    reset() {
      this.form = { isHot: '0', isRecommend: '0', sortNum: 0, status: '0' }
      this.resetForm("form")
    },
    handleDelete(row) {
      const articleIds = row.articleId || this.ids
      this.$modal.confirm('是否确认删除选中的数据项？').then(() => delArticle(articleIds)).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('/help/article/export', { ...this.queryParams }, `知识库_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.mb8 { margin-bottom: 16px; }
.answer-content {
  line-height: 1.9;
  color: #303133;
  white-space: pre-wrap;
  background: #f5f7fa;
  padding: 16px;
  border-radius: 4px;
}
.answer-content >>> p { margin: 8px 0; }
</style>
