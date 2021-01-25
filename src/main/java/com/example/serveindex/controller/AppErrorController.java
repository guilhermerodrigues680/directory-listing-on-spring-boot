package com.example.serveindex.controller;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

@Component
public class AppErrorController extends BasicErrorController {

    public AppErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes, new ErrorProperties());
        getErrorProperties().setIncludeException(true);
        getErrorProperties().setIncludeStacktrace(ErrorProperties.IncludeStacktrace.ALWAYS);
    }

    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        ResponseEntity<Map<String, Object>> error = super.error(request);
        error.getBody().put("customobj", Collections.singletonMap("key1", "value1"));
        return error;
    }

    @Override
    public ResponseEntity<String> mediaTypeNotAcceptable(HttpServletRequest request) {
        return super.mediaTypeNotAcceptable(request);
    }

    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = super.errorHtml(request, response);
        modelAndView.addObject("customobj", "customvalue");
        return modelAndView;
    }

// handles errors triggered in XML endpoints
//    @RequestMapping(produces = MediaType.APPLICATION_XML_VALUE)
//    public ResponseEntity<Map<String, Object>> xmlError(HttpServletRequest request) {
//
//        return ResponseEntity.ok(Collections.singletonMap("chave", "valor"));
//    }
}
