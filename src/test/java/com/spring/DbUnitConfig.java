package com.spring;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
