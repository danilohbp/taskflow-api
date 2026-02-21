package com.danilohbp.taskflowapi.application.service;

import com.danilohbp.taskflowapi.application.usecase.task.CreateTaskRequest;
import com.danilohbp.taskflowapi.application.usecase.task.TaskResponse;
import com.danilohbp.taskflowapi.application.usecase.task.UpdateTaskRequest;

import java.util.List;

public interface ITaskService {

    TaskResponse create(CreateTaskRequest request);

    TaskResponse getById(Long id);

    TaskResponse updateById(Long id, UpdateTaskRequest request);

    List<TaskResponse> listAll();
}
