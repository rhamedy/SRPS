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

import com.srps.model.Survey;
import com.srps.model.User;
import com.srps.service.SurveyServices;
import com.srps.service.UserServices;
import com.srps.util.FormUtil;
import com.srps.util.MailClient;

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

		String username = userServices.getCurrentUsername();
		List<String> formList = surveyServices.getFormsByUsername(username);

		String forms = FormUtil.compileFormList(formList);

		response.setStatus(200);
		response.setContentType("text/xml");
		response.getOutputStream().print(forms);
		response.flushBuffer();
	}

	@RequestMapping(value = "/mobile/download", method = RequestMethod.GET)
	public void downloadForm(HttpServletResponse response,
			@RequestParam(value = "formName", required = true) String formName)
			throws IOException {

		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposation", "attachment;filename="
				+ formName);

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

	@RequestMapping(value = "/mobile/submission", method = RequestMethod.POST)
	public void formSubmission(MultipartHttpServletRequest request,
			HttpServletResponse response) {

		String submissionId = FormUtil.generateSubmissionId();
		String username = userServices.getCurrentUsername();

		surveyServices.createSubmissionDirectoryInDisk(submissionId);

		boolean saveStatus = surveyServices.saveSubmissionContentToDisk(
				request.getFileMap(), submissionId);

		MultipartFile xmlFile = surveyServices.getXmlFile(request.getFileMap());

		saveStatus = surveyServices.processXmlFile(xmlFile, submissionId,
				username);

		if (saveStatus) {
			response.setStatus(201);
		} else {
			response.setStatus(500);
		}
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

	@RequestMapping(value = "/public/home", method = RequestMethod.GET)
	public ModelAndView showHomepage() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("public");

		List<Survey> submissions = surveyServices.getPublicSubmissions();

		mav.addObject("submissions", submissions);

		return mav;

	}

	@RequestMapping(value = "/public/account", method = RequestMethod.POST)
	public void newAccount(@RequestParam(required = true) String firstName,
			@RequestParam(required = true) String lastName, @RequestParam(required = true) String email,
			HttpServletResponse response) {

		User user; 
		
		if(!userServices.isUsernameUsed(email)) {
			
			user = new User(); 
			
			user.setFirstName(firstName); 
			user.setLastName(lastName); 
			user.setEmail(email);
			
			userServices.addAccountRequest(user); 
			userServices.sendConfirmationEmail(user); 
			
			response.setHeader("Msg", "Account request sent. You will receive a confirmation email once its created."); 
			response.setStatus(200);
		} else { 
			response.setHeader("Msg", "Username is use. Choose a different one."); 
			response.setStatus(302); 
		}
		

	}
}
