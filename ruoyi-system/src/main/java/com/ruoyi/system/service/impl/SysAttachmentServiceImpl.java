package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.SysAttachment;
import com.ruoyi.system.mapper.SysAttachmentMapper;
import com.ruoyi.system.service.ISysAttachmentService;

/**
 * 附件表 服务层实现
 *
 * @author ruoyi
 */
@Service
public class SysAttachmentServiceImpl implements ISysAttachmentService
{
    @Autowired
    private SysAttachmentMapper attachmentMapper;

    /**
     * 查询附件信息
     *
     * @param fileId 附件ID
     * @return 附件信息
     */
    @Override
    public SysAttachment selectAttachmentById(Long fileId)
    {
        return attachmentMapper.selectAttachmentById(fileId);
    }

    /**
     * 查询附件列表
     *
     * @param attachment 附件信息
     * @return 附件集合
     */
    @Override
    public List<SysAttachment> selectAttachmentList(SysAttachment attachment)
    {
        return attachmentMapper.selectAttachmentList(attachment);
    }

    /**
     * 新增附件
     *
     * @param attachment 附件信息
     * @return 结果
     */
    @Override
    public int insertAttachment(SysAttachment attachment)
    {
        return attachmentMapper.insertAttachment(attachment);
    }

    /**
     * 修改附件
     *
     * @param attachment 附件信息
     * @return 结果
     */
    @Override
    public int updateAttachment(SysAttachment attachment)
    {
        return attachmentMapper.updateAttachment(attachment);
    }

    /**
     * 删除附件（逻辑删除）
     *
     * @param fileId 附件ID
     * @return 结果
     */
    @Override
    public int deleteAttachmentById(Long fileId)
    {
        return attachmentMapper.deleteAttachmentById(fileId);
    }

    /**
     * 批量删除附件（逻辑删除）
     *
     * @param fileIds 需要删除的附件ID
     * @return 结果
     */
    @Override
    public int deleteAttachmentByIds(Long[] fileIds)
    {
        return attachmentMapper.deleteAttachmentByIds(fileIds);
    }

    /**
     * 根据业务ID和业务类型查询附件列表
     *
     * @param businessType 业务类型
     * @param businessId 业务ID
     * @return 附件列表
     */
    @Override
    public List<SysAttachment> selectAttachmentByBusiness(String businessType, Long businessId)
    {
        return attachmentMapper.selectAttachmentByBusiness(businessType, businessId);
    }
}
