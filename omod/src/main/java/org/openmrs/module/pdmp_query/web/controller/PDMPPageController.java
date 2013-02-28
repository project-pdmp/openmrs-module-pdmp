package org.openmrs.module.pdmp_query.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
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
import org.xml.sax.SAXException;

import org.openmrs.module.pdmp_query.Config;
import org.openmrs.module.pdmp_query.ConfigService;
import org.openmrs.module.pdmp_query.Prescription;


/**
 * The main controller.
 */
@Controller
public class PDMPPageController {

	protected final Log log = LogFactory.getLog(getClass());

	@RequestMapping(value = "/module/pdmp_query/pdmp", method = RequestMethod.GET)
	public void manage(ModelMap model, @RequestParam("patientId") Integer patientId) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
            XPath xpath = XPathFactory.newInstance().newXPath();
            Config c = Context.getService(ConfigService.class).getConfig();
            String userpassword = c.getUser() + ":" + c.getPassword();
            String baseURL = c.getUrl();
		String sUrl = null;
		String sType = null;
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

		Document doc = PDMPGet(baseURL + "search?given=" + sGivenName +
				"&family=" + sFamilyName + "&gender=" + sGender +
				"&loc=" + sAddress + "&dob=" + sBirthdate, userpassword, "Accept", "application/atom+xml");

                Element hPeople = (Element)xpath.evaluate("/feed/entry/link[@type='application/atom+xml']", doc, XPathConstants.NODE);
                if (hPeople != null) {
                    sUrl = baseURL + hPeople.getAttribute("href");
                    sType = hPeople.getAttribute("type");
                }

		if (sType != null)
                {
			doc = PDMPGet(sUrl, userpassword, "Accept", sType);
                        Element hPeopleSRPP = (Element)xpath.evaluate("/feed/entry/link[@type='application/atom+xml']", doc, XPathConstants.NODE);
                        sUrl = baseURL + hPeopleSRPP.getAttribute("href");
                        sType = hPeopleSRPP.getAttribute("type");

			doc = PDMPGet(sUrl, userpassword, "Accept", sType);
                        Element hPeopleSRPPReport = (Element)xpath.evaluate("/feed/entry/link[@rel='report']", doc, XPathConstants.NODE);
                        sUrl = baseURL + hPeopleSRPPReport.getAttribute("href");
                        sType = hPeopleSRPPReport.getAttribute("type");

			doc = PDMPGet(sUrl, userpassword, "Accept", sType);
			{
				NodeList nLPeopleMedication = (NodeList)xpath.evaluate("/record/medicationOrder", doc, XPathConstants.NODESET);
				if (nLPeopleMedication.getLength() > 0)
				{
                                    List meds = new ArrayList();
                                    model.addAttribute("meds", meds);

					for (int countMeds = 0; countMeds < nLPeopleMedication.getLength(); countMeds++)
					{
                                            Prescription drug = new Prescription();

						Node nMed = nLPeopleMedication.item(countMeds);

						// Prescriber Information
						Element ePrescriberName = (Element)xpath.evaluate("orderInformation/prescriber/name", nMed, XPathConstants.NODE);
						NodeList nLPrescriberAttrs = ePrescriberName.getChildNodes();
                                                String prescriber = "";
						for (int countPrescriberAttrs = 0; countPrescriberAttrs < nLPrescriberAttrs.getLength(); countPrescriberAttrs++)
						{
                                                        prescriber += nLPrescriberAttrs.item(countPrescriberAttrs).getTextContent();
						}
                                                drug.setPrescriber(prescriber);

						// Drug Information:
						Element eMedicationCode = (Element)xpath.evaluate("medicationInformation/code", nMed, XPathConstants.NODE);
                                                drug.setDrug(eMedicationCode.getAttribute("displayName"));

						// Order Information
						Node nOrderedDateTime = (Node)xpath.evaluate("orderInformation/orderedDateTime", nMed, XPathConstants.NODE);
                                                drug.setWrittenOn(nOrderedDateTime.getTextContent());

						// Fullfillment Information
						// Prescription Number
						Node nPrescriptionNumber = (Node)xpath.evaluate("fulfillmentHistory/prescriptionNumber", nMed, XPathConstants.NODE);
                                                drug.setRxNumber(nPrescriptionNumber.getTextContent());

						// When Filled
						Node nWhenFilled = (Node)xpath.evaluate("fulfillmentHistory/dispenseDate", nMed, XPathConstants.NODE);
                                                drug.setFilledOn(nWhenFilled.getTextContent());

						// Pharmacy
						Node nPharmacyName = (Node)xpath.evaluate("fulfillmentHistory/pharmacy/name", nMed, XPathConstants.NODE);
                                                drug.setPharmacy(nPharmacyName.getTextContent());

						// Pharmacist  Name
						Node nPharmacistGivenName = (Node)xpath.evaluate("fulfillmentHistory/pharmacist/name/givenName", nMed, XPathConstants.NODE);
						Node nPharmacistFamilyName = (Node)xpath.evaluate("fulfillmentHistory/pharmacist/name/familyName", nMed, XPathConstants.NODE);

                                                drug.setPharmacist(nPharmacistGivenName.getTextContent() + " " + nPharmacistGivenName.getTextContent());

						// Quantity
						Element eQuantity = (Element)xpath.evaluate("fulfillmentHistory/quantityDispensed", nMed, XPathConstants.NODE);
                                                drug.setQuantityFilled(eQuantity.getAttribute("amount"));

						// Status
						Element eStatus = (Element)xpath.evaluate("fulfillmentHistory", nMed, XPathConstants.NODE);
                                                drug.setStatus(eStatus.getAttribute("fillStatus"));

                                                meds.add(drug);
					}
				}
			}
		}
	}

    private Document PDMPGet(String sURL, String userpassword, String hProp, String hPropVal) throws IOException, ParserConfigurationException, SAXException
	{
		HttpURLConnection connection = null;
		URL serverAddress = null;

		try {
			serverAddress = new URL(sURL);

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

                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        return builder.parse(connection.getInputStream());
		}
		finally	{
			connection.disconnect();
		}
	}
}
