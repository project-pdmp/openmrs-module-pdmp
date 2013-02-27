package org.openmrs.module.pdmp_query.web.controller;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.openmrs.module.pdmp_query.Config;
import org.openmrs.module.pdmp_query.ConfigService;


/**
 * The main controller.
 */
@Controller
public class PDMPPageController {

	protected final Log log = LogFactory.getLog(getClass());
	protected String sPDMPUrl;
	protected String sPDMPUserID;
	protected String sPDMPPassword;
	protected String sWorkerUrl;
	protected String sWorkerType;

	@RequestMapping(value = "/module/pdmp_query/pdmp", method = RequestMethod.GET)
	public void manage(ModelMap model, @RequestParam("patientId") Integer patientId) {
            Config c = Context.getService(ConfigService.class).getConfig();
            String userpassword = c.getUser() + ":" + c.getPassword();
            String baseURL = c.getUrl();
		String sNoRecordsFound = "No PDMP Records Found";
		String sUrl = null;
		String sResponse = null;
		String sType = null;
		StringBuilder sb = null;
		Patient patient = Context.getPatientService().getPatient(patientId);
		Person person = Context.getPersonService().getPerson(patient);

		String sGivenName = "";
		String sFamilyName = "";
		String sGender = "";
		String sBirthdate = "";
		String sAddress = "";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");

		sGivenName = person.getGivenName();
		sFamilyName = person.getFamilyName();
		if (person.getGender().equalsIgnoreCase("F")) {
			sGender = "female";
		} else if (person.getGender().equalsIgnoreCase("M")) {
			sGender = "male";
		}
		sBirthdate = dateFormat.format(person.getBirthdate());
		sAddress = person.getPersonAddress().getCityVillage();

		sb = new StringBuilder();
		sResponse = PDMPGet(sb, model, baseURL + "search?utf8=%E2%9C%93&given=" + sGivenName +
				"&family=" + sFamilyName + "&gender=" + sGender +
				"&loc=" + sAddress + "&dob=" + sBirthdate + "&commit=Search", userpassword, "Accept", "application/atom+xml");

		try
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = loadXMLFromString(sResponse);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile("/feed/entry/link[@type='application/atom+xml']");
			Node nPeople = (Node) expr.evaluate(doc, XPathConstants.NODE);
			Element hPeople = (Element) nPeople;
			sUrl = baseURL + hPeople.getAttribute("href");
			sType = hPeople.getAttribute("type");
		}
		catch (Exception e)
		{
			log.error("DOM Exception", e.fillInStackTrace());
		}

		if (sType == null)
		{
			sResponse = "No PDMP Records found.";
		}
		else
		{
			sb.setLength(0);
			sResponse = PDMPGet(sb, model, sUrl, userpassword, "Accept", sType);
			sUrl = "";
			sType = "";

			try
			{
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = loadXMLFromString(sResponse);
				XPathFactory xPathfactory = XPathFactory.newInstance();
				XPath xpath = xPathfactory.newXPath();
				XPathExpression expr = xpath.compile("/feed/entry/link[@type='application/atom+xml']");
				Node nPeopleSRPP = (Node) expr.evaluate(doc, XPathConstants.NODE);
				Element hPeopleSRPP = (Element) nPeopleSRPP;
				sUrl = baseURL + hPeopleSRPP.getAttribute("href");
				sType = hPeopleSRPP.getAttribute("type");
			}
			catch (Exception e)
			{
				log.error("DOM Exception", e.fillInStackTrace());
			}

			sb.setLength(0);
			sResponse = PDMPGet(sb, model, sUrl, userpassword, "Accept", sType);
			sUrl = "";
			sType = "";

			try
			{
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = loadXMLFromString(sResponse);
				XPathFactory xPathfactory = XPathFactory.newInstance();
				XPath xpath = xPathfactory.newXPath();
				XPathExpression expr = xpath.compile("/feed/entry/link[@rel='report']");
				Node nPeopleSRPPReport = (Node) expr.evaluate(doc, XPathConstants.NODE);
				Element hPeopleSRPPReport = (Element) nPeopleSRPPReport;
				sUrl = baseURL + hPeopleSRPPReport.getAttribute("href");
				sType = hPeopleSRPPReport.getAttribute("type");
			}
			catch (Exception e)
			{
				log.error("DOM Exception", e.fillInStackTrace());
			}

			sb.setLength(0);
			sResponse = PDMPGet(sb, model, sUrl, userpassword, "Accept", sType);
			sUrl = "";
			sType = "";

			try
			{
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = loadXMLFromString(sResponse);
				XPathFactory xPathfactory = XPathFactory.newInstance();
				XPath xpath = xPathfactory.newXPath();
				XPathExpression expr = xpath.compile("/record/medicationOrder");
				NodeList nLPeopleMedication = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
				if (nLPeopleMedication.getLength() > 0)
				{
					sResponse = "";
					for (int countMeds = 0; countMeds < nLPeopleMedication.getLength(); countMeds++)
					{
						Node nMed = nLPeopleMedication.item(countMeds);

						// Prescriber Information
						expr = xpath.compile("orderInformation/prescriber/name");
						Node nPrescriberName = (Node) expr.evaluate(nMed, XPathConstants.NODE);
						Element ePrescriberName = (Element) nPrescriberName;
						NodeList nLPrescriberAttrs = ePrescriberName.getChildNodes();
						sResponse = sResponse + "<p><b>Prescriber:</b> ";
						for (int countPrescriberAttrs = 0; countPrescriberAttrs < nLPrescriberAttrs.getLength(); countPrescriberAttrs++)
						{
							sResponse = sResponse + nLPrescriberAttrs.item(countPrescriberAttrs).getTextContent();
						}
						sResponse = sResponse + "</p>\n";

						// Drug Information:
						expr = xpath.compile("medicationInformation/code");
						Node nMedicationCode = (Node) expr.evaluate(nMed, XPathConstants.NODE);
						Element eMedicationCode = (Element) nMedicationCode;
						String sDisplayName = eMedicationCode.getAttribute("displayName");
						sResponse = sResponse + "<p><b>Drug:</b> " + sDisplayName;
						sResponse = sResponse + "</p>\n";

						// Order Information
						expr = xpath.compile("orderInformation/orderedDateTime");
						Node nOrderedDateTime = (Node) expr.evaluate(nMed, XPathConstants.NODE);
						sResponse = sResponse + "<p><b>When written: </b>" + nOrderedDateTime.getTextContent() + "</p>\n";

						// Fullfillment Information
						// Prescription Number
						expr = xpath.compile("fulfillmentHistory/prescriptionNumber");
						Node nPrescriptionNumber = (Node) expr.evaluate(nMed, XPathConstants.NODE);
						sResponse = sResponse + "<table border='1'><tr><th>Rx Number</th><th>When Filled</th><th>Pharmacy</th><th>Pharmacist</th><th>Quantity</th><th>Status</th></tr>";
						sResponse = sResponse + "<tr><td>" + nPrescriptionNumber.getTextContent() + "</td>\n";

						// When Filled
						expr = xpath.compile("fulfillmentHistory/dispenseDate");
						Node nWhenFilled = (Node) expr.evaluate(nMed, XPathConstants.NODE);
						sResponse = sResponse + "<td>" + nWhenFilled.getTextContent() + "</td>\n";

						// Pharmacy
						expr = xpath.compile("fulfillmentHistory/pharmacy/name");
						Node nPharmacyName = (Node) expr.evaluate(nMed, XPathConstants.NODE);
						sResponse = sResponse + "<td>" + nPharmacyName.getTextContent() + "</td>\n";

						// Pharmacist  Name
						expr = xpath.compile("fulfillmentHistory/pharmacist/name/givenName");
						Node nPharmacistGivenName = (Node) expr.evaluate(nMed, XPathConstants.NODE);

						expr = xpath.compile("fulfillmentHistory/pharmacist/name/familyName");
						Node nPharmacistFamilyName = (Node) expr.evaluate(nMed, XPathConstants.NODE);

						sResponse = sResponse + "<td>" + nPharmacistGivenName.getTextContent() + " " + nPharmacistGivenName.getTextContent() + "</td>\n";

						// Quantity
						expr = xpath.compile("fulfillmentHistory/quantityDispensed");
						Node nQuantity = (Node) expr.evaluate(nMed, XPathConstants.NODE);
						Element eQuantity = (Element) nQuantity;
						String sQuantity = eQuantity.getAttribute("amount");
						sResponse = sResponse + "<td>" + sQuantity + "</td>\n";

						// Status
						expr = xpath.compile("fulfillmentHistory");
						Node nStatus = (Node) expr.evaluate(nMed, XPathConstants.NODE);
						Element eStatus = (Element) nStatus;
						String sStatus = eStatus.getAttribute("fillStatus");
						sResponse = sResponse + "<td>" + sStatus + "</td></tr></table>\n<HR/>";

					}
				}
			}
			catch (Exception e)
			{
				log.error("DOM Exception", e.fillInStackTrace());
			}
		}

		model.addAttribute("subsection", sResponse);

	}

	public String PDMPGet(StringBuilder sb, ModelMap model, String sURL, String userpassword, String hProp, String hPropVal)
	{
		HttpURLConnection connection = null;
		OutputStreamWriter wr = null;
		BufferedReader rd  = null;
		String line = null;
		String output = null;
		URL serverAddress = null;

		try {
			serverAddress = new URL(sURL);

			//set up out communications stuff
			connection = null;

			//Set up the initial connection
			connection = (HttpURLConnection)serverAddress.openConnection();
			connection.setRequestMethod("GET");

			byte[] authEncBytes = Base64.encodeBase64(userpassword.getBytes());
			String encodedAuthorization = new String(authEncBytes);
			connection.setRequestProperty("Authorization", "Basic "+
					encodedAuthorization);
			if (hProp!=null) {
				connection.addRequestProperty(hProp, hPropVal);
			}
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);
			connection.connect();

			rd  = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			while ((line = rd.readLine()) != null)
			{
				sb.append(line + '\n');
			}

			output = sb.toString();

			return output;
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
			return "Malformed URL Error! " + sURL;
		}
		catch (ProtocolException e) {
			e.printStackTrace();
			return "Protocol Error! " + sURL;
		}
		catch (IOException e) {
			e.printStackTrace();
			log.error("IOException", e.fillInStackTrace());
			return "IO Error! " + sURL;
		}
		finally	{
			//close the connection, set all objects to null
			connection.disconnect();
			rd = null;
			wr = null;
			connection = null;
		}
	}

	public static int nthIndexOf(String source, String sought, int n) {
	    int index = source.indexOf(sought);
	    if (index == -1) return -1;

	    for (int i = 1; i < n; i++) {
	        index = source.indexOf(sought, index + 1);
	        if (index == -1) return -1;
	    }
	    return index;
	}

	public static Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        return builder.parse(is);
    }

	protected String XParseDoc(String sResponse, String expression)
	{
		try
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = loadXMLFromString(sResponse);
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile(expression);
			Node nNode = (Node) expr.evaluate(doc, XPathConstants.NODE);
			Element hNode = (Element) nNode;
			sWorkerUrl = sPDMPUrl + hNode.getAttribute("href");
			sWorkerType = hNode.getAttribute("type");
		}
		catch (Exception e)
		{
			log.error("DOM Exception", e.fillInStackTrace());
		}
		return sWorkerType;
	}

}
