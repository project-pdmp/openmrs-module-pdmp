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
package org.openmrs.module.pdmp_query.impl;

import org.openmrs.api.APIException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.pdmp_query.Config;
import org.openmrs.module.pdmp_query.db.ServiceDAO;


public class ConfigServiceImpl extends BaseOpenmrsService implements org.openmrs.module.pdmp_query.ConfigService {

    ServiceDAO dao = null;

    public void setServiceDao(ServiceDAO dao) {
        this.dao = dao;
    }

    public Config getConfig() throws APIException {
        return dao.getConfig();
    }

    public void saveConfig(Config config) throws APIException {
        dao.saveConfig(config);
    }

}
