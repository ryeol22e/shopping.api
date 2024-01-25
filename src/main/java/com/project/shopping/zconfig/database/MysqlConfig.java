package com.project.shopping.zconfig.database;

import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import lombok.RequiredArgsConstructor;

@Deprecated
@Configuration
@EnableJpaRepositories(
		basePackages = {"com.project.shopping.*.repository"},
		entityManagerFactoryRef = "mysqlEntityManagerFactory",
		transactionManagerRef = "mysqlTransactionManager")
@RequiredArgsConstructor
public class MysqlConfig {
	private final JpaProperties jpaProperties;
	private final HibernateProperties hibernateProperties;

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource-mysql.hikari")
	DataSource mysqlDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	@Primary
	LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		Map<String, Object> propertiesMap = hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), new HibernateSettings());

		return builder.dataSource(mysqlDataSource())
				.packages("com.project.shopping.*.dto")
				.persistenceUnit("mysqlEntityManager")
				.properties(propertiesMap)
				.build();
	}

	@Bean
	@Primary
	PlatformTransactionManager mysqlTransactionManager(@Qualifier("mysqlEntityManagerFactory") LocalContainerEntityManagerFactoryBean factoryBean) {
		return new JpaTransactionManager(factoryBean.getObject());
	}

}