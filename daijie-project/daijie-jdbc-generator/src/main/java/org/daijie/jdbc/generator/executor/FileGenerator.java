package org.daijie.jdbc.generator.executor;

import com.google.common.collect.Lists;
import org.daijie.jdbc.generator.code.CodeGeneratorUtil;
import org.daijie.jdbc.generator.config.*;
import org.daijie.jdbc.generator.file.BaseFileCreator;
import org.daijie.jdbc.generator.file.FileCreator;
import org.daijie.jdbc.matedata.TableMateData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * 文件生成器
 * @author daijie
 * @since 2019/9/16
 */
public class FileGenerator implements Generator {

    private Logger log = LoggerFactory.getLogger(FileGenerator.class);

    private final GeneratorConfiguration configuration;
    private final FileCreator fileCreator;

    public FileGenerator(GeneratorConfiguration configuration) {
        this.configuration = configuration;
        this.fileCreator = new BaseFileCreator();
    }

    @Override
    public Object execute() {
        List<DatasourceConfiguration> datasourceConfigurations = this.configuration.getDatasourceConfigurations();
        Collection<FileConfiguration> fileConfigurations = this.configuration.getFileConfigurations();
        List<TableMateData> tableMateDatas = Lists.newArrayList();
        for (DatasourceConfiguration datasourceConfiguration : datasourceConfigurations) {
            Properties properties = new Properties();
            properties.setProperty("user", datasourceConfiguration.getUser());
            properties.setProperty("password", datasourceConfiguration.getPassword());
            properties.setProperty("remarks", "true");
            properties.setProperty("useInformationSchema", "true");
            tableMateDatas.addAll(ConnectionClientManage.getMatedata(datasourceConfiguration.getUrl(), datasourceConfiguration.getDriverClassName(), properties));
        }
        for (FileConfiguration fileConfiguration : fileConfigurations) {
            if (fileConfiguration instanceof AbstractJavaFileConfiguration) {
                AbstractJavaFileConfiguration javaFileConfiguration = (AbstractJavaFileConfiguration) fileConfiguration;
                String path = System.getProperty("user.dir") + "/" + javaFileConfiguration.getTargetProject() + "/" +  CodeGeneratorUtil.pathSpotToSlash(javaFileConfiguration.getTargetPackage());
                for (TableMateData tableMateData : tableMateDatas) {
                    javaFileConfiguration.setTableMateData(tableMateData);
                    javaFileConfiguration.execute();
                    String fileName = javaFileConfiguration.getFileName() + javaFileConfiguration.getSuffix();
                    String code = javaFileConfiguration.getCode();
                    log.info("{}表已生成文件：{}/{}", tableMateData.getName(), path, fileName);
                    log.debug("{}表已生成文件内容：{}", tableMateData.getName(), code);
                    this.fileCreator.createFile(path, code, fileName);
                }
            } else if (fileConfiguration instanceof AbstractHtmlFileConfiguration) {
                AbstractHtmlFileConfiguration htmlFileConfiguration = (AbstractHtmlFileConfiguration) fileConfiguration;
                htmlFileConfiguration.setTableMateDatas(tableMateDatas);
                htmlFileConfiguration.execute();
                String fileName = htmlFileConfiguration.getFileName() + htmlFileConfiguration.getSuffix();
                String code = htmlFileConfiguration.getCode();
                this.fileCreator.createFile(htmlFileConfiguration.getPath(), code, fileName);
                log.info("已生成文件：{}", fileName);
            }

        }
        return null;
    }
}
