package com.stv.medinfosys.web;

import com.stv.medinfosys.exception.ObjectAlreadyExistsException;
import com.stv.medinfosys.exception.ObjectNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ObjectAlreadyExistsException.class, ObjectNotFoundException.class})
    public ModelAndView handleException(Exception e) {
        ModelAndView modelAndView = new ModelAndView("custom-error.html");
        modelAndView.addObject("errorMessage", e.getMessage());
        return modelAndView;
    }
}