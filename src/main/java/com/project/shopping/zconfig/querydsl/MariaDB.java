package com.project.shopping.zconfig.querydsl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Configuration
public class MariaDB {
	@PersistenceContext(unitName = "mariaEntityManager")
	private EntityManager mariaEntityManager;

	@Primary
	@Bean(name = "mariadbFactory")
	JPAQueryFactory mariadbFactory() {
		return new JPAQueryFactory(mariaEntityManager);
	}
}
