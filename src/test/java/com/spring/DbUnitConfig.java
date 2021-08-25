package com.spring;

import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;

public class DbUnitConfig extends DataSourceBasedDBTestCase
{
        @Override
        protected DataSource getDataSource() {
            JdbcDataSource dataSource = new JdbcDataSource();
            dataSource.setURL("jdbc:h2:mem:default;DB_CLOSE_DELAY=-1;init=runscript from 'classpath:schema.sql'");
            dataSource.setUser("sa");
            dataSource.setPassword("sa");
            return dataSource;
        }

        @Override
        protected IDataSet getDataSet() throws Exception {
            return new FlatXmlDataSetBuilder().build(getClass().getClassLoader()
                    .getResourceAsStream("data.xml"));
        }
        @Override
        protected DatabaseOperation getSetUpOperation() {
            return DatabaseOperation.REFRESH;
        }

        @Override
        protected DatabaseOperation getTearDownOperation() {
            return DatabaseOperation.DELETE_ALL;
        }
}
