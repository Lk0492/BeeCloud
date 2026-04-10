-- =============================================
-- 高校入学资料在线预审平台 - 咨询帮助模块数据库
-- 表名统一以 bc_help_ 开头
-- 作者：RuoYi dev_luokai
-- 日期：2026-04-10
-- =============================================
use `ry-vue`;

-- ----------------------------
-- 1、帮助问题分类表
-- ----------------------------
drop table if exists bc_help_category;
create table bc_help_category (
  category_id      bigint(20)      not null auto_increment    comment '分类ID',
  category_name    varchar(100)    not null                   comment '分类名称',
  category_code    varchar(50)     not null                   comment '分类编码',
  parent_id        bigint(20)      default 0                  comment '父分类ID（0表示顶级）',
  ancestors        varchar(50)     default ''                  comment '祖级列表',
  sort_num         int(4)          default 0                  comment '显示顺序',
  icon             varchar(100)    default null               comment '分类图标',
  status           char(1)         default '0'                comment '状态（0正常 1停用）',
  create_by        varchar(64)     default ''                 comment '创建者',
  create_time      datetime                                 comment '创建时间',
  update_by        varchar(64)     default ''                 comment '更新者',
  update_time      datetime                                 comment '更新时间',
  remark           varchar(500)    default null               comment '备注',
  primary key (category_id),
  unique key uk_category_code (category_code)
) engine=innodb auto_increment=1 comment='帮助问题分类表';

-- ----------------------------
-- 2、帮助文章/FAQ表（知识库）
-- ----------------------------
drop table if exists bc_help_article;
create table bc_help_article (
  article_id       bigint(20)      not null auto_increment    comment '文章ID',
  category_id      bigint(20)     not null                   comment '所属分类ID',
  question         varchar(500)    not null                   comment '问题标题',
  answer           text                                     comment '答案内容（富文本）',
  keywords         varchar(500)    default null               comment '关键词（逗号分隔，用于模糊匹配）',
  is_hot           char(1)         default '0'                comment '是否热门（0否 1是）',
  is_recommend     char(1)         default '0'                comment '是否推荐（0否 1是）',
  view_count       int(4)          default 0                  comment '浏览次数',
  sort_num         int(4)          default 0                  comment '排序号',
  status           char(1)         default '0'                comment '状态（0正常 1停用）',
  create_by        varchar(64)     default ''                 comment '创建者',
  create_time      datetime                                 comment '创建时间',
  update_by        varchar(64)     default ''                 comment '更新者',
  update_time      datetime                                 comment '更新时间',
  remark           varchar(500)    default null               comment '备注',
  primary key (article_id),
  key idx_category_id (category_id),
  key idx_status (status),
  key idx_is_hot (is_hot),
  key key_idx_keywords (keywords)
) engine=innodb comment='帮助文章/FAQ表';

-- ----------------------------
-- 3、问题反馈表（用户提交未匹配到答案的问题）
-- ----------------------------
drop table if exists bc_help_feedback;
create table bc_help_feedback (
  feedback_id      bigint(20)      not null auto_increment    comment '反馈ID',
  user_id          bigint(20)     default null                comment '提交用户ID',
  user_name        varchar(64)    default null               comment '提交用户姓名',
  question         varchar(500)    not null                   comment '用户提交的问题',
  answer           text             default null               comment '管理员回复的答案',
  reply_by         varchar(64)    default null               comment '回复人',
  reply_time       datetime        default null               comment '回复时间',
  reply_status     char(1)         default '0'                comment '回复状态（0待回复 1已回复 2已关闭）',
  search_keywords  varchar(500)   default null               comment '用户搜索时输入的关键词',
  create_time      datetime                                 comment '提交时间',
  update_by        varchar(64)     default ''                 comment '更新者',
  update_time      datetime                                 comment '更新时间',
  remark           varchar(500)    default null               comment '备注',
  primary key (feedback_id),
  key idx_user_id (user_id),
  key idx_reply_status (reply_status),
  key idx_create_time (create_time)
) engine=innodb comment='问题反馈表';

-- ----------------------------
-- 4、浏览记录表（记录用户查看FAQ的行为，用于统计热门问题）
-- ----------------------------
drop table if exists bc_help_view_log;
create table bc_help_view_log (
  log_id           bigint(20)      not null auto_increment    comment '记录ID',
  article_id       bigint(20)     not null                   comment '文章ID',
  user_id          bigint(20)     default null                comment '浏览用户ID',
  user_name        varchar(64)   default null                comment '浏览用户姓名',
  ip_addr          varchar(128)  default null                comment 'IP地址',
  create_time      datetime                                 comment '浏览时间',
  primary key (log_id),
  key idx_article_id (article_id),
  key idx_user_id (user_id),
  key idx_create_time (create_time)
) engine=innodb comment='FAQ浏览记录表';

-- ----------------------------
-- 初始化-问题分类数据
-- ----------------------------
insert into bc_help_category (category_id, category_name, category_code, parent_id, ancestors, sort_num, icon, status, create_by, create_time, remark) values
(1, '新生入学', 'freshman', 0, '0', 1, 'user', '0', 'admin', sysdate(), '新生入学相关问题');
insert into bc_help_category (category_id, category_name, category_code, parent_id, ancestors, sort_num, icon, status, create_by, create_time, remark) values
(2, '资料提交', 'doc_submit', 0, '0', 2, 'upload', '0', 'admin', sysdate(), '资料提交与上传问题');
insert into bc_help_category (category_id, category_name, category_code, parent_id, ancestors, sort_num, icon, status, create_by, create_time, remark) values
(3, '审核流程', 'review_process', 0, '0', 3, 'checkbox', '0', 'admin', sysdate(), '审核流程与状态问题');
insert into bc_help_category (category_id, category_name, category_code, parent_id, ancestors, sort_num, icon, status, create_by, create_time, remark) values
(4, '账户与密码', 'account', 0, '0', 4, 'lock', '0', 'admin', sysdate(), '账户密码及登录问题');
insert into bc_help_category (category_id, category_name, category_code, parent_id, ancestors, sort_num, icon, status, create_by, create_time, remark) values
(5, '其他问题', 'other', 0, '0', 5, 'question', '0', 'admin', sysdate(), '其他常见问题');

-- ----------------------------
-- 初始化-FAQ文章数据
-- ----------------------------
insert into bc_help_article (category_id, question, answer, keywords, is_hot, is_recommend, view_count, sort_num, status, create_by, create_time) values
(1, '新生如何登录系统？', '新生首次登录系统的账号为您的学号，初始密码为身份证号后6位（若身份证号最后一位为X，请使用大写X）。登录后建议您立即修改密码。', '登录,学号,密码,初始密码,账号', '1', '1', 156, 1, '0', 'admin', sysdate());

insert into bc_help_article (category_id, question, answer, keywords, is_hot, is_recommend, view_count, sort_num, status, create_by, create_time) values
(1, '如何查看自己的入学资料审核状态？', '登录系统后，点击左侧菜单"提交记录"，可以在列表中查看您所有资料的状态。状态包括：未提交、已提交、审核中、已通过、已驳回、补交中、再审核。', '审核状态,查看,提交记录,进度', '1', '1', 203, 2, '0', 'admin', sysdate());

insert into bc_help_article (category_id, question, answer, keywords, is_hot, is_recommend, view_count, sort_num, status, create_by, create_time) values
(2, '需要提交哪些入学资料？', '根据学校要求，您需要提交以下资料：<br>1. 录取通知书（必填）<br>2. 身份证正反面（必填）<br>3. 户口本首页及本人页（必填）<br>4. 免冠证件照片（必填）<br>5. 高考成绩单（必填）<br>6. 体检表（必填）<br>7. 高中学籍表（必填）<br>8. 党团组织关系材料（如有）<br>9. 贫困证明材料（如有）<br><br>请在"资料提审"页面查看详细要求。', '资料,入学,需要,提交,清单', '1', '1', 189, 1, '0', 'admin', sysdate());

insert into bc_help_article (category_id, question, answer, keywords, is_hot, is_recommend, view_count, sort_num, status, create_by, create_time) values
(2, '上传的资料文件有什么格式和大小限制？', '系统对上传文件有以下限制：<br>1. 支持格式：PDF、JPG、JPEG、PNG、DOC、DOCX<br>2. 单文件大小：一般不超过10MB，照片不超过2MB<br>3. 部分资料支持多文件上传（如身份证需上传正反两面）<br>4. 请确保上传的文件清晰可读，避免模糊或遮挡', '上传,文件,格式,大小,限制,pdf,jpg', '1', '0', 145, 2, '0', 'admin', sysdate());

insert into bc_help_article (category_id, question, answer, keywords, is_hot, is_recommend, view_count, sort_num, status, create_by, create_time) values
(3, '审核需要多长时间？', '一般情况下，审核员会在您提交后的1-3个工作日内完成审核。具体时间视资料数量和审核员工作安排而定。如遇节假日或特殊情况，可能会稍有延迟。请您耐心等待，如有紧急情况可通过系统内消息联系审核员。', '审核,时间,多久,审核时间', '1', '1', 178, 1, '0', 'admin', sysdate());

insert into bc_help_article (category_id, question, answer, keywords, is_hot, is_recommend, view_count, sort_num, status, create_by, create_time) values
(3, '资料被驳回后怎么办？', '如果您的资料审核未通过，系统会显示驳回原因。请根据驳回意见进行以下操作：<br>1. 仔细阅读驳回原因<br>2. 重新上传或补充正确的资料<br>3. 再次提交审核<br><br>注意：驳回后请尽快补交，以免影响您的入学办理进度。', '驳回,补交,重新提交,修改,不合格', '1', '0', 167, 2, '0', 'admin', sysdate());

insert into bc_help_article (category_id, question, answer, keywords, is_hot, is_recommend, view_count, sort_num, status, create_by, create_time) values
(4, '忘记登录密码怎么办？', '如果忘记密码，请联系您的辅导员或招生办老师，由管理员为您重置密码。密码重置后，初始密码为身份证号后6位。', '忘记密码,找回密码,重置密码,密码', '1', '1', 234, 1, '0', 'admin', sysdate());

insert into bc_help_article (category_id, question, answer, keywords, is_hot, is_recommend, view_count, sort_num, status, create_by, create_time) values
(4, '如何修改个人登录密码？', '登录系统后，点击右上角个人头像，选择"个人中心"，在安全设置中可以修改登录密码。修改密码需要输入原密码进行验证。', '修改密码,更改密码,密码设置', '0', '0', 89, 2, '0', 'admin', sysdate());

insert into bc_help_article (category_id, question, answer, keywords, is_hot, is_recommend, view_count, sort_num, status, create_by, create_time) values
(5, '登录时提示账号异常怎么办？', '如果登录时提示账号异常，可能是以下原因：<br>1. 账号被禁用 - 请联系管理员<br>2. 连续登录失败导致账号被锁定 - 30分钟后自动解锁<br>3. 账号已过期或被删除<br><br>如有疑问，请联系学校招生办。', '登录异常,账号异常,账号锁定,登录失败', '0', '0', 67, 1, '0', 'admin', sysdate());

insert into bc_help_article (category_id, question, answer, keywords, is_hot, is_recommend, view_count, sort_num, status, create_by, create_time) values
(5, '如何联系审核员或招生办？', '您可以通过以下方式联系相关人员：<br>1. 系统内消息：在"我的消息"中发送站内消息<br>2. 辅导员联系方式可在"组织管理"中查看<br>3. 招生办联系电话：见学校官网公告', '联系,招生办,辅导员,联系方式', '0', '0', 45, 2, '0', 'admin', sysdate());

-- ----------------------------
-- 初始化-菜单数据（咨询帮助模块，menu_id从3200开始）
-- ----------------------------
-- 一级目录：咨询帮助（menu_id=3200）
insert into sys_menu values(3200, '咨询帮助', 0, 8, 'help', null, '', '', 1, 0, 'M', '0', '0', '', 'question', 'admin', sysdate(), '', null, '咨询帮助模块');
-- 二级菜单-学生端
insert into sys_menu values(3201, '咨询帮助', 3200, 1, 'index', 'help/index/index', '', '', 1, 0, 'C', '0', '0', 'help:index:view', 'question', 'admin', sysdate(), '', null, '学生咨询帮助入口');
-- 二级菜单-管理员端
insert into sys_menu values(3210, '知识库管理', 3200, 2, 'article', 'help/article/index', '', '', 1, 0, 'C', '0', '0', 'help:article:list', 'documentation', 'admin', sysdate(), '', null, '管理员知识库管理菜单');
insert into sys_menu values(3211, '问题分类', 3200, 3, 'category', 'help/category/index', '', '', 1, 0, 'C', '0', '0', 'help:category:list', 'tree', 'admin', sysdate(), '', null, '问题分类管理菜单');
insert into sys_menu values(3212, '问题反馈', 3200, 4, 'feedback', 'help/feedback/index', '', '', 1, 0, 'C', '0', '0', 'help:feedback:list', 'message', 'admin', sysdate(), '', null, '问题反馈管理菜单');
-- 三级按钮-知识库管理
insert into sys_menu values(3221, '知识库查询', 3210, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'help:article:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3222, '知识库新增', 3210, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'help:article:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3223, '知识库修改', 3210, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'help:article:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3224, '知识库删除', 3210, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'help:article:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3225, '知识库导出', 3210, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'help:article:export', '#', 'admin', sysdate(), '', null, '');
-- 三级按钮-问题分类
insert into sys_menu values(3231, '分类查询', 3211, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'help:category:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3232, '分类新增', 3211, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'help:category:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3233, '分类修改', 3211, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'help:category:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3234, '分类删除', 3211, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'help:category:remove', '#', 'admin', sysdate(), '', null, '');
-- 三级按钮-问题反馈
insert into sys_menu values(3241, '反馈查询', 3212, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'help:feedback:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3242, '反馈回复', 3212, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'help:feedback:reply', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3243, '反馈删除', 3212, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'help:feedback:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3244, '反馈导出', 3212, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'help:feedback:export', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 初始化-角色菜单关联
-- ----------------------------
-- 超级管理员 -> 所有咨询帮助菜单
insert into sys_role_menu values (1, 3200);
insert into sys_role_menu values (1, 3210);
insert into sys_role_menu values (1, 3211);
insert into sys_role_menu values (1, 3212);
insert into sys_role_menu values (1, 3221);
insert into sys_role_menu values (1, 3222);
insert into sys_role_menu values (1, 3223);
insert into sys_role_menu values (1, 3224);
insert into sys_role_menu values (1, 3225);
insert into sys_role_menu values (1, 3231);
insert into sys_role_menu values (1, 3232);
insert into sys_role_menu values (1, 3233);
insert into sys_role_menu values (1, 3234);
insert into sys_role_menu values (1, 3241);
insert into sys_role_menu values (1, 3242);
insert into sys_role_menu values (1, 3243);
insert into sys_role_menu values (1, 3244);
-- 审核员 -> 知识库管理 + 问题分类 + 问题反馈
insert into sys_role_menu values (3, 3200);
insert into sys_role_menu values (3, 3210);
insert into sys_role_menu values (3, 3211);
insert into sys_role_menu values (3, 3212);
insert into sys_role_menu values (3, 3221);
insert into sys_role_menu values (3, 3222);
insert into sys_role_menu values (3, 3223);
insert into sys_role_menu values (3, 3224);
insert into sys_role_menu values (3, 3225);
insert into sys_role_menu values (3, 3231);
insert into sys_role_menu values (3, 3232);
insert into sys_role_menu values (3, 3233);
insert into sys_role_menu values (3, 3234);
insert into sys_role_menu values (3, 3241);
insert into sys_role_menu values (3, 3242);
insert into sys_role_menu values (3, 3243);
insert into sys_role_menu values (3, 3244);
-- 学生 -> 咨询帮助（只读）
insert into sys_role_menu values (2, 3200);
insert into sys_role_menu values (2, 3201);
