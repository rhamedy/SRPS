package com.srps.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.srps.dao.SurveyDao;
import com.srps.model.Survey;
import com.srps.util.CustomMap;
import com.srps.util.FileUtil;
import com.srps.util.FormUtil;

@Repository
public class SurveyServices {

	@Autowired
	private SurveyDao surveyDao;

	public int validateXml(MultipartFile file) throws MalformedURLException {
		try {
			FileOutputStream fos = new FileOutputStream(
					"/home/fareen/workspace/forms/temp_file.xml");
			fos.write(file.getBytes());
			fos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		Source xmlFile = new StreamSource(new File(
				"/home/fareen/workspace/forms/temp_file.xml"));
		SchemaFactory schemaFactory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = null;
		try {
			schema = schemaFactory.newSchema(new URL(
					"http://java.sun.com/xml/ns/j2ee/web-app_2_4.xml"));
		} catch (SAXException sax) {
			sax.printStackTrace();
			return 1;
		}

		try {
			Validator validator = schema.newValidator();
			validator.validate(xmlFile);
		} catch (SAXException sax) {
			sax.printStackTrace();
			return -1;
		} catch (IOException ex) {
			return 1;
		}

		return 0;
	}

	public List<String> getFormNamesByUsername(String username) {
		return surveyDao.retrieveFormNamesByUsername(username);
	}

	public MultipartFile getXmlFile(Map<String, MultipartFile> files) {
		for (Map.Entry<String, MultipartFile> entry : files.entrySet()) {
			MultipartFile temp = entry.getValue();
			if (temp.getOriginalFilename().endsWith(".xml")) {
				return temp;
			}
		}
		return null;
	}

	public void createSubmissionDirectoryInDisk(String submissionId) {
		File dir = new File("/home/fareen/workspace/submissions", submissionId);
		dir.mkdir();
	}

	public boolean saveSubmissionContentToDisk(
			Map<String, MultipartFile> files, String submissionId) {

		File file = null;

		try {
			for (Map.Entry<String, MultipartFile> entry : files.entrySet()) {
				MultipartFile f = entry.getValue();
				file = new File("/home/fareen/workspace/submissions/"
						+ submissionId, f.getOriginalFilename());
				f.transferTo(file);
				file.createNewFile();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean processXmlFile(MultipartFile file, String submissionId, String username) {
		File xmlFile = new File("/home/fareen/workspace/submissions/"
				+ submissionId, file.getOriginalFilename());
		
		Survey survey;
		
		SAXBuilder builder = new SAXBuilder(); 
		
		try { 
			Document document = builder.build(xmlFile); 
			Element data = document.getRootElement(); 
			
			survey = new Survey(); 
			
			survey.setSubmissionId(submissionId);
			survey.setFormName(data.getAttribute("id").getValue());
			survey.setUsername(username);
			survey.setSubmissionDate(new Date()); 
			
			List<Element> elements = data.getChildren(); 
			
			survey = FormUtil.populateObject(survey, elements); 
			
			surveyDao.saveSubmission(survey); 
			
		} catch(IOException ex) { 
			ex.printStackTrace(); 
			return false; 
		} catch (JDOMException ex) { 
			ex.printStackTrace(); 
			return false; 
		}
		return true;
	}

	public int getNextFormId() {
		return surveyDao.getNextFormId();
	}

	public void storeBlankForm(int formId, String filename, String username) {
		surveyDao.storeBlankForm(formId, filename);
		surveyDao.relateFormToUser(formId, username);
	}

	public void storeBlankFormInDisc(MultipartFile file, String filename) {
		try {
			FileOutputStream fos = new FileOutputStream(
					"/home/fareen/workspace/forms/" + filename);
			fos.write(file.getBytes());
			fos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public List<Survey> getSubmissions(String username, int flag) { 
		return surveyDao.getSubmissions(username, flag); 
	}
	
	public List<CustomMap> getFormsByUsername(String username) { 
		return surveyDao.getFormsByUsername(username); 
	}
	
	public List<CustomMap> getAllForms() { 
		return surveyDao.getAllForms(); 
	}
	
	public void deleteFormRelation(int formId) { 
		surveyDao.deleteFormRelation(formId); 
	}
	
	public void deleteForm(int formId) { 
		
		String formName = surveyDao.getFormNameById(formId); 
		
		surveyDao.deleteForm(formId); 
		
		File file = new File("/home/fareen/workspace/forms", formName); 
		if(file.exists()) { 
			file.delete(); 
		}
	}
	
	public void deleteSubmission(String submissionId) {
		surveyDao.deleteSubmission(submissionId); 
		
		File dir = new File("/home/fareen/workspace/submissions/" + submissionId); 
		if(dir.exists()) { 
			FileUtil.deleteDirectory(dir); 
		}
	}
}
