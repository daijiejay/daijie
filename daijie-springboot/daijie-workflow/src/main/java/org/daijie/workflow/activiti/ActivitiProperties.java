package org.daijie.workflow.activiti;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StringUtils;

@ConfigurationProperties(prefix = "spring.activiti")
public class ActivitiProperties {
	
	private static final String BPMN_RESOURCE_PATTERN = "classpath:/processes/*.bpmn";

	private List<Resource> deploymentResources = new ArrayList<Resource>();
	
	private PathMatchingResourcePatternResolver resolover = new PathMatchingResourcePatternResolver();

	public Resource[] getDeploymentResources() {
		if (deploymentResources.size() == 0) {
			init();
		}
		return deploymentResources.toArray(new Resource[deploymentResources.size()]); 
	}

	public void setDeploymentResources(String deploymentResources) throws IOException {
		String[] resources = StringUtils.tokenizeToStringArray(deploymentResources, 
				ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
		for (String resource : resources) {
			this.deploymentResources.addAll(Arrays.asList(resolover.getResources(resource)));
		}
	}
	
	public void init(){
		try {
			this.deploymentResources.addAll(Arrays.asList(resolover.getResources(BPMN_RESOURCE_PATTERN)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
