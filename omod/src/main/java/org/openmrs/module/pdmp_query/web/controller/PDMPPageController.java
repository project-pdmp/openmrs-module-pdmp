package org.openmrs.module.pdmp_query.web.controller;


import java.io.BufferedReader;
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
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.util.ConfigHelper;
import org.openmrs.api.context.Context;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The main controller.
 */
@Controller
public class PDMPPageController {
	
	protected final Log log = LogFactory.getLog(getClass());
	protected String sPDMPUrl;
	protected String sPDMPUserID;
	protected String sPDMPPassword;
	
	@RequestMapping(value = "/module/pdmp_query/pdmp", method = RequestMethod.GET)
	public void manage(ModelMap model, @RequestParam("patientId") Integer patientId) {
		fetchPDMPProperties();
		String userpassword = sPDMPUserID + ":" + sPDMPPassword;
		String baseURL = sPDMPUrl; 
		String sNoRecordsFound = "No PDMP Records Found";
		String sUrl = null;
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
/*		address methods
			.getCountry():  returns the country as a string
			.getStateProvince(): returns the state or province as a string
			.getCountyDistrict(): returns the county or district as a string
			.getCityVillage(): returns the city or village as a string
			.getNeighborhoodCell(): returns the neighborhood cell as a string
			.getAddress1(): returns the street and number as a string
*/

		sb = new StringBuilder();
		sUrl = PDMPGet(sb, model, baseURL + "search?utf8=%E2%9C%93&given=" + sGivenName + 
				"&family=" + sFamilyName + "&gender=" + sGender + 
				"&loc=" + sAddress + "&dob=" + sBirthdate + "&commit=Search", userpassword, null, null);
		sb.append(sUrl);


 		int indexURL = sb.lastIndexOf("href=");
 		if (indexURL > 0) {
 			String sBeginUrl = sb.substring(indexURL + 6);
 			String sSecondUrl = sBeginUrl.substring(0, sBeginUrl.indexOf("\""));

			// second call to server
 			sb.setLength(0);
 			sUrl = PDMPGet(sb, model, baseURL + sSecondUrl, userpassword, "Accept", "application/atom+xml");

			// end second call

 			sb.setLength(0);
 			sb.append(sUrl);

 			indexURL = sb.lastIndexOf("href");
 	 		if (indexURL > 0)
 	 		{
 	 			sBeginUrl = sb.substring(indexURL + 6);
 	 			String sThirdUrl = sBeginUrl.substring(0, sBeginUrl.indexOf("\""));
 	 			// third call to server
 	 			sb.setLength(0);
 	 			sUrl = PDMPGet(sb, model, baseURL + sThirdUrl, userpassword, "Accept", "application/atom+xml");
 	 			
 	 			sb.setLength(0);
 	 			sb.append(sUrl);
//	 			model.addAttribute("subsection", sUrl.toString());

// 	 			indexURL = sb.lastIndexOf("href=\"/prescriptions");
 	 			int ndex = 1;
 	 			int indexPrescriptions = 1;
 	 			String sPrescriptions = sb.toString();
 	 			String sURLCollection = "";
 	 			while (indexPrescriptions >= 0)
 	 			{
 	 	 			indexPrescriptions = nthIndexOf(sPrescriptions, "href=\"/prescriptions", ndex);
 	 	 			ndex = ndex + 2;
 	 				if (indexPrescriptions > 0) {
 	 	 				sBeginUrl = sPrescriptions.substring(indexPrescriptions + 6);
 	 	 				String sFourthUrl = sBeginUrl.substring(0, sBeginUrl.indexOf("\""));
 	 	 				// third call to server
 	 	 				sb.setLength(0);
 	 	 				sUrl = PDMPGet(sb, model, baseURL + sFourthUrl, userpassword, null, null);

 	 	 				sb.setLength(0);
 	 	 				sb.append(sUrl);
 	 	 				indexURL = sb.lastIndexOf("<div class=\"container\">");
 	 	 	 			if (indexURL > 0) {
 	 	 	 				sBeginUrl = sb.substring(indexURL + 23);
 	 	 	 				indexURL = sBeginUrl.lastIndexOf("</p>");
 	 	 	 				sUrl = sBeginUrl.substring(0, indexURL+4);
 	 	 	 				sURLCollection = sURLCollection + sUrl;

// 	 	 	 				System.out.println(sUrl.toString()); 
// 	 	 	 				model.addAttribute("subsection", sUrl.toString());
// 	 	 	 			} else {
// 	 	 	 				System.out.println(sNoRecordsFound);
// 	 	 	 				model.addAttribute("subsection", sNoRecordsFound + " 1 ");
 	 	 	 			}

 	 	 			} else {
 	 					System.out.println(sNoRecordsFound);
 	 					model.addAttribute("subsection", sNoRecordsFound);
 	 	 			}
 	 			}
 				model.addAttribute("subsection", sURLCollection);

 			} else {
 	 			System.out.println(sNoRecordsFound);
 	 			model.addAttribute("subsection", sNoRecordsFound);
 	 		}

 		} else {
 			System.out.println(sNoRecordsFound);
 			model.addAttribute("subsection", sNoRecordsFound);
 		}

		sb = null;

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
	
	public void fetchPDMPProperties()
	{
		Configuration config = new Configuration();
		
		// load in the default hibernate properties
		try {
			InputStream propertyStream = ConfigHelper.getResourceAsStream("/hibernate.default.properties");
			Properties props = new Properties();
			OpenmrsUtil.loadProperties(props, propertyStream);
			propertyStream.close();
			
			// Only load in the default properties if they don't exist
			config.mergeProperties(props);
		}
		catch (IOException e) {
			log.fatal("Unable to load default hibernate properties", e);
		}

		SessionFactory sessionFactory = config.configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		
		Query pdmpQuery = session.createSQLQuery("select pdmp_url, pdmp_uid, pdmp_pwd from pdmp_import")
				.addScalar("pdmp_url", Hibernate.STRING)
				.addScalar("pdmp_uid", Hibernate.STRING)
				.addScalar("pdmp_pwd", Hibernate.STRING);
		Object [] pdmpImport = (Object []) pdmpQuery.uniqueResult(); 
		sPDMPUrl = (String) pdmpImport[0];
		sPDMPUserID = (String) pdmpImport[1];
		sPDMPPassword = (String) pdmpImport[2];
		session.close();
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
}