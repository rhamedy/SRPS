package com.srps.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.srps.util.FormUtil;

@Controller
public class UserSurveyController {
	
	@RequestMapping(value="/")
	public ModelAndView home() { 
		
		ModelAndView mav = new ModelAndView(); 
		mav.setViewName("index"); 
		
		return mav; 
	}
	
	@RequestMapping(value="/mobile/formList")
	public void getFormList(HttpServletResponse response) throws IOException { 
		
		String formList = FormUtil.prepareFormList(null); 
		
		response.setStatus(200); 
		response.setContentType("text/xml"); 
		response.getOutputStream().print(formList); 
		response.flushBuffer(); 
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.GET)
	public ModelAndView upload() { 
		ModelAndView mav = new ModelAndView(); 
		mav.setViewName("upload"); 
		
		return mav; 
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public void upload(MultipartHttpServletRequest request) {
		System.out.println("test");
		System.out.println("files.size = " + request.getMultiFileMap().size()); 
	}
}
