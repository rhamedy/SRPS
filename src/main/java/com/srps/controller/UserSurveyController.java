package com.srps.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.srps.util.CustomMap;
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

		String username = userServices.getCurrentUsername();
		boolean bool = userServices.isAdmin(username);

		User user = userServices.getUserByUsername(username);

		if (bool) {
			mav.setViewName("admin");
			List<User> users = userServices.retrieveUsers();
			List<CustomMap> forms = surveyServices.getAllForms();
			List<Survey> submissions = surveyServices.getSubmissions("", 3);

			mav.addObject("users", users);
			mav.addObject("forms", forms);
			mav.addObject("submissions", submissions);
		} else {
			mav.setViewName("user");
			List<Survey> submissions = surveyServices.getSubmissions(username,
					2);
			List<CustomMap> forms = surveyServices.getFormsByUsername(username);

			mav.addObject("submissions", submissions);
			mav.addObject("forms", forms);
		}

		mav.addObject("user", user);
		return mav;
	}

	@RequestMapping(value = "/mobile/formList")
	public void getFormList(HttpServletResponse response) throws IOException {

		String username = userServices.getCurrentUsername();
		List<String> formList = surveyServices.getFormNamesByUsername(username);

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

		List<String> allUsers = userServices.retrieveUsernames();
		mav.addObject("users", allUsers);

		return mav;
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public void upload(@RequestParam String username,
			MultipartHttpServletRequest request) throws MalformedURLException {
		MultipartFile xmlFile = surveyServices.getXmlFile(request.getFileMap());
		MultipartFile mapperFile = surveyServices.getMapperFile(request
				.getFileMap());

		// surveyServices.validateXml(xmlFile);

		if (xmlFile == null) {
			// invalid form upload
		} else {
			int formId = surveyServices.getNextFormId();
			String filename = FormUtil.escapeEmptySpaces(
					xmlFile.getOriginalFilename(), ++formId);

			surveyServices.storeBlankForm(formId, filename, username);
			surveyServices.storeBlankFormInDisc(xmlFile, filename);
			surveyServices.storeSurveyMapperInDisc(mapperFile,
					filename.replace(".xml", ".txt"));
		}
	}

	@RequestMapping(value = "/public/home", method = RequestMethod.GET)
	public ModelAndView showHomepage() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("public");

		// all the submissions which is public, differentiated by value 1
		List<Survey> submissions = surveyServices.getSubmissions("", 1);

		mav.addObject("submissions", submissions);

		return mav;
	}

	@RequestMapping(value = "/public/account", method = RequestMethod.POST)
	public void newAccount(@RequestParam(required = true) String firstName,
			@RequestParam(required = true) String lastName,
			@RequestParam(required = true) String email,
			HttpServletResponse response) {

		User user;

		if (!userServices.isUsernameUsed(email)) {

			user = new User();

			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setEmail(email);

			userServices.addAccountRequest(user);
			userServices.sendConfirmationEmail(user);

			response.setHeader("Msg", "Account request sent successfully.");
			response.setStatus(200);
		} else {
			response.setHeader("Msg",
					"Username is use. Choose a different one.");
			response.setStatus(302);
		}
	}

	@RequestMapping(value = "/user/update", method = RequestMethod.POST)
	public void updateUser(@RequestParam(required = true) String firstName,
			@RequestParam(required = true) String lastName,
			@RequestParam(required = true) String dateOfBirth,
			@RequestParam(required = true) String email,
			HttpServletResponse response) {

		User user = userServices.getUserByUsername(email);

		user.setFirstName(firstName);
		user.setLastName(lastName);

		try {
			java.util.Date dd = null;

			if (dateOfBirth.contains("-")) {
				dd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
						.parse(dateOfBirth);
			} else {
				dd = new SimpleDateFormat("mm/dd/yyyy", Locale.ENGLISH)
						.parse(dateOfBirth);
			}

			java.sql.Date ddd = new java.sql.Date(dd.getTime());
			user.setDateOfBirth(ddd);

			userServices.updateUser(user);
			response.setHeader("Msg", "Details updated successfully.");
			response.setStatus(200);

		} catch (ParseException ex) {
			ex.printStackTrace();
			response.setStatus(302);
			response.setHeader("Msg", "Updating failed.");
		}
	}

	@RequestMapping(value = "/form/delete", method = RequestMethod.GET)
	public void deleteForm(@RequestParam String id, HttpServletResponse response) {

		int formId = Integer.parseInt(id);

		surveyServices.deleteFormRelation(formId);
		surveyServices.deleteForm(formId);

		response.setStatus(200);
	}

	@RequestMapping(value = "/submission/delete", method = RequestMethod.POST)
	public void deleteSubmission(@RequestParam String submissionId,
			HttpServletResponse response) {
		System.out.println("submissionId for deleting : " + submissionId);

		surveyServices.deleteSubmission(submissionId);

		response.setStatus(200);
	}

	@RequestMapping(value = "/user/edit", method = RequestMethod.GET)
	public ModelAndView editUser(@RequestParam String email) {
		ModelAndView mav = new ModelAndView();

		mav.setViewName("editUser");

		User user = userServices.getUserByUsername(email);
		String role = "User";

		if (userServices.userHasRoleAssigned(user.getEmail())) {
			role = userServices.getUserRole(email);
		}

		System.out.println("user= " + user.getFirstName() + ", role = " + role
				+ ", disabled = " + user.isDisabled());

		mav.addObject("user", user);
		mav.addObject("role", role);

		return mav;
	}

	@RequestMapping(value = "/user/edit", method = RequestMethod.POST)
	public void editUser(@RequestParam String firstName,
			@RequestParam String lastName, @RequestParam String dateOfBirth,
			@RequestParam String email, @RequestParam String disabled,
			@RequestParam String role, HttpServletResponse response) {

		boolean d = disabled.equals("True") ? true : false;
		int roleId = userServices.getRoleIdByRoleName(role);

		User user = userServices.getUserByUsername(email);
		java.util.Date dd = null;
		try {
			if (dateOfBirth.contains("-")) {
				dd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
						.parse(dateOfBirth);
			} else {
				dd = new SimpleDateFormat("mm/dd/yyyy", Locale.ENGLISH)
						.parse(dateOfBirth);
			}

			java.sql.Date ddd = new java.sql.Date(dd.getTime());
			user.setDateOfBirth(ddd);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}

		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setDisabled(d);

		userServices.updateUser(user);

		if (userServices.userHasRoleAssigned(user.getEmail())) {
			if (!userServices.getUserRole(user.getEmail()).equals(role)) {
				userServices.updateUserRole(user.getEmail(), roleId);
			}
		} else {
			userServices.assignUserRole(user.getEmail(), roleId);
		}

		if (!userServices.doesUserHavePassword(user.getEmail())) {

			System.out.println("no user does not have password!!!!");

			String randomPassword = UUID.randomUUID().toString();
			String shortPassword = new StringBuffer(randomPassword).substring(
					0, 6);

			userServices.changePassword(user.getEmail(), shortPassword);
		}

		response.setHeader("Msg",
				"The edited fields were updated successfully.");
		response.setStatus(200);
	}

	@RequestMapping(value = "/user/delete", method = RequestMethod.GET)
	public void deleteUser(@RequestParam String username) {

		if (surveyServices.hasFormsAssociatedWith(username)) {
			List<CustomMap> forms = surveyServices.getFormsByUsername(username);
			if (forms != null) {
				for (CustomMap f : forms) {
					surveyServices.deleteFormRelation(Integer.parseInt((f
							.getKey()).toString()));
					surveyServices.deleteForm(Integer.parseInt((f.getKey())
							.toString()));
				}
			}
		}

		if (surveyServices.hasSubmissionsAssociatedWith(username)) {
			List<Survey> submissions = surveyServices.getSubmissions(username,
					2);

			if (submissions != null) {
				for (Survey s : submissions) {
					surveyServices.deleteSubmission(s.getSubmissionId());
				}
			}
		}

		if (userServices.userHasRoleAssigned(username)) {
			userServices.deleteUserRoleRelation(username);
		}

		userServices.deleteUser(username);
	}

	@RequestMapping(value = "/user/resetPassword", method = RequestMethod.GET)
	public void resetPassword(@RequestParam String username,
			HttpServletResponse response) {
		String randomPassword = UUID.randomUUID().toString();
		String shortPassword = new StringBuffer(randomPassword).substring(0, 6);

		userServices.changePassword(username, shortPassword);

		response.setStatus(200);
	}

	@RequestMapping(value = "/submission/view", method = RequestMethod.GET)
	public ModelAndView viewSubmission(@RequestParam String id) {

		ModelAndView mav = new ModelAndView();
		mav.setViewName("viewSubmission");

		Survey survey = surveyServices.getSubmissionById(id);

		String htmlView = surveyServices.generateHtmlViewFromSubmission(survey);

		mav.addObject("htmlContent", htmlView);

		//System.out.println("htmlContent : " + htmlView);

		return mav;
	}

	@RequestMapping(value = "/submission/image", method = RequestMethod.GET)
	public void getImage(@RequestParam String id,
			@RequestParam String submissionId, HttpServletResponse response) {
		
		System.out.println("inside /submission/image id : " + id); 
		
		
		response.setContentType("image/jpeg");
		File file = new File("/home/fareen/workspace/submissions/" + submissionId, id);
		try { 
			InputStream is = new FileInputStream(file); 
			IOUtils.copy(is, response.getOutputStream()); 
		} catch(Exception ex) { 
			ex.printStackTrace(); 
		}
	}
}
