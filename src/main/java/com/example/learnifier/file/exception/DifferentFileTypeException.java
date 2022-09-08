package com.example.learnifier.file.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Cannot update file. Uploaded file type is different from original type!")
public class DifferentFileTypeException extends RuntimeException {

}
