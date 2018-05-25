package com.wedo.OMS.service;

import com.wedo.OMS.entity.Resource;
import com.wedo.OMS.entity.Task;
import com.wedo.OMS.entity.TaskResource;
import com.wedo.OMS.exception.ResourceNotFoundException;
import com.wedo.OMS.exception.TaskNotFoundException;

import java.util.List;

public interface ResourceService {
    /**
     * 获取任务资源
     *
     * @param taskId
     * @return
     */
    List<Resource> listResourcesByTaskId(long taskId) throws TaskNotFoundException;

    /**
     * 发包方查看所有资源
     *
     * @return
     */
    List<Resource> listAllResources();

    /**
     * 发包方新建资源并分配
     *
     * @param resource
     * @param tasks    资源分配任务
     * @return
     */
    Resource addResource(Resource resource, List<Task> tasks);

    /**
     * 发包方修改资源，并重新分配资源
     * @param resourceId
     * @param tasks      资源分配任务
     */
    //void updateResource(Long resourceId, Resource resource, List<Task> tasks);

    /**
     * 删除资源
     *
     * @param resourceId
     */
    void deleteResourceById(long resourceId);

    /**
     * 获取资源
     *
     * @param resourceId
     * @return
     */
    Resource getResourceByResourceId(long resourceId) throws ResourceNotFoundException;

    /**
     * 根据资源名称搜索资源
     *
     * @param resourcename
     * @return
     */
    List<Resource> getResourcesByResourceName(String resourcename);

    /**
     * 发包方上传资源
     *
     * @param taskId
     * @param resource
     */
    Resource uploadResource(long taskId, Resource resource) throws TaskNotFoundException;

    /**
     * 接包方下载资源
     *
     * @param resourceId
     */
    String downloadResource(long resourceId);

    /**
     * 分配资源
     *
     * @param resourceId
     * @param taskId
     * @return
     */
    TaskResource addTaskResource(long resourceId, long taskId) throws ResourceNotFoundException, TaskNotFoundException;
}
