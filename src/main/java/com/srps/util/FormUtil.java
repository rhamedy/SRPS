package com.srps.util;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.srps.model.FormElement;
import com.srps.model.Survey;
import com.srps.model.SurveyMapper;

public class FormUtil {

	// used for testing purpose
	public static String prepareFormList(String forms) {
		StringBuilder output = new StringBuilder();

		output.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		output.append("<forms>");
		output.append("<form url=\"http://localhost:8080/SRPS/odk/form?name=abc\">abc</form>\n");
		output.append("<form url=\"http://localhost:8080/SRPS/odk/form?name=123\">123</form>\n");
		output.append("<form url=\"http://localhost:8080/SRPS/odk/form?name=xyz\">xyz</form>\n");
		output.append("<form url=\"http://localhost:8080/SRPS/odk/form?name=456\">456</form>\n");
		output.append("</forms>");

		return output.toString();
	}

	// build xml representation of available forms
	public static String compileFormList(List<String> forms) {

		StringBuilder output = new StringBuilder();

		output.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		output.append("<forms>");

		for (String form : forms) {
			output.append("<form url=\"http://192.168.56.101:8443/SRPS/mobile/download?formName="
					+ form + "\"> " + form + "</form>\n");
		}

		output.append("</forms>");

		System.out.println("output : " + output);

		return output.toString();

	}

	public static String escapeEmptySpaces(String filename, int formId) {
		StringBuilder modifiedFilename = new StringBuilder();
		for (int i = 0; i < filename.length(); i++) {
			if (filename.charAt(i) == '.') {
				modifiedFilename.append("_" + formId + ".");
				continue;
			}
			if (filename.charAt(i) == ' ') {
				modifiedFilename.append("_");
			} else {
				modifiedFilename.append(filename.charAt(i));
			}
		}
		return modifiedFilename.toString();
	}

	public static String generateSubmissionId() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	public static Survey populateObject(Survey survey, List<Element> elements) {
		boolean image = true;
		boolean audio = true;
		boolean location = true;
		boolean visibility = true;
		boolean deviceId = true;

		for (Element e : elements) {
			if (image && e.getName().equals("Image")) {
				survey.setImage(true);
				image = false;
				continue;
			}

			if (audio && e.getName().equals("Sound")) {
				survey.setAudio(true);
				audio = false;
				continue;
			}

			if (location && e.getName().equals("Location")) {
				survey.setGeopoint(true);
				location = false;
				continue;
			}

			if (deviceId && e.getName().equals("DeviceId")) {
				survey.setDeviceId(e.getValue());
				deviceId = false;
				continue;
			}

			if (visibility && e.getName().equals("Visibility")) {
				if (e.getValue().equals("no")) {
					survey.setVisibility(false);
				}
				visibility = true;
				continue;
			}

			if (location && audio && image && visibility && deviceId) {
				break;
			}
		}

		return survey;
	}

	public static String generateHtmlFromXml(Survey survey,
			SurveyMapper surveyMapper, File xmlFile) {

		SAXBuilder builder = new SAXBuilder();
		StringBuffer htmlView = new StringBuffer();

		try {
			Document document = builder.build(xmlFile);
			Element data = document.getRootElement();

			List<Element> elements = data.getChildren();

			for (Element e : elements) {
				if (!(e.getChildren().size() == 0 && e.getValue().equals(""))) {
					htmlView.append(
							generateHtmlElement(e,
									findElement(e, surveyMapper.getElements()),survey.getSubmissionId()));
				}
			}

			return htmlView.toString();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public static FormElement findElement(Element e,
			List<FormElement> formElements) {
		for (FormElement fe : formElements) {
			if (fe.getElement().equals(e.getName())) {
				return fe;
			}
		}
		return null;
	}

	public static String generateHtmlElement(Element e, FormElement fe,
			String submissionId) {

		String temp = null;

		if (fe.getType().equals("string")) {

			temp = "<br>" + "<label for=\"" + e.getName() + "\">"
					+ fe.getText() + "</label><br>"
					+ "<input type=\"text\" name =\"" + e.getName()
					+ "\" value=\"" + e.getValue() + "\" /><br>";

			return temp;

		} else if (fe.getType().equals("text")) {

			temp = "<br><label for=\"" + e.getName() + "\">" + fe.getText()
					+ "</label><br>"
					+ "<textarea rows=\"10\" cols=\"10\" name=\"" + e.getName()
					+ "\" value=\"" + e.getValue() + "\">" + e.getValue()
					+ "</textarea><br><br>";

			return temp;

		} else if (fe.getType().equals("boolean")) {

			String s = "yes";
			String y = "no";

			temp = "<br><label for=\"" + e.getName() + "\">" + fe.getText()
					+ "</label><br>" + "<input type=\"radio\" name=\""
					+ e.getName() + "\" value=\"" + e.getValue()
					+ "\" checked >" + e.getValue()
					+ "</input><br><input type=\"radio\" name=\"" + e.getName()
					+ "\" value=\"" + ((e.getValue().equals(s)) ? y : s)
					+ "\">" + ((e.getValue().equals(s)) ? y : s) + "</input><br><br>";

			return temp;

		} else if (fe.getType().equals("date")) {

			temp = "<br>" + "<label for=\"" + e.getName() + "\">"
					+ fe.getText() + "</label><br>"
					+ "<input type=\"text\" name =\"" + e.getName()
					+ "\" id=\"dateOfBirth\" value=\"" + e.getValue()
					+ "\" /><br><br>";

			return temp;

		} else if (fe.getType().equals("sound")) {
			temp = "<br>"
					+ "<label for=\""
					+ e.getName()
					+ "\">"
					+ fe.getText()
					+ "</label><br><br>"
					+ "<embed name=\""
					+ e.getName()
					+ "\" height=\"50\" width=\"100\" src=\"http://localhost:8080/SRPS/submission/sound?id="
					+ e.getValue() + "&submissionId=" + submissionId + "\" /><br><br>";

			return temp;

		} else if (fe.getType().equals("image")) {

			temp = "<br>"
					+ "<label for=\""
					+ e.getName()
					+ "\">"
					+ fe.getText()
					+ "</label><br><br>"
					+ "<img name=\""
					+ e.getName()
					+ "\" src=\"http://localhost:8080/SRPS/submission/image?id="
					+ e.getValue() + "&submissionId=" + submissionId + "\" /><br><br>";

			return temp;

		} else if (fe.getType().equals("geopoint")) {

			String[] chunks = e.getValue().split(" ");
			if (chunks.length < 3) {
				chunks = new String[3];
				chunks[0] = "0.0";
				chunks[1] = "0.0";
				chunks[2] = "0";
			}

			temp = "<br><p>" + fe.getText() + "</p><br>"
					+ "<label for=\"Latitude\">Latitude</label><br>"
					+ "<input type=\"text\" name =\"Latitude\" value=\""
					+ chunks[0] + "\" /><br>"
					+ "<br><label for=\"Longitude\">Longitude</label><br>"
					+ "<input type=\"text\" name =\"Longitude\" value=\""
					+ chunks[1] + "\" /><br>"
					+ "<br><label for=\"Accuracy\">Accuracy</label><br>"
					+ "<input type=\"text\" name =\"Accuracy\" value=\""
					+ chunks[2] + "\" /><br>";

			return temp;

		}

		return "";
	}
}
