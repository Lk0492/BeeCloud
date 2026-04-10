-- =============================================
-- 高校入学资料在线预审平台 - 资料审核模块数据库
-- 表名统一以 bc_ 开头
-- 作者：RuoYi dev_luokai
-- 日期：2026-04-06
-- =============================================
use `ry-vue`;
-- ----------------------------
-- 1、资料审核申请单主表
-- ----------------------------
drop table if exists bc_review_form;
create table bc_review_form (
  form_id           bigint(20)      not null auto_increment    comment '申请单ID',
  form_no           varchar(32)     not null                   comment '申请单编号',
  student_id        bigint(20)     not null                   comment '学生ID（关联sys_user）',
  student_name      varchar(64)     not null                   comment '学生姓名',
  student_no        varchar(30)     default null               comment '学号',
  id_card           varchar(18)     default null               comment '身份证号（冗余）',
  dept_id           bigint(20)     default null                comment '所属组织ID（关联sys_dept）',
  dept_name         varchar(128)    default null               comment '组织名称（冗余）',
  admission_year    varchar(4)      default null               comment '入学年份',
  form_status       varchar(20)     not null default '0'       comment '申请单状态（0未提交 1已提交 2审核中 3已通过 4已驳回 5补交中 6再审核）',
  submit_time       datetime        default null               comment '提交时间',
  reviewer_id       bigint(20)     default null                comment '当前审核员ID',
  reviewer_name     varchar(64)     default null               comment '当前审核员姓名',
  review_time       datetime        default null               comment '审核时间',
  version_num       int(4)          default 1                  comment '提交版本号（第几次提交）',
  is_current        char(1)         default '1'               comment '是否为当前版本（0否 1是）',
  reject_reason     varchar(1000)   default null               comment '驳回原因（最后一次）',
  total_attachments int(4)          default 0                  comment '附件总数',
  reviewed_attachments int(4)        default 0                  comment '已审核附件数',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                 comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                 comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  del_flag          char(1)         default '0'                comment '删除标志（0存在 2删除）',
  primary key (form_id),
  unique key uk_form_no (form_no),
  key idx_student_id (student_id),
  key idx_form_status (form_status),
  key idx_reviewer_id (reviewer_id),
  key idx_submit_time (submit_time),
  key idx_del_flag (del_flag)
) engine=innodb auto_increment=1 comment = '资料审核申请单主表';

-- ----------------------------
-- 2、资料审核申请单附件表（明细表）
-- ----------------------------
drop table if exists bc_review_attachment;
create table bc_review_attachment (
  detail_id         bigint(20)      not null auto_increment    comment '明细ID',
  form_id           bigint(20)     not null                   comment '申请单ID（关联bc_review_form）',
  file_id           bigint(20)     default null                comment '附件ID（关联sys_attachment）',
  file_category     varchar(50)     not null                   comment '附件资料类型（字典值）',
  file_name         varchar(255)    not null                   comment '文件原始名称',
  file_path         varchar(500)    not null                   comment '文件访问路径（MinIO URL）',
  file_size         bigint(20)     default null                comment '文件大小（字节）',
  file_suffix       varchar(20)     default null               comment '文件后缀',
  attach_status     varchar(20)     not null default '0'       comment '附件审核状态（0未提交 1已提交 2审核中 3已通过 4已驳回）',
  attach_remark     varchar(500)    default null               comment '附件审核备注（驳回原因等）',
  review_time       datetime        default null               comment '附件审核时间',
  reviewer_id       bigint(20)     default null                comment '审核员ID',
  reviewer_name     varchar(64)     default null               comment '审核员姓名',
  version_num       int(4)          default 1                  comment '所属版本号',
  sort_num          int(4)          default 0                  comment '排序号（同类型多个附件时排序）',
  create_by         varchar(64)     default ''                 comment '上传人',
  create_time       datetime                                 comment '上传时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                 comment '更新时间',
  del_flag          char(1)         default '0'                comment '删除标志（0存在 2删除）',
  primary key (detail_id),
  key idx_form_id (form_id),
  key idx_file_category (file_category),
  key idx_attach_status (attach_status),
  key idx_del_flag (del_flag)
) engine=innodb comment = '资料审核申请单附件表';

-- ----------------------------
-- 3、资料审核记录表（审核员操作历史）
-- ----------------------------
drop table if exists bc_review_record;
create table bc_review_record (
  record_id         bigint(20)      not null auto_increment    comment '审核记录ID',
  form_id           bigint(20)     not null                   comment '申请单ID',
  detail_id         bigint(20)     default null                comment '附件明细ID（可为null表示对整单审核）',
  student_id        bigint(20)     not null                   comment '学生ID',
  student_name      varchar(64)     default null               comment '学生姓名（冗余）',
  reviewer_id       bigint(20)     not null                   comment '审核员ID',
  reviewer_name     varchar(64)     not null                   comment '审核员姓名',
  action_type       varchar(20)     not null                   comment '操作类型（submit提交/accept通过/reject驳回/resubmit补交/review认领）',
  action_remark     varchar(500)   default null               comment '操作备注（驳回原因等）',
  form_status_before varchar(20)   default null               comment '操作前申请单状态',
  form_status_after varchar(20)    default null               comment '操作后申请单状态',
  version_num       int(4)         default 1                  comment '版本号',
  create_by         varchar(64)    default ''                 comment '创建者',
  create_time       datetime                               comment '操作时间',
  primary key (record_id),
  key idx_form_id (form_id),
  key idx_student_id (student_id),
  key idx_reviewer_id (reviewer_id),
  key idx_action_type (action_type),
  key idx_create_time (create_time)
) engine=innodb comment = '资料审核记录表';

-- ----------------------------
-- 4、资料类型字典表（资料审核资料类型配置）
-- ----------------------------
drop table if exists bc_doc_type;
create table bc_doc_type (
  type_id           bigint(20)      not null auto_increment    comment '资料类型ID',
  type_code         varchar(50)     not null                   comment '类型编码',
  type_name         varchar(100)    not null                   comment '类型名称',
  type_desc         varchar(500)    default null               comment '材料说明',
  is_required       char(1)         default '1'                comment '是否必填（0否 1是）',
  allow_multiple    char(1)         default '0'                comment '是否允许多文件（0否 1是）',
  max_file_count    int(4)          default 1                  comment '最大文件数量',
  allowed_suffix    varchar(200)    default null               comment '允许的文件后缀（逗号分隔，如：pdf,jpg,png）',
  max_file_size     bigint(20)      default 10485760           comment '单文件最大大小（字节，默认10MB）',
  sort_num          int(4)          default 0                  comment '排序号',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                 comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                 comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (type_id),
  unique key uk_type_code (type_code)
) engine=innodb comment = '资料类型字典表';

-- ----------------------------
-- 5、系统通知表（站内消息）
-- ----------------------------
drop table if exists bc_notice;
create table bc_notice (
  notice_id         bigint(20)      not null auto_increment    comment '通知ID',
  notice_type       varchar(20)     not null                   comment '通知类型（review_pass审核通过/review_reject审核驳回/supply补交通知/system系统通知）',
  title             varchar(200)    not null                   comment '通知标题',
  content           varchar(2000)   default null               comment '通知内容',
  form_id           bigint(20)      default null                comment '关联申请单ID',
  receive_user_id   bigint(20)      default null                comment '接收人用户ID',
  receive_user_name varchar(64)     default null               comment '接收人姓名',
  is_read           char(1)         default '0'                comment '是否已读（0未读 1已读）',
  read_time         datetime        default null               comment '阅读时间',
  create_by         varchar(64)    default ''                 comment '创建者',
  create_time       datetime                               comment '创建时间',
  primary key (notice_id),
  key idx_receive_user_id (receive_user_id),
  key idx_form_id (form_id),
  key idx_notice_type (notice_type),
  key idx_is_read (is_read)
) engine=innodb comment = '系统通知表';

-- ----------------------------
-- 6、审核分配记录表（审核员分配历史）
-- ----------------------------
drop table if exists bc_review_assign;
create table bc_review_assign (
  assign_id         bigint(20)      not null auto_increment    comment '分配ID',
  form_id           bigint(20)     not null                   comment '申请单ID',
  assigner_id       bigint(20)     default null                comment '分配人ID（系统分配为null）',
  assigner_name     varchar(64)    default null                comment '分配人姓名',
  reviewer_id       bigint(20)     not null                   comment '被分配的审核员ID',
  reviewer_name     varchar(64)     not null                   comment '被分配的审核员姓名',
  assign_type       varchar(20)     default 'auto'             comment '分配方式（auto自动/random随机/spec指定）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                 comment '分配时间',
  primary key (assign_id),
  key idx_form_id (form_id),
  key idx_reviewer_id (reviewer_id)
) engine=innodb comment = '审核分配记录表';

-- ----------------------------
-- 初始化-资料类型字典表数据
-- ----------------------------
insert into bc_doc_type (type_code, type_name, type_desc, is_required, allow_multiple, max_file_count, allowed_suffix, max_file_size, sort_num, status, create_by, create_time) values
('admission_notice', '录取通知书', '录取通知书电子版', '1', '0', 1, 'pdf,jpg,jpeg,png', 10485760, 1, '0', 'admin', sysdate()),
('id_card', '身份证', '身份证正反面', '1', '1', 2, 'jpg,jpeg,png', 5242880, 2, '0', 'admin', sysdate()),
('household', '户口本', '户口本首页及本人页', '1', '1', 2, 'jpg,jpeg,png,pdf', 5242880, 3, '0', 'admin', sysdate()),
('photo', '照片', '免冠证件照片', '1', '0', 1, 'jpg,jpeg,png', 2097152, 4, '0', 'admin', sysdate()),
('ganscore', '高考成绩单', '高考成绩单', '1', '0', 1, 'pdf,jpg,jpeg,png', 10485760, 5, '0', 'admin', sysdate()),
('health_cert', '体检表', '入学体检表', '1', '0', 1, 'pdf,jpg,jpeg,png', 10485760, 6, '0', 'admin', sysdate()),
('enroll_record', '学籍表', '高中学籍表', '1', '0', 1, 'pdf,jpg,jpeg,png', 10485760, 7, '0', 'admin', sysdate()),
('party_relation', '党团关系', '党团组织关系转移材料', '0', '1', 3, 'pdf,jpg,jpeg,png', 10485760, 8, '0', 'admin', sysdate()),
('poor_proof', '贫困证明', '贫困证明材料（如有）', '0', '1', 2, 'pdf,jpg,jpeg,png', 10485760, 9, '0', 'admin', sysdate()),
('other', '其他材料', '其他补充材料', '0', '1', 5, 'pdf,doc,docx,jpg,jpeg,png', 10485760, 10, '0', 'admin', sysdate());

-- ----------------------------
-- 初始化-字典类型和字典数据（附件资料类型）
-- ----------------------------
drop table if exists sys_dict_type_bc_category;
insert into sys_dict_type values(200, '资料审核-附件类型', 'bc_doc_category', '0', 'admin', sysdate(), '', null, '入学资料审核附件类型字典');

drop table if exists sys_dict_data_bc_category;
insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) values
(1, '录取通知书', 'admission_notice', 'bc_doc_category', '', 'primary', 'Y', '0', 'admin', sysdate(), '录取通知书电子版'),
(2, '身份证', 'id_card', 'bc_doc_category', '', 'success', 'N', '0', 'admin', sysdate(), '身份证正反面'),
(3, '户口本', 'household', 'bc_doc_category', '', 'warning', 'N', '0', 'admin', sysdate(), '户口本首页及本人页'),
(4, '照片', 'photo', 'bc_doc_category', '', 'info', 'N', '0', 'admin', sysdate(), '免冠证件照片'),
(5, '高考成绩单', 'ganscore', 'bc_doc_category', '', 'primary', 'N', '0', 'admin', sysdate(), '高考成绩单'),
(6, '体检表', 'health_cert', 'bc_doc_category', '', 'warning', 'N', '0', 'admin', sysdate(), '入学体检表'),
(7, '学籍表', 'enroll_record', 'bc_doc_category', '', 'success', 'N', '0', 'admin', sysdate(), '高中学籍表'),
(8, '党团关系', 'party_relation', 'bc_doc_category', '', 'danger', 'N', '0', 'admin', sysdate(), '党团组织关系转移材料'),
(9, '贫困证明', 'poor_proof', 'bc_doc_category', '', 'warning', 'N', '0', 'admin', sysdate(), '贫困证明材料'),
(10, '其他材料', 'other', 'bc_doc_category', '', 'info', 'N', '0', 'admin', sysdate(), '其他补充材料');

-- ----------------------------
-- 初始化-字典类型和字典数据（申请单状态）
-- ----------------------------
drop table if exists sys_dict_type_bc_form_status;
insert into sys_dict_type values(201, '资料审核-申请单状态', 'bc_form_status', '0', 'admin', sysdate(), '', null, '入学资料审核申请单状态字典');

drop table if exists sys_dict_data_bc_form_status;
insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) values
(0, '未提交', '0', 'bc_form_status', '', 'info', 'Y', '0', 'admin', sysdate(), '草稿状态'),
(1, '已提交', '1', 'bc_form_status', '', 'primary', 'N', '0', 'admin', sysdate(), '等待分配'),
(2, '审核中', '2', 'bc_form_status', '', 'warning', 'N', '0', 'admin', sysdate(), '正在审核'),
(3, '已通过', '3', 'bc_form_status', '', 'success', 'N', '0', 'admin', sysdate(), '审核通过'),
(4, '已驳回', '4', 'bc_form_status', '', 'danger', 'N', '0', 'admin', sysdate(), '审核驳回'),
(5, '补交中', '5', 'bc_form_status', '', 'warning', 'N', '0', 'admin', sysdate(), '需要补交资料'),
(6, '再审核', '6', 'bc_form_status', '', 'warning', 'N', '0', 'admin', sysdate(), '补交后再次审核');

-- ----------------------------
-- 初始化-字典类型和字典数据（附件审核状态）
-- ----------------------------
drop table if exists sys_dict_type_bc_attach_status;
insert into sys_dict_type values(202, '资料审核-附件状态', 'bc_attach_status', '0', 'admin', sysdate(), '', null, '入学资料审核附件状态字典');

drop table if exists sys_dict_data_bc_attach_status;
insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) values
(0, '未提交', '0', 'bc_attach_status', '', 'info', 'Y', '0', 'admin', sysdate(), '未上传'),
(1, '已提交', '1', 'bc_attach_status', '', 'primary', 'N', '0', 'admin', sysdate(), '已上传待审核'),
(2, '审核中', '2', 'bc_attach_status', '', 'warning', 'N', '0', 'admin', sysdate(), '审核中'),
(3, '已通过', '3', 'bc_attach_status', '', 'success', 'N', '0', 'admin', sysdate(), '审核通过'),
(4, '已驳回', '4', 'bc_attach_status', '', 'danger', 'N', '0', 'admin', sysdate(), '审核驳回');

-- ----------------------------
-- 初始化-字典类型和字典数据（通知类型）
-- ----------------------------
drop table if exists sys_dict_type_bc_notice_type;
insert into sys_dict_type values(203, '资料审核-通知类型', 'bc_notice_type', '0', 'admin', sysdate(), '', null, '入学资料审核通知类型字典');

drop table if exists sys_dict_data_bc_notice_type;
insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) values
(1, '审核通过', 'review_pass', 'bc_notice_type', '', 'success', 'N', '0', 'admin', sysdate(), '资料审核通过通知'),
(2, '审核驳回', 'review_reject', 'bc_notice_type', '', 'danger', 'N', '0', 'admin', sysdate(), '资料审核驳回通知'),
(3, '补交通知', 'supply', 'bc_notice_type', '', 'warning', 'N', '0', 'admin', sysdate(), '需要补交资料通知'),
(4, '系统通知', 'system', 'bc_notice_type', '', 'primary', 'N', '0', 'admin', sysdate(), '系统消息通知');

-- ----------------------------
-- ALTER sys_attachment 添加 file_category 字段（已有表则ALTER，未有则跳过）
-- 判断 sys_attachment 是否已有 file_category 字段
-- ----------------------------
-- alter table sys_attachment add column file_category varchar(50) default null comment '附件资料类型（审核模块用）' after business_id;

-- ----------------------------
-- 初始化-菜单数据（资料审核模块）
-- ----------------------------
-- 注意：这些菜单数据需要插入到 sys_menu 表
-- 一级目录：资料审核（menu_id从3000开始）
insert into sys_menu values(3000, '资料审核', 0, 5, 'review', null, '', '', 1, 0, 'M', '0', '0', '', 'documentation', 'admin', sysdate(), '', null, '高校入学资料审核模块');
-- 二级菜单
insert into sys_menu values(3001, '资料提审', 3000, 1, 'submit', 'review/submit/index', '', '', 1, 0, 'C', '0', '0', 'review:submit:list', 'upload', 'admin', sysdate(), '', null, '学生资料提审菜单');
insert into sys_menu values(3002, '提交记录', 3000, 2, 'record', 'review/record/index', '', '', 1, 0, 'C', '0', '0', 'review:record:list', 'list', 'admin', sysdate(), '', null, '学生提交记录菜单');
insert into sys_menu values(3003, '资料审核', 3000, 3, 'audit', 'review/audit/index', '', '', 1, 0, 'C', '0', '0', 'review:audit:list', 'checkbox', 'admin', sysdate(), '', null, '审核员资料审核菜单');
insert into sys_menu values(3004, '审核记录', 3000, 4, 'auditRecord', 'review/auditRecord/index', '', '', 1, 0, 'C', '0', '0', 'review:auditRecord:list', 'tree', 'admin', sysdate(), '', null, '审核记录菜单');
insert into sys_menu values(3005, '资料类型', 3000, 5, 'docType', 'review/docType/index', '', '', 1, 0, 'C', '0', '0', 'review:docType:list', 'component', 'admin', sysdate(), '', null, '资料类型配置菜单');
insert into sys_menu values(3006, '数据统计', 3000, 6, 'statistics', 'review/statistics/index', '', '', 1, 0, 'C', '0', '0', 'review:statistics:list', 'chart', 'admin', sysdate(), '', null, '审核数据统计菜单');
insert into sys_menu values(3007, '我的消息', 3000, 7, 'notice', 'review/notice/index', '', '', 1, 0, 'C', '0', '0', 'review:notice:list', 'message', 'admin', sysdate(), '', null, '学生/审核员消息通知');
-- 三级按钮-资料提审
insert into sys_menu values(3101, '资料提审查询', 3001, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'review:submit:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3102, '资料提审新增', 3001, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'review:submit:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3103, '资料提审修改', 3001, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'review:submit:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3104, '资料提审删除', 3001, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'review:submit:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3105, '资料提审提交', 3001, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'review:submit:submit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3106, '资料提审导出', 3001, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'review:submit:export', '#', 'admin', sysdate(), '', null, '');
-- 三级按钮-提交记录
insert into sys_menu values(3107, '提交记录查询', 3002, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'review:record:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3108, '提交记录查看', 3002, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'review:record:view', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3109, '提交记录导出', 3002, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'review:record:export', '#', 'admin', sysdate(), '', null, '');
-- 三级按钮-资料审核（审核员）
insert into sys_menu values(3111, '资料审核查询', 3003, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'review:audit:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3112, '资料审核通过', 3003, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'review:audit:accept', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3113, '资料审核驳回', 3003, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'review:audit:reject', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3114, '资料审核查看', 3003, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'review:audit:view', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3115, '资料审核导出', 3003, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'review:audit:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3116, '批量审核通过', 3003, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'review:audit:batchAccept', '#', 'admin', sysdate(), '', null, '');
-- 三级按钮-审核记录
insert into sys_menu values(3121, '审核记录查询', 3004, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'review:auditRecord:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3122, '审核记录查看', 3004, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'review:auditRecord:view', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3123, '审核记录导出', 3004, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'review:auditRecord:export', '#', 'admin', sysdate(), '', null, '');
-- 三级按钮-资料类型
insert into sys_menu values(3131, '资料类型查询', 3005, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'review:docType:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3132, '资料类型新增', 3005, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'review:docType:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3133, '资料类型修改', 3005, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'review:docType:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3134, '资料类型删除', 3005, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'review:docType:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3135, '资料类型导出', 3005, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'review:docType:export', '#', 'admin', sysdate(), '', null, '');
-- 三级按钮-数据统计
insert into sys_menu values(3141, '数据统计查看', 3006, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'review:statistics:query', '#', 'admin', sysdate(), '', null, '');
-- 三级按钮-我的消息
insert into sys_menu values(3151, '消息查询', 3007, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'review:notice:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values(3152, '消息标记已读', 3007, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'review:notice:read', '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- 初始化-角色菜单关联（给审核员角色和学生角色分配菜单）
-- ----------------------------
-- 审核员角色 -> 资料审核菜单
insert into sys_role_menu values (3, 3000);
insert into sys_role_menu values (3, 3003);
insert into sys_role_menu values (3, 3004);
insert into sys_role_menu values (3, 3005);
insert into sys_role_menu values (3, 3006);
insert into sys_role_menu values (3, 3007);
insert into sys_role_menu values (3, 3111);
insert into sys_role_menu values (3, 3112);
insert into sys_role_menu values (3, 3113);
insert into sys_role_menu values (3, 3114);
insert into sys_role_menu values (3, 3115);
insert into sys_role_menu values (3, 3116);
insert into sys_role_menu values (3, 3121);
insert into sys_role_menu values (3, 3122);
insert into sys_role_menu values (3, 3123);
insert into sys_role_menu values (3, 3131);
insert into sys_role_menu values (3, 3132);
insert into sys_role_menu values (3, 3133);
insert into sys_role_menu values (3, 3134);
insert into sys_role_menu values (3, 3135);
insert into sys_role_menu values (3, 3141);
insert into sys_role_menu values (3, 3151);
insert into sys_role_menu values (3, 3152);
-- 学生角色 -> 资料提审 + 提交记录 + 我的消息
insert into sys_role_menu values (2, 3000);
insert into sys_role_menu values (2, 3001);
insert into sys_role_menu values (2, 3002);
insert into sys_role_menu values (2, 3007);
insert into sys_role_menu values (2, 3101);
insert into sys_role_menu values (2, 3102);
insert into sys_role_menu values (2, 3103);
insert into sys_role_menu values (2, 3104);
insert into sys_role_menu values (2, 3105);
insert into sys_role_menu values (2, 3106);
insert into sys_role_menu values (2, 3107);
insert into sys_role_menu values (2, 3108);
insert into sys_role_menu values (2, 3109);
insert into sys_role_menu values (2, 3151);
insert into sys_role_menu values (2, 3152);

-- ----------------------------
-- 补充-将所有学生用户分配到学生角色 (role_id=2)
-- 说明：将 user_type='01' 的用户自动分配到学生角色（注意：sys_user.user_type 的值是 '01'，不是 'student'）
-- 说明2：bc_review.sql 运行在全新库时会插入 student 用户并分配角色，
--        运行在已有库时会自动跳过已有角色的用户，不会重复插入
-- ----------------------------
INSERT INTO sys_user_role (user_id, role_id)
SELECT u.user_id, 2
FROM sys_user u
WHERE u.user_type = '01'
  AND u.del_flag = '0'
  AND NOT EXISTS (
      SELECT 1 FROM sys_user_role ur WHERE ur.user_id = u.user_id AND ur.role_id = 2
  )
ON DUPLICATE KEY UPDATE role_id = role_id;

-- ----------------------------
-- 补充-将所有审核员用户分配到审核员角色 (role_id=3)
-- 说明：将 user_type='02' 的用户自动分配到审核员角色
-- 注意：sys_user.user_type 的值是 '02'，不是 'reviewer'
-- ----------------------------
INSERT INTO sys_user_role (user_id, role_id)
SELECT u.user_id, 3
FROM sys_user u
WHERE u.user_type = '02'
  AND u.del_flag = '0'
  AND NOT EXISTS (
      SELECT 1 FROM sys_user_role ur WHERE ur.user_id = u.user_id AND ur.role_id = 3
  )
ON DUPLICATE KEY UPDATE role_id = role_id;

-- ----------------------------
-- ALTER sys_attachment 添加 file_category 字段
-- 如果 sys_attachment 表已存在，执行以下 ALTER 语句添加字段
-- ----------------------------
-- ALTER TABLE sys_attachment ADD COLUMN file_category varchar(50) DEFAULT NULL COMMENT '附件资料类型（审核模块用）' AFTER business_id;
-- ALTER TABLE sys_attachment ADD INDEX idx_attachment_file_category (file_category);
