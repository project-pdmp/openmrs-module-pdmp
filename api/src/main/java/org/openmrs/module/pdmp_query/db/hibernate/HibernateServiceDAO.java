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
package org.openmrs.module.pdmp_query.db.hibernate;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Expression;
import org.openmrs.api.db.DAOException;

import org.openmrs.module.pdmp_query.Config;


public class HibernateServiceDAO implements org.openmrs.module.pdmp_query.db.ServiceDAO {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory factory) {
        this.sessionFactory = factory;
    }

    public Config getConfig() throws DAOException {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Config.class);

        // there's only one row in the db, but Hibernate requires a PK so...
        criteria.add(Expression.eq("id", 1));
        return (Config)criteria.uniqueResult();
    }

    public void saveConfig(Config config) throws DAOException {
        sessionFactory.getCurrentSession().update(config);
    }

}
