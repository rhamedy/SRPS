package com.srps.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.srps.service.SurveyServices;
import com.srps.service.UserServices;
import com.srps.util.FormUtil;

@Controller
public class UserSurveyController {

	@Autowired
	private SurveyServices surveyServices;

	@Autowired
	private UserServices userServices;

	@RequestMapping(value = "/")
	public ModelAndView home() {

		ModelAndView mav = new ModelAndView();
		mav.setViewName("index");

		return mav;
	}

	@RequestMapping(value = "/mobile/formList")
	public void getFormList(HttpServletResponse response) throws IOException {

		// String formList = FormUtil.prepareFormList(null);

		String username = userServices.getCurrentUsername();
		List<String> formList = surveyServices.getFormsByUsername(username);

		String forms = FormUtil.compileFormList(formList);

		response.setStatus(200);
		response.setContentType("text/xml");
		response.getOutputStream().print(forms);
		response.flushBuffer();
	}

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public ModelAndView upload() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("upload");

		List<String> allUsers = userServices.retrieveAllUsers();
		mav.addObject("users", allUsers);

		return mav;
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public void upload(@RequestParam String username,
			MultipartHttpServletRequest request) throws MalformedURLException {
		MultipartFile xmlFile = surveyServices.getXmlFile(request.getFileMap());

		// surveyServices.validateXml(xmlFile);

		if (xmlFile == null) {
			// invalid form upload
		} else {
			int formId = surveyServices.getNextFormId();
			String filename = FormUtil.escapeEmptySpaces(
					xmlFile.getOriginalFilename(), formId);
			surveyServices.storeBlankForm(formId, filename, username);
			surveyServices.storeBlankFormInDisc(xmlFile, filename);
		}
	}

	@RequestMapping(value = "/mobile/download", method = RequestMethod.GET)
	public void downloadForm(HttpServletResponse response,
			@RequestParam(value = "formName", required = true) String formName)
			throws IOException {

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposation", "attachment;filename=" + formName);
		
		System.out.println("formName : " + formName); 

		File file = new File("/home/fareen/workspace/forms/" + formName);
		FileInputStream fin = new FileInputStream(file);
		ServletOutputStream out = response.getOutputStream();

		byte[] outByte = new byte[4096];

		while (fin.read(outByte, 0, 4096) != -1) {
			out.write(outByte, 0, 4096);
		}

		fin.close();
		out.flush();
		out.close();
	}
}
