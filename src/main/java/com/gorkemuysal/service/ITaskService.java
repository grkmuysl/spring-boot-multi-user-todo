package com.gorkemuysal.service;

import com.gorkemuysal.dto.DtoTask;

public interface ITaskService {

	DtoTask findTaskById(Long id);
}
