package com.example.learnifier.metadata;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE, reason = "Given file format is not supported")
public class UnsupportedFileFormatException extends RuntimeException {

}
