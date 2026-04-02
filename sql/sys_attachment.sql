-- ===============================================================
-- 附件管理表 sys_attachment DDL
-- 适用于 RuoYi-Vue 3.9.2 + MySQL 8.0
-- 执行方式：在 MySQL 控制台或 Navicat/DBeaver 中执行以下 SQL
-- ===============================================================

-- ----------------------------
-- 1. 创建附件表
-- ----------------------------
DROP TABLE IF EXISTS `sys_attachment`;
CREATE TABLE `sys_attachment` (
  `file_id`            bigint        NOT NULL                AUTO_INCREMENT  COMMENT '附件ID',
  `original_filename`  varchar(255)  DEFAULT NULL          COMMENT '原始文件名（上传时的名称）',
  `filename`          varchar(255)  DEFAULT NULL          COMMENT '服务器端文件名',
  `file_suffix`        varchar(32)   DEFAULT NULL          COMMENT '文件后缀',
  `file_size`          bigint        DEFAULT NULL          COMMENT '文件大小（字节）',
  `file_type`          varchar(32)   DEFAULT NULL          COMMENT '文件类型（image/video/audio/document/archive/other）',
  `content_type`       varchar(128)  DEFAULT NULL          COMMENT 'MIME类型',
  `access_url`         varchar(512)  DEFAULT NULL          COMMENT '访问地址（完整URL）',
  `storage_type`       varchar(16)   DEFAULT NULL          COMMENT '存储方式（minio/local）',
  `bucket_name`        varchar(128)  DEFAULT NULL          COMMENT '存储桶名称（MinIO）',
  `object_name`        varchar(512)  DEFAULT NULL          COMMENT '对象名称（MinIO object key）',
  `uuid`               varchar(64)   DEFAULT NULL          COMMENT '文件唯一标识（UUID）',
  `business_type`      varchar(64)   DEFAULT NULL          COMMENT '业务类型（如：avatar/notice/article）',
  `business_id`        bigint        DEFAULT NULL          COMMENT '关联业务ID',
  `dept_id`            bigint        DEFAULT NULL          COMMENT '上传者部门ID',
  `dept_name`          varchar(64)   DEFAULT NULL          COMMENT '上传者部门名称',
  `uploader_username`  varchar(64)   DEFAULT NULL          COMMENT '上传者用户名',
  `download_count`     bigint        DEFAULT 0             COMMENT '下载次数',
  `status`             char(1)       DEFAULT '0'           COMMENT '文件状态（0=正常 1=禁用）',
  `del_flag`           char(1)       DEFAULT '0'           COMMENT '删除标志（0=存在 2=删除）',
  `create_by`          varchar(64)   DEFAULT NULL          COMMENT '创建者',
  `create_time`        datetime      DEFAULT NULL          COMMENT '创建时间',
  `update_by`          varchar(64)   DEFAULT NULL          COMMENT '更新者',
  `update_time`        datetime      DEFAULT NULL          COMMENT '更新时间',
  `remark`             varchar(512)  DEFAULT NULL          COMMENT '备注',
  PRIMARY KEY (`file_id`),
  KEY `idx_attachment_uuid`           (`uuid`),
  KEY `idx_attachment_business`       (`business_type`, `business_id`),
  KEY `idx_attachment_uploader`       (`uploader_username`),
  KEY `idx_attachment_create_time`    (`create_time`),
  KEY `idx_attachment_del_flag`       (`del_flag`),
  KEY `idx_attachment_file_type`      (`file_type`),
  KEY `idx_attachment_storage_type`    (`storage_type`),
  KEY `idx_attachment_dept_id`        (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='附件管理表';

-- ----------------------------
-- 2. 初始数据（文件类型字典，配合前端下拉筛选使用）
-- ----------------------------
-- 注意：若系统已有字典模块，请在"字典类型"中新增 `sys_file_type` 类型，
-- 并在"字典数据"中添加以下数据：
--
--  INSERT INTO `sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
--  (1, '图片',     'image',    'sys_file_type', '', 'primary',   'Y', '0', 'admin', sysdate(), ''),
--  (2, '视频',     'video',    'sys_file_type', '', 'danger',    'N', '0', 'admin', sysdate(), ''),
--  (3, '音频',     'audio',    'sys_file_type', '', 'warning',   'N', '0', 'admin', sysdate(), ''),
--  (4, '文档',     'document', 'sys_file_type', '', 'success',   'N', '0', 'admin', sysdate(), ''),
--  (5, '压缩包',   'archive',  'sys_file_type', '', 'info',      'N', '0', 'admin', sysdate(), ''),
--  (6, '其他',     'other',    'sys_file_type', '', 'default',   'N', '0', 'admin', sysdate(), '');
--
-- 存储方式字典（dict_type: sys_storage_type）：
--  INSERT INTO `sys_dict_data` (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
--  (1, 'MinIO对象存储', 'minio', 'sys_storage_type', '', 'primary',   'Y', '0', 'admin', sysdate(), ''),
--  (2, '本地磁盘',     'local', 'sys_storage_type', '', 'default',   'N', '0', 'admin', sysdate(), '');
