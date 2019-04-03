package org.daijie.workflow.activiti;

import javax.sql.DataSource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.daijie.jdbc.BaseMultipleDataSourceConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableConfigurationProperties(ActivitiProperties.class)
@Import(BaseMultipleDataSourceConfiguration.class)
public class ActivitiAutoConfigure {
	
	private ActivitiProperties properties;
	
	public ActivitiAutoConfigure(ActivitiProperties properties) {
		this.properties = properties;
	}
	
	@Bean
	@ConditionalOnMissingBean
	public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
	
	@Bean
	@ConditionalOnMissingBean
	public SpringProcessEngineConfiguration processEngineConfiguration(DataSource dataSource, 
			PlatformTransactionManager transactionManager) {
		SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
		processEngineConfiguration.setDataSource(dataSource);
		processEngineConfiguration.setTransactionManager(transactionManager);
		processEngineConfiguration.setDatabaseSchemaUpdate("true");
		processEngineConfiguration.setDeploymentResources(this.properties.getDeploymentResources());
		return processEngineConfiguration;
	}
	
	@Bean
	@ConditionalOnMissingBean
	public ProcessEngineFactoryBean processEngineFactory(SpringProcessEngineConfiguration processEngineConfiguration) {
		ProcessEngineFactoryBean processEngineFactory = new ProcessEngineFactoryBean();
		processEngineFactory.setProcessEngineConfiguration(processEngineConfiguration);
		return processEngineFactory;
	}
	
	@Bean("repositoryService")
	public RepositoryService repositoryService(ProcessEngineFactoryBean processEngineFactory) throws Exception {
		return processEngineFactory.getObject().getRepositoryService();
	}
	
	@Bean("historyService")
	public HistoryService historyService(ProcessEngineFactoryBean processEngineFactory) throws Exception {
		return processEngineFactory.getObject().getHistoryService();
	}
	
	@Bean("runtimeService")
	public RuntimeService runtimeService(ProcessEngineFactoryBean processEngineFactory) throws Exception {
		return processEngineFactory.getObject().getRuntimeService();
	}
	
	@Bean("taskService")
	public TaskService taskService(ProcessEngineFactoryBean processEngineFactory) throws Exception {
		return processEngineFactory.getObject().getTaskService();
	}
	
	@Bean("managementService")
	public ManagementService managementService(ProcessEngineFactoryBean processEngineFactory) throws Exception {
		return processEngineFactory.getObject().getManagementService();
	}

}
