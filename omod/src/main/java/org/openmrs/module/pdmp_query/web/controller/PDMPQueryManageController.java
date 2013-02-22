/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.pdmp_query.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.hibernate.FlushMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.QueryStatistics;
import org.hibernate.stat.Statistics;
import org.hibernate.cfg.Configuration;
import org.hibernate.util.ConfigHelper;
import org.openmrs.api.context.Context;
import org.openmrs.util.OpenmrsUtil;
/**
 * The main controller.
 */
@Controller
public class  PDMPQueryManageController {
	
	protected final Log log = LogFactory.getLog(getClass());
//	private static SessionFactory factory;
	   
	@RequestMapping(value = "/module/pdmp_query/manage.form", method = RequestMethod.GET)
	public void manage(ModelMap model) {
		model.addAttribute("user", Context.getAuthenticatedUser());
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

		org.hibernate.SessionFactory sessionFactory = config.configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		
		Query pdmpQuery = session.createSQLQuery("select pdmp_url, pdmp_uid from pdmp_import")
				.addScalar("pdmp_url", Hibernate.STRING)
				.addScalar("pdmp_uid", Hibernate.STRING);
		Object [] pdmpImport = (Object []) pdmpQuery.uniqueResult(); 
		model.addAttribute("pdmpURL", pdmpImport[0]);
		model.addAttribute("pdmpUID", pdmpImport[1]);
		session.close();
	}
	
	@RequestMapping(value = "/module/pdmp_query/manage.form", method = RequestMethod.POST)
	public ModelAndView doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // Postprocess request here.
		String pdmpURL = request.getParameter("pdmpURL");
		String pdmpUIR = request.getParameter("pdmpUID");
		String pdmpPWD = request.getParameter("pdmpPWD");
	    update(pdmpURL, pdmpUIR, pdmpPWD);
	    // And forward to manage.form to display results.
	    return new ModelAndView(new RedirectView("manage.form", true));
	}
	
	public int update(String sPDMPUrl, String sPDMPUID, String sPDMPPwd) {
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

		org.hibernate.SessionFactory sessionFactory = config.configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		int result = 0;
		try {
			tx = session.beginTransaction();
			Query  pdmpQuery = session.createSQLQuery("update openmrs.pdmp_import set pdmp_url = :pdmpUrl, "
					+ "pdmp_uid = :pdmpUID, "
					+ "pdmp_pwd = :pdmpPWD");
					pdmpQuery.setParameter("pdmpUrl", sPDMPUrl);
					pdmpQuery.setParameter("pdmpUID", sPDMPUID);
					pdmpQuery.setParameter("pdmpPWD", sPDMPPwd);
			result = pdmpQuery.executeUpdate();
			tx.commit();
		}
		catch (Exception e) {
			result = -1;
			log.error("PDMP Error writing to db");
			tx.rollback();
		}
		finally {
			session.close();
		}
		return result;
	}
}
