package com.wedo.OMS.controller;

import com.wedo.OMS.entity.Code;
import com.wedo.OMS.entity.Record;
import com.wedo.OMS.entity.Task;
import com.wedo.OMS.entity.UserTask;
import com.wedo.OMS.enums.UserTaskRole;
import com.wedo.OMS.enums.VerifyStatus;
import com.wedo.OMS.exception.*;
import com.wedo.OMS.service.ProjectService;
import com.wedo.OMS.service.TaskService;
import com.wedo.OMS.vo.EnumChoice;
import com.wedo.OMS.vo.NewTask;
import com.wedo.OMS.vo.OneLong;
import com.wedo.OMS.vo.VueTask;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//TODO fill in javadoc or remove it.
@RestController
public class TaskController {
    private final TaskService taskService;
    private final ProjectService projectService;

    public TaskController(TaskService taskService, ProjectService projectService) {
        this.taskService = taskService;
        this.projectService = projectService;
    }

    /**
     * 用户获取我负责的任务/我参与的任务
     *
     * @param utr
     * @param userId
     * @return
     */
    @PostMapping(value = "/users/{userId}/tasks")
    public List<VueTask> getTask(@PathVariable("userId") long userId, @RequestBody EnumChoice utr) throws UserNotFoundException {
        List<Task> tasks = new ArrayList<>();
        List<VueTask> vueTasks = new ArrayList<>();
        VueTask vueTask = new VueTask();
        long percentage;
        switch (utr.getChoice()){
            case "LEADER":
                tasks = taskService.findTasks(userId, UserTaskRole.LEADER);
                break;
            case "FOLLOWER":
                tasks = taskService.findTasks(userId, UserTaskRole.FOLLOWER);
                break;
        }
        for(Task task:tasks){
            vueTask = new VueTask();
            vueTask.setId(task.getId());
           vueTask.setName(task.getName());
           if(task.getTotal()==0) percentage=0;
           else percentage = task.getCompletion()*100/task.getTotal();
           vueTask.setPercentage(percentage);
            vueTask.setTaskColor(VueTask.percentToColor(percentage));
           vueTasks.add(vueTask);
        }
        return vueTasks;
    }

    /**
     * 用户新建任务
     *
     * @param newTask 任务信息及任务所属项目id
     * @return
     */
    @PostMapping(value = "/tasks")
    public Code addTask(@RequestBody NewTask newTask) throws ProjectNotFoundException {
        Task task = new Task();
        task.setProject(projectService.findProjectByProjectId(newTask.getProjectId()));
        task.setCompletion(newTask.getCompletion());
        task.setInfo(newTask.getInfo());
        task.setName(newTask.getName());
        task.setAgreementUrl(newTask.getAgreementUrl());
        task.setContractUrl(newTask.getContractUrl());
        task.setBeginTime(newTask.getBeginTime());
        task.setChangeCount(newTask.getChangeCount());
        task.setCreateTime(newTask.getCreateTime());
        task.setEndTime(newTask.getEndTime());
        /*
        switch(newTask.getSafety()){
            case("A"):
                task.setSafety(SafetyLevel.A);
                break;
            case("B"):
                task.setSafety(SafetyLevel.B);
                break;
            case("C"):
                task.setSafety(SafetyLevel.C);
                break;
            case("D"):
                task.setSafety(SafetyLevel.D);
                break;
            case("E"):
                task.setSafety(SafetyLevel.E);
                break;
        }
        */
        return taskService.addTask(task, newTask.getProjectId());
    }

    /**
     * 用户通过拖曳的方式将任务放进一个项目里
     *
     * @param taskId    任务id
     * @param projectId 目标项目id
     */
    @PatchMapping(value = "/tasks/{taskId}")
    public Task MoveTaskToProjectById(@PathVariable("taskId") long taskId, @RequestBody OneLong projectId) throws ProjectNotFoundException, TaskNotFoundException {
        long id = projectId.getId();
        return projectService.MoveTaskToProjectById(taskId, id);
    }

    /**
     * 用户获取单个任务
     *
     * @param taskId
     * @return
     */
    @GetMapping(value = "/tasks/{taskId}")
    public Task getTaskByTaskId(@PathVariable("taskId") long taskId) throws TaskNotFoundException {
        return taskService.getTaskByTaskId(taskId);
    }

    /**
     * 负责人激活任务（更新code状态，添加一条userTask信息）
     *
     * @param userId
     * @param origin
     * @return
     */
    @PatchMapping(value = "/users/{userId}/codes/{code}")
    public UserTask addUserTask(@PathVariable("userId") long userId, @PathVariable("code") String origin) throws CodeNotFoundException, UserNotFoundException, TaskNotFoundException {
        Code code = taskService.findCodeByCode(origin);
        if (code != null) {
            code = taskService.updateCodeStatus(code);
            return taskService.addUserTask(code, userId);
        } else
            return null;
    }

    /**
     * 该任务所有成员
     *
     * @param taskId
     * @return
     */
    @GetMapping(value = "/tasks/{taskId}/users")
    public List<UserTask> findUsersByTaskId(@PathVariable("taskId") long taskId) throws TaskNotFoundException {
        return taskService.findUsersByTaskId(taskId);
    }

    /**
     * 队长添加成员
     *
     * @param taskId
     * @param userId
     * @param userTask
     * @return
     */
    @PostMapping(value = "/tasks/{taskId}/users/{userId}")
    public UserTask addTaskUser(@PathVariable("taskId") long taskId, @PathVariable("userId") long userId, @RequestBody UserTask userTask) throws UserNotFoundException, TaskNotFoundException {
        UserTask userTask1 = new UserTask();
        userTask1 = taskService.addTaskUser(taskId, userId, userTask.getJob());
        return userTask1;
    }

    /**
     * 审核任务成员
     *
     * @param userId
     * @param taskId
     * @param verifyStatus
     * @return
     */
    @PatchMapping(value = "/tasks/{taskId}/users/{userId}")
    public UserTask auditTaskUserById(@PathVariable("userId") long userId, @PathVariable("taskId") long taskId, @RequestBody EnumChoice verifyStatus) throws UserNotFoundException, TaskNotFoundException {
        UserTask userTask = new UserTask();
        switch (verifyStatus.getChoice()){
            case "NORMAL":
                userTask = taskService.auditTaskUserById(userId, taskId, VerifyStatus.NORMAL);
                break;
            case "ADD_CHECK":
                userTask = taskService.auditTaskUserById(userId, taskId, VerifyStatus.ADD_CHECK);
                break;
            case "DELETE_CHECK":
                userTask = taskService.auditTaskUserById(userId, taskId, VerifyStatus.DELETE_CHECK);
                break;
        }
        return userTask;
    }

    @GetMapping(value = "/tasks/{taskId}/records")
    public List<Record> getTaskRecords(@PathVariable("taskId") long taskId) throws RecordNotFoundException{
        List<Record> records=taskService.findAllRecord();
        List<Record> result=new ArrayList<Record>();
        for(Record record : records)
        {
            if(record.getAttendance().getTask().getId()==taskId)
            {
                result.add(record);
            }
        }
        return result;
    }

    @GetMapping(value = "/tasks/{taskId}/users/{userId}/records")
    public List<Record> getTaskRecordsByUser(@PathVariable("taskId") long taskId, @PathVariable("userId") long userId) throws RecordNotFoundException{
        List<Record> records=taskService.findAllRecord();
        List<Record> result=new ArrayList<Record>();
        for(Record record : records)
        {
            if(record.getAttendance().getTask().getId()==taskId&&record.getAttendance().getUser().getId()==userId)
            {
                result.add(record);
            }
        }
        return result;
    }

    /**
     * 删除任务成员
     * @param taskId
     * @param userId
     * @throws UserNotFoundException
     * @throws TaskNotFoundException
     */
    @DeleteMapping(value = "/tasks/{taskId}/users/{userId}")
    public void deleteTaskUser(@PathVariable("taskId") long taskId, @PathVariable("userId") long userId) throws UserNotFoundException, TaskNotFoundException {
        taskService.deleteTaskUserById(taskId, userId);
    }
}
