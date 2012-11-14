package com.srps.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserSurveyController {
	
	@RequestMapping(value="/homess")
	public ModelAndView home() { 
		
		ModelAndView mav = new ModelAndView(); 
		mav.setViewName("index"); 
		
		return mav; 
	}
}
