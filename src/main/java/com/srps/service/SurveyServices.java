package com.srps.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
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
public class SurveyServices {
	
	@Autowired 
	private SurveyDao surveyDao; 
	
	public int validateXml(MultipartFile file) throws MalformedURLException { 
		try { 
			FileOutputStream fos = new FileOutputStream("/home/fareen/workspace/forms/temp_file.xml"); 
			fos.write(file.getBytes());
			fos.close(); 
		} catch(IOException ex) { 
			ex.printStackTrace();
		}
		
		Source xmlFile = new StreamSource(new File("/home/fareen/workspace/forms/temp_file.xml")); 
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI); 
		Schema schema = null; 
		try{ 
			schema = schemaFactory.newSchema(new URL("http://java.sun.com/xml/ns/j2ee/web-app_2_4.xml")); 
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
	
	public List<String> getFormsByUsername(String username) { 
		return surveyDao.retrieveFormsByUsername(username); 
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
		surveyDao.relateFormToUser(formId, username); 
	}
	
	public void storeBlankFormInDisc(MultipartFile file, String filename) { 
		try {
			FileOutputStream fos = new FileOutputStream("/home/fareen/workspace/forms/" + filename); 
			fos.write(file.getBytes());
			fos.close(); 
		} catch(IOException ex) { 
			ex.printStackTrace();
		}
	}
}