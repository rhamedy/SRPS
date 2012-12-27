package com.srps.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.srps.dao.SurveyDao;


@Repository
public class SurveyService {
	
	@Autowired 
	private SurveyDao surveyDao; 
	
	public int validateXml(MultipartFile file) { 
		
		try { 
			FileOutputStream fos = new FileOutputStream("/home/fareen/workspace/forms/temp_file.xml"); 
			fos.write(file.getBytes());
			fos.close(); 
		} catch(IOException ex) { 
			ex.printStackTrace();
		}
		
		File schemaFile = new File("/home/fareen/workspace/forms/web-app_2_4.xsd"); 
		Source xmlFile = new StreamSource(new File("/home/fareen/workspace/forms/temp_file.xml")); 
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
		Schema schema = null; 
		try{ 
			schema = schemaFactory.newSchema(schemaFile); 
		} catch(SAXException sax) {
			sax.printStackTrace();
			return 1; 
		}
		
		try { 
			Validator validator = schema.newValidator(); 
			validator.validate(xmlFile);
		} catch(SAXException sax) { 
			sax.printStackTrace(); 
			return -1; 
		} catch(IOException ex) { 
			return 1; 
		}
		
		return 0; 
	}
	
	public MultipartFile getXmlFile(Map<String, MultipartFile> files) { 
		for(Map.Entry<String, MultipartFile> entry: files.entrySet()) { 
			MultipartFile temp = entry.getValue(); 
			if(temp.getOriginalFilename().endsWith(".xml")) { 
				return temp; 
			}
		}
		return null;
	}
	
	public void processXmlFile(MultipartFile file) { 
		
	}
	
	public int getNextFormId() { 
		return surveyDao.getNextFormId(); 
	}
	
	public void storeBlankForm(int formId, String filename, String username) { 
		surveyDao.storeBlankForm(formId, filename); 
		
		if(!username.equals("everyone")) { 
			surveyDao.relateFormToUser(formId, username); 
		} 
	}
	
	public void storeXmlFileInDisc(MultipartFile file) { 
		try {
			FileOutputStream fos = new FileOutputStream("/home/fareen/workspace/forms/temp_file.xml"); 
			fos.write(file.getBytes());
			fos.close(); 
		} catch(IOException ex) { 
			ex.printStackTrace();
		}
	}
}
