package com.project.shopping.aop;

import java.util.Collections;
import javax.sql.DataSource;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class Transaction {
	// private TransactionManager transactionManager;

	// Transaction(DataSource dataSource) {
	// 	DataSourceTransactionManager manager = new DataSourceTransactionManager();
	// 	manager.setDataSource(dataSource);
	// 	transactionManager = manager;
	// }

	// @Bean
	// TransactionInterceptor transactionAdvice() {
	// 	MatchAlwaysTransactionAttributeSource source = new MatchAlwaysTransactionAttributeSource();
	// 	RuleBasedTransactionAttribute attribute = new RuleBasedTransactionAttribute();

	// 	attribute.setName("set*");
	// 	attribute.setRollbackRules(
	// 			Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
	// 	source.setTransactionAttribute(attribute);

	// 	return new TransactionInterceptor(transactionManager, source);
	// }

	// @Bean
	// Advisor transactionAdviceAdvisor() {
	// 	AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
	// 	pointcut.setExpression("execution(* com.project.shopping.*.service.*Service.*(..))");

	// 	return new DefaultPointcutAdvisor(pointcut, transactionAdvice());
	// }
}
