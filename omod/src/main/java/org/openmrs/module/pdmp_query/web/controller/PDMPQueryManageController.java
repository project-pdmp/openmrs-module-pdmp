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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.api.context.Context;
import org.openmrs.module.pdmp_query.Config;
import org.openmrs.module.pdmp_query.ConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


/**
 * The main controller.
 */
@Controller
public class  PDMPQueryManageController {
    @RequestMapping(value = "/module/pdmp_query/manage.form", method = RequestMethod.GET)
    public void manage(ModelMap model) {
        Config c = Context.getService(ConfigService.class).getConfig();
        model.addAttribute("user", Context.getAuthenticatedUser());
        model.addAttribute("pdmpURL", c.getUrl());
        model.addAttribute("pdmpUID", c.getUser());
    }

    @RequestMapping(value = "/module/pdmp_query/manage.form", method = RequestMethod.POST)
    public ModelAndView doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        // Postprocess request here.
        String pdmpURL = request.getParameter("pdmpURL");
        String pdmpUIR = request.getParameter("pdmpUID");
        String pdmpPWD = request.getParameter("pdmpPWD");
        Context.getService(ConfigService.class).saveConfig(new Config(pdmpURL, pdmpUIR, pdmpPWD));

        // And forward to manage.form to display results.
        return new ModelAndView(new RedirectView("manage.form", true));
    }
}
