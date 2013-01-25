package com.srps.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.srps.model.Survey;
import com.srps.model.SurveyMapper;
import com.srps.model.FormElement;

public class FileUtil {

	public static void deleteDirectory(File directory) {
		File[] files = directory.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					deleteDirectory(f);
				} else {
					f.delete();
				}
			}
		}
		directory.delete();
	}

	public static String readMapperFile(Survey survey) {
//		File submissionDir = new File("/home/fareen/workspace/submissions/"
//				+ survey.getSubmissionId());
//		File formsDir = new File("/home/fareen/workspace/forms");
		
		File submissionDir = new File("C:\\project\\submissions\\"
				+ survey.getSubmissionId());
		File formsDir = new File("C:\\project\\forms");

		File xmlFile = null;
		File mapperFile = null;

		File[] tempFiles = submissionDir.listFiles();

		for (File f : tempFiles) {
			if (f.getName().endsWith(".xml")) {
				xmlFile = f;
				break;
			}
		}

		tempFiles = formsDir.listFiles();

		for (File f : tempFiles) {
			if (f.getName().startsWith(survey.getFormName())
					&& f.getName().endsWith(".txt")) {
				mapperFile = f; 
				break; 
			}
		}
		
		SurveyMapper surveyMapper = populateMapperFile(mapperFile); 
		surveyMapper.setFormName(survey.getFormName()); 
		surveyMapper.setUsername(survey.getUsername()); 
		surveyMapper.setVisibility(survey.isVisibility()); 
		
		String htmlView = FormUtil.generateHtmlFromXml(survey, surveyMapper, xmlFile); 
		
		return htmlView; 
	}
	
	public static SurveyMapper populateMapperFile(File file) { 
		try { 
			BufferedReader br = new BufferedReader(new FileReader(file)); 
			String line = null; 
			String[] chunks = null; 
			FormElement element = null; 
			
			SurveyMapper surveyMapper = new SurveyMapper(); 
			
			List<FormElement> elements = new ArrayList<FormElement>(); 
			
			while((line=br.readLine()) != null) {
				chunks = line.split("-"); 
				
				element = new FormElement(); 
				
				element.setElement(chunks[0].trim()); 
				element.setType(chunks[1].trim()); 
				element.setText(chunks[2].trim()); 
				element.setOther(chunks[3].trim()); 
				
				elements.add(element); 
			}
			
			surveyMapper.setElements(elements); 
			
			return surveyMapper; 
			
		} catch(Exception ex) { 
			ex.printStackTrace();
			return null; 
		}
	}
}
