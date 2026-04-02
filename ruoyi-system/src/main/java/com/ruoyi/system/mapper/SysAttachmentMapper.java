package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysAttachment;

/**
 * 附件表 数据层
 *
 * @author ruoyi
 */
public interface SysAttachmentMapper
{
    /**
     * 查询附件信息
     *
     * @param fileId 附件ID
     * @return 附件信息
     */
    public SysAttachment selectAttachmentById(Long fileId);

    /**
     * 查询附件列表
     *
     * @param attachment 附件信息
     * @return 附件集合
     */
    public List<SysAttachment> selectAttachmentList(SysAttachment attachment);

    /**
     * 新增附件
     *
     * @param attachment 附件信息
     * @return 结果
     */
    public int insertAttachment(SysAttachment attachment);

    /**
     * 修改附件
     *
     * @param attachment 附件信息
     * @return 结果
     */
    public int updateAttachment(SysAttachment attachment);

    /**
     * 删除附件（逻辑删除）
     *
     * @param fileId 附件ID
     * @return 结果
     */
    public int deleteAttachmentById(Long fileId);

    /**
     * 批量删除附件（逻辑删除）
     *
     * @param fileIds 需要删除的附件ID
     * @return 结果
     */
    public int deleteAttachmentByIds(Long[] fileIds);

    /**
     * 根据业务ID和业务类型查询附件列表
     *
     * @param businessType 业务类型
     * @param businessId 业务ID
     * @return 附件列表
     */
    public List<SysAttachment> selectAttachmentByBusiness(String businessType, Long businessId);
}
