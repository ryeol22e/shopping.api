package com.project.shopping.zconfig.querydsl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import com.project.shopping.zconfig.conditional.UseMysql;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Conditional(UseMysql.class)
@Configuration
public class Mysql {
    @PersistenceContext(unitName = "mysqlEntityManager")
    private EntityManager mysqlEntityManager;

    @Bean(name = "mysqlFactory")
    JPAQueryFactory mysqlFactory() {
        return new JPAQueryFactory(mysqlEntityManager);
    }

}
