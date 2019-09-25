package org.daijie.jdbc.generator.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 生成器基本配置
 * @author daijie
 * @since 2019/9/16
 */
public class GeneratorConfiguration {

    private List<DatasourceConfiguration> datasourceConfigurations;

    private Map<Class, FileConfiguration> fileConfigurations;

    public GeneratorConfiguration() {
        this.datasourceConfigurations = Lists.newArrayList();
        this.fileConfigurations = Maps.newHashMap();
    }

    public void addDatasource(Map<String, Object> properties) {
        String driverClassName = properties.get("driverClassName").toString();
        String url = properties.get("url").toString();
        String user = properties.get("username").toString();
        String password = properties.get("password").toString();
        this.addDatasource(driverClassName, url, user, password);
    }

    public void addDatasource(String driverClassName, String url, String user, String password) {
        this.datasourceConfigurations.add(new DatasourceConfiguration(driverClassName, url, user, password));
    }

    public List<DatasourceConfiguration> getDatasourceConfigurations() {
        return datasourceConfigurations;
    }

    public void addFileConfiguration(FileConfiguration configuration) {
        this.fileConfigurations.put(configuration.getClass(), configuration);
    }

    public Collection<FileConfiguration> getFileConfigurations() {
        return fileConfigurations.values();
    }
}
