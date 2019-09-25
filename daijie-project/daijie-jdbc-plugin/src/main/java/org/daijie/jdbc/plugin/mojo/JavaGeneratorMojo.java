package org.daijie.jdbc.plugin.mojo;

import com.google.common.collect.Lists;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.daijie.jdbc.generator.config.*;
import org.daijie.jdbc.generator.executor.FileGenerator;
import org.daijie.jdbc.plugin.configuration.DatasourceConfiguration;
import org.daijie.jdbc.plugin.configuration.MapperJavaConfiguration;
import org.daijie.jdbc.plugin.configuration.ModelJavaConfiguration;

import java.util.List;

/**
 * java文件生成maven插件
 * @author daijie
 * @since 2019/9/22
 */
@Mojo( name = "generator-java")
public class JavaGeneratorMojo extends AbstractMojo {

    /**
     * mapper类配置信息
     */
    @Parameter(property = "mapper")
    private MapperJavaConfiguration mapper;

    /**
     * 实体类配置信息
     */
    @Parameter(property = "model")
    private ModelJavaConfiguration model;

    /**
     * 数据源配置
     */
    @Parameter(property = "datasources")
    private List<DatasourceConfiguration> datasources = Lists.newArrayList();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        GeneratorConfiguration generatorConfiguration = new GeneratorConfiguration();
        for (DatasourceConfiguration htmlGeneratorConfiguration : this.datasources) {
            generatorConfiguration.addDatasource(htmlGeneratorConfiguration.getDriverClassName(), htmlGeneratorConfiguration.getUrl(), htmlGeneratorConfiguration.getUsername(), htmlGeneratorConfiguration.getPassword());
        }
        Class modelClass = null;
        Class mapperClass = null;
        try {
            modelClass = Class.forName(this.model.getModeClass());
            mapperClass = Class.forName(this.model.getModeClass());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (modelClass == null || !modelClass.isAssignableFrom(ModelFileConfiguration.class)) {
            modelClass = ModelFileConfiguration.class;
        }
        if (mapperClass == null || !mapperClass.isAssignableFrom(MapperFileConfiguration.class)) {
            mapperClass = MapperFileConfiguration.class;
        }
        ModelFileConfiguration modelFileConfiguration = (ModelFileConfiguration) FileConfigurationManager.newInstance(modelClass, this.model.getTargetPackage(), this.model.getTargetProject(),
                this.model.isOverrideToString(), this.model.isOverrideEquats(), this.model.isLombok(), this.model.isSerializeble());
        generatorConfiguration.addFileConfiguration(modelFileConfiguration);
        MapperFileConfiguration mapperFileConfiguration = (MapperFileConfiguration) FileConfigurationManager.newInstance(mapperClass, this.mapper.getTargetPackage(), this.mapper.getTargetProject());
        mapperFileConfiguration.addInterfacePackages(this.mapper.getInterfacePackages());
        generatorConfiguration.addFileConfiguration(mapperFileConfiguration);
        new FileGenerator(generatorConfiguration).execute();
    }
}
