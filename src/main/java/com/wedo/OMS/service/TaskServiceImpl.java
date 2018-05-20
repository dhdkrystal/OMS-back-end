package com.wedo.OMS.service;

import com.wedo.OMS.entity.Task;
import com.wedo.OMS.entity.User;
import com.wedo.OMS.entity.UserTask;
import com.wedo.OMS.enums.UserTaskRole;
import com.wedo.OMS.enums.VerifyStatus;
import com.wedo.OMS.repository.TaskRepository;
import com.wedo.OMS.repository.UserRepository;
import com.wedo.OMS.repository.UserTaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private UserTaskRepository userTaskRepository;
    private UserRepository userRepository;

    TaskServiceImpl(TaskRepository taskRepository, UserTaskRepository userTaskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userTaskRepository = userTaskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Task> listTasksByUserId(Long userId) {
        User user = userRepository.findUserById(userId);
        List<UserTask> userTasks = userTaskRepository.findUserTasksByUser(user);
        List<Task> tasks = new ArrayList<>();
        for (UserTask ut : userTasks) {
            tasks.add(ut.getTask());
        }
        return tasks;
    }

    @Override
    public Task getTaskByTaskId(Long taskId) {
        return taskRepository.findTaskById(taskId);
    }

    @Override
    public List<Task> getTasksByTaskname(String taskname) {
        return taskRepository.findTasksByName(taskname);
    }

    @Override
    public void addTask(Task task) {
        taskRepository.save(task);

    }

    @Override
    public Task updateTaskByTaskId(Long taskId, Task task) {
        Task newTask = taskRepository.findTaskById(taskId);
        newTask = task;
        taskRepository.save(newTask);
        return newTask;
    }

    @Override
    public void addTaskUser(Long userId, UserTaskRole utr) {
        User user = userRepository.findUserById(userId);
        UserTask userTask = new UserTask();
        userTask.setUser(user);
        userTask.setStatus(VerifyStatus.UNAUDITED);
        userTask.setUserTaskRole(utr);


    }

    @Override
    public UserTask auditTaskUserById(Long userId, Long taskId, VerifyStatus status) {
        User user = userRepository.findUserById(userId);
        Task task = taskRepository.findTaskById(taskId);
        UserTask userTask = userTaskRepository.findUserTasksByUserAndTask(user, task);
        userTask.setStatus(status);
        userTask.setTask(task);
        userTaskRepository.save(userTask);
        return userTask;
    }

    @Override
    public void deleteTaskUserById(Long userId, Long taskId) {
        User user = userRepository.findUserById(userId);
        Task task = taskRepository.findTaskById(taskId);
        userTaskRepository.deleteByUserAndTask(user, task);
    }
}
