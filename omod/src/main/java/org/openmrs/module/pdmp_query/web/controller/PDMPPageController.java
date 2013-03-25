package org.openmrs.module.pdmp_query.web.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.codec.binary.Base64;
import org.ncpdp.schema.script.HistoryDispensedMedicationType;
import org.ncpdp.schema.script.NameType;
import org.ncpdp.schema.script.OptionalPharmacyType;
import org.ncpdp.schema.script.QuantityType;
import org.ncpdp.schema.script.RxHistoryResponse;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.pdmp_query.Config;
import org.openmrs.module.pdmp_query.ConfigService;
import org.openmrs.module.pdmp_query.Prescription;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


/**
 * The main controller.
 */
@Controller
public class PDMPPageController {

    @RequestMapping(value = "/module/pdmp_query/pdmp", method = RequestMethod.GET)
    public void manage(ModelMap model, @RequestParam("patientId") Integer patientId) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException, ParseException, JAXBException {

	XPath xpath = XPathFactory.newInstance().newXPath();
	xpath.setNamespaceContext(new NamespaceContext() {
	    public String getNamespaceURI(String prefix) {
		if (prefix.equals("script")) {
		    return "http://www.ncpdp.org/schema/SCRIPT";
		}
		return XMLConstants.NULL_NS_URI;
	    }

	    public String getPrefix(String namespaceURI) {
		if (namespaceURI.equals("http://www.ncpdp.org/schema/SCRIPT")) {
		    return "script";
		}
		return null;
	    }

	    public Iterator<String> getPrefixes(String namespaceURI) {
		List<String> list = new ArrayList<String>();
		if (namespaceURI.equals("http://www.ncpdp.org/schema/SCRIPT")) {
		    list.add("script");
		}
		return list.iterator();
	    }
	});

        Config c = Context.getService(ConfigService.class).getConfig();
        String userpassword = c.getUser() + ":" + c.getPassword();
        String baseURL = c.getUrl();
        String sUrl = null;
        String sType = null;
        Patient patient = Context.getPatientService().getPatient(patientId);

        String sGivenName = "";
        String sFamilyName = "";
        String sGender = "";
        String sBirthdate = "";
        String sAddress = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
        DateFormat isoDate = new SimpleDateFormat("yyyy-MM-dd");

        sGivenName = patient.getGivenName();
        sFamilyName = patient.getFamilyName();
        if (patient.getGender().equalsIgnoreCase("F")) {
            sGender = "female";
        } else if (patient.getGender().equalsIgnoreCase("M")) {
            sGender = "male";
        }
        sBirthdate = dateFormat.format(patient.getBirthdate());
        sAddress = patient.getPersonAddress().getCityVillage();

        Document doc = PDMPGet(baseURL + "search?given=" + sGivenName +
                               "&family=" + sFamilyName + "&gender=" + sGender +
                               "&loc=" + sAddress + "&dob=" + sBirthdate, userpassword, "Accept", "application/atom+xml", false);

        Element hPeople = (Element)xpath.evaluate("/feed/entry/link[@type='application/atom+xml']", doc, XPathConstants.NODE);
        if (hPeople != null) {
            sUrl = baseURL + hPeople.getAttribute("href");
            sType = hPeople.getAttribute("type");
        }

        if (sType != null) {
            doc = PDMPGet(sUrl, userpassword, "Accept", sType, false);
            Element hPeopleSRPP = (Element)xpath.evaluate("/feed/entry/link[@type='application/atom+xml']", doc, XPathConstants.NODE);
            sUrl = baseURL + hPeopleSRPP.getAttribute("href");
            sType = hPeopleSRPP.getAttribute("type");

            doc = PDMPGet(sUrl, userpassword, "Accept", sType, false);
            Element hPeopleSRPPReport = (Element)xpath.evaluate("/feed/entry/link[@rel=\"report\" and @type=\"application/vnd.ncpdp.script.10+xml\"]", doc, XPathConstants.NODE);
            sUrl = baseURL + hPeopleSRPPReport.getAttribute("href");
            sType = hPeopleSRPPReport.getAttribute("type");

            doc = PDMPGet(sUrl, userpassword, "Accept", sType, true);
            Element response = (Element)xpath.evaluate("/script:Message/script:Body/script:RxHistoryResponse", doc, XPathConstants.NODE);

            Unmarshaller u = JAXBContext.newInstance(RxHistoryResponse.class).createUnmarshaller();
            RxHistoryResponse rxh = (RxHistoryResponse)u.unmarshal(response);

            List<Prescription> meds = new ArrayList<Prescription>();
            model.addAttribute("prescriptions", meds);

            for (Iterator<HistoryDispensedMedicationType> i = rxh.getMedicationDispensed().iterator(); i.hasNext();) {
                HistoryDispensedMedicationType med = i.next();
                Prescription drug = new Prescription();
                drug.setDrug(med.getDrugDescription());
                drug.setWrittenOn(isoDate.parse(med.getWrittenDate().getDateTime().toString()));
                List<QuantityType> quantityFilled = med.getQuantity();
                if (quantityFilled.size() > 0) {
                    drug.setQuantityFilled(quantityFilled.get(0).getValue());
                }
                drug.setFilledOn(isoDate.parse(med.getDeliveredOnDate().getDateTime().toString()));
                drug.setPrescriber(nameToString(med.getPrescriber().getName()));
                OptionalPharmacyType store = med.getPharmacy();
                drug.setPharmacy(store.getStoreName());
                drug.setPharmacist(nameToString(store.getPharmacist()));
                // FIXME - drug.setRxNumber
                // FIXME - drug.setStatus
                meds.add(drug);
            }
        }
    }


    private String nameToString(NameType name) {
        String n = name.getFirstName() + " " + name.getLastName();
        if (null != name.getPrefix()) {
            n = name.getPrefix() + " " + n;
        }
        if (null != name.getSuffix()) {
            n = n + " " + name.getPrefix();
        }
        return n;
    }


    private Document PDMPGet(String sURL, String userpassword, String hProp, String hPropVal, boolean namespaceAware) throws IOException, ParserConfigurationException, SAXException {
        HttpURLConnection connection = null;
        URL serverAddress = new URL(sURL);

        try {
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
            factory.setNamespaceAware(namespaceAware);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(connection.getInputStream());
        }
        finally	{
            connection.disconnect();
        }
    }
}
