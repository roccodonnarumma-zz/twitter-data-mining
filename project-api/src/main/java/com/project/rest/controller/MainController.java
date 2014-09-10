package com.project.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * REST controller for the root path.
 * 
 * @author rdonnarumma
 * 
 */
@Controller
public class MainController {

    /**
     * GET API the returns the main HTML page on the root path.
     * 
     * @return the main HTML page on the root path.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getApp() {
        return "twitter-movies-app.html";
    }
}
