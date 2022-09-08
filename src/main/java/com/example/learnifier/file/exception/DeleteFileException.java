package com.example.learnifier.file.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Cannot delete this file")
public class DeleteFileException extends RuntimeException{
}
