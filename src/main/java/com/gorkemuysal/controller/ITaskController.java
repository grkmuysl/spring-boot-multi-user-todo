package com.gorkemuysal.controller;

import com.gorkemuysal.dto.DtoTask;

public interface ITaskController {
	public DtoTask findTaskById(Long id);
}
