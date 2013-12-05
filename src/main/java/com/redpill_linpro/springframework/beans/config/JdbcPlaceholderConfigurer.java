/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redpill_linpro.springframework.beans.factory.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;


import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * Subclass of PropertyPlaceholderConfigurer that supports loading properties 
 * from a JDBC datasource.
 *
 * <p>Tries to resolve placeholders as keys using the provided SQL select statement.
 * </p>
 *
 * <p>The default SQL select statment is:
 * <code>SELECT value FROM properties WHERE key = ?</code>
 * </p>
 *
 * @author Pontus Ullren
 * @since 16.02.2004
 * @see #setDataSource
 * @see #setSelectStatement
 */
public class JdbcPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements InitializingBean {

	private String selectStatement = "SELECT value FROM properties WHERE key = ?";

	private JdbcTemplate jdbcTemplate;

	/**
	 * Set the data source to be used to look up properties.
	 */
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * Sets the SQL statement used to resolve the 
	 * placeholders. Default is "SELECT value FROM properties WHERE key = ?"
	 */
	public void setSelectStatement(String statement) {
		this.selectStatement = statement;
	}


	/**
	 * This implementation eagerly fetches the Preferences instances
	 * for the required system and user tree nodes.
	 */
	@Override
	public void afterPropertiesSet() {
		if ( this.jdbcTemplate == null) {
			throw new IllegalArgumentException("dataSource is required");
                }
	}

	/**
	 * This implementation tries to resolve placeholders first by using a SQL statement,
         * then in the passed-in properties.
	 */
	@Override
	protected String resolvePlaceholder(String placeholder, Properties props) {
                String value = null;
		if ( this.jdbcTemplate != null ) {
			try {
				value = this.jdbcTemplate.queryForObject(
					this.selectStatement,
					new Object[]{placeholder}, String.class);
			} catch (IncorrectResultSizeDataAccessException e ) {
				// Ignored 
			}
		}
		if (value == null) {
			value = props.getProperty(placeholder);
		}
		return value;
	}
}
