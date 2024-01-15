package com.project.shopping.zconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Configuration
public class QueryDslConfig {

	@PersistenceContext(unitName = "mysqlEntityManager")
	private EntityManager mysqlEntityManager;

	@Bean(name = "mysqlFactory")
	JPAQueryFactory mysqlFactory() {
		return new JPAQueryFactory(mysqlEntityManager);
	}
}
