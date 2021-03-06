package com.wedo.OMS.controller;

import com.wedo.OMS.entity.Attendance;
import com.wedo.OMS.entity.Record;
import com.wedo.OMS.entity.User;
import com.wedo.OMS.exception.AttendanceNotFoundException;
import com.wedo.OMS.exception.RecordNotFoundException;
import com.wedo.OMS.exception.TaskNotFoundException;
import com.wedo.OMS.exception.UserNotFoundException;
import com.wedo.OMS.service.AttendanceService;
import com.wedo.OMS.service.UserService;
import com.wedo.OMS.vo.UserAttendance;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class AttendanceController {
    private final AttendanceService attendanceService;
    private final UserService userService;

    public AttendanceController(AttendanceService attendanceService, UserService userService) {
        this.attendanceService = attendanceService;
        this.userService = userService;
    }

    /**
     * 获取人员考勤
     * @param userId
     * @return
     * @throws UserNotFoundException
     */
    @GetMapping(value = "/users/{userId}/attendances")
    public List<Attendance> getAttendancesByUserId(@PathVariable("userId") long userId) throws UserNotFoundException {
        return attendanceService.getAttendancesByUserId(userId);
    }

    /**
     * 获取任务全部人员考勤
     * @param taskId
     * @return
     * @throws TaskNotFoundException
     */
    @GetMapping(value = "/tasks/{taskId}/attendances")
    public List<Attendance> getTaskAttendancesByTaskId(@PathVariable("taskId") long taskId) throws TaskNotFoundException {
        return attendanceService.getTaskAttendancesByTaskId(taskId);
    }

    /**
     * 获取某考勤的考勤日志
     * @param attendanceId
     * @return
     * @throws RecordNotFoundException
     * @throws AttendanceNotFoundException
     */
    @GetMapping(value = "/attendances/{attendanceId}")
    public Record getRecordByAttendanceId(@PathVariable("attendanceId") long attendanceId) throws RecordNotFoundException, AttendanceNotFoundException {
        return attendanceService.getRecordByAttendanceId(attendanceId);
    }

    /**
     * 用户签到
     * @param userAttendance
     * @return
     * @throws UserNotFoundException
     * @throws TaskNotFoundException
     */
    @PostMapping(value = "/attendances")
    public User signIn(@RequestBody UserAttendance userAttendance) throws UserNotFoundException, TaskNotFoundException {
        Date date = new Date();
        userAttendance.setDate(date);
        return userService.signin(userAttendance.getUserId(), userAttendance.getTaskId(), userAttendance.getDate());
    }

    /**
     * 用户签退
     * @param userAttendance
     * @return
     * @throws UserNotFoundException
     * @throws AttendanceNotFoundException
     * @throws TaskNotFoundException
     */
    @PatchMapping(value = "/attendances")
    public User signOut(@RequestBody UserAttendance userAttendance) throws UserNotFoundException, AttendanceNotFoundException, TaskNotFoundException {
        Date date = new Date();
        userAttendance.setDate(date);
        String content = userAttendance.getContent();
        return userService.signout(userAttendance.getUserId(), userAttendance.getTaskId(), userAttendance.getDate(), content);
    }

}
