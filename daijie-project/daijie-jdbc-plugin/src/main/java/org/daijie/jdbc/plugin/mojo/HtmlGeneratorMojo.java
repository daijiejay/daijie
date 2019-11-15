package org.daijie.jdbc.plugin.mojo;

import com.google.common.collect.Lists;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.daijie.jdbc.generator.config.AbstractHtmlFileConfiguration;
import org.daijie.jdbc.generator.config.FileConfigurationManager;
import org.daijie.jdbc.generator.config.GeneratorConfiguration;
import org.daijie.jdbc.generator.executor.FileGenerator;
import org.daijie.jdbc.plugin.configuration.DatasourceConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 数据库导出数据结构生成文件maven插件
 * @author daijie
 * @since 2019/9/22
 */

@Mojo( name = "doc")
public class HtmlGeneratorMojo extends AbstractMojo {

    private static final Logger logger = LoggerFactory.getLogger(HtmlGeneratorMojo.class);

    /**
     * 生成路径
     */
    @Parameter(property = "path")
    private String path;

    /**
     * 生成文件名
     */
    @Parameter(property = "fileName")
    private String fileName;

    /**
     * 数据源配置
     */
    @Parameter(property = "datasources")
    private List<DatasourceConfiguration> datasources = Lists.newArrayList();

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            GeneratorConfiguration generatorConfiguration = new GeneratorConfiguration();
            for (DatasourceConfiguration htmlGeneratorConfiguration : this.datasources) {
                generatorConfiguration.addDatasource(htmlGeneratorConfiguration.getDriverClassName(), htmlGeneratorConfiguration.getUrl(), htmlGeneratorConfiguration.getUsername(), htmlGeneratorConfiguration.getPassword());
            }
            AbstractHtmlFileConfiguration htmlFileConfiguration = FileConfigurationManager.newInstance(AbstractHtmlFileConfiguration.class, path, fileName);
            generatorConfiguration.addFileConfiguration(htmlFileConfiguration);
            new FileGenerator(generatorConfiguration).execute();
        } catch (Exception e) {
            logger.error("生成文档失败！", e);
        }
    }
}
