package com.project.shopping.zconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Configuration
public class QueryDslConfig {

	@PersistenceContext(unitName = "mariaEntityManager")
	private EntityManager mariaEntityManager;

	@PersistenceContext(unitName = "mysqlEntityManager")
	private EntityManager mysqlEntityManager;

	@Primary
	@Bean(name = "mariadbFactory")
	JPAQueryFactory mariadbFactory() {
		return new JPAQueryFactory(mariaEntityManager);
	}

	@Bean(name = "mysqlFactory")
	JPAQueryFactory mysqlFactory() {
		return new JPAQueryFactory(mysqlEntityManager);
	}
}
