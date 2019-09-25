package org.daijie.jdbc.generator.executor;

import com.google.common.collect.Lists;
import org.daijie.jdbc.generator.code.CodeGeneratorUtil;
import org.daijie.jdbc.generator.config.AbstractJavaFileConfiguration;
import org.daijie.jdbc.generator.config.DatasourceConfiguration;
import org.daijie.jdbc.generator.config.GeneratorConfiguration;
import org.daijie.jdbc.generator.file.BaseFileCreator;
import org.daijie.jdbc.generator.file.FileCreator;
import org.daijie.jdbc.matedata.TableMateData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

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
        Collection<AbstractJavaFileConfiguration> fileConfigurations = this.configuration.getFileConfigurations();
        List<TableMateData> tableMateDatas = Lists.newArrayList();
        for (DatasourceConfiguration datasourceConfiguration : datasourceConfigurations) {
            String url = datasourceConfiguration.getUrl() + "&user=" + datasourceConfiguration.getUser() + "&password=" + datasourceConfiguration.getPassword();
            tableMateDatas.addAll(ConnectionManage.getMatedata(url, datasourceConfiguration.getDriverClassName()));
        }
        for (AbstractJavaFileConfiguration fileConfiguration : fileConfigurations) {
            String path = System.getProperty("user.dir") + "/" + fileConfiguration.getTargetProject() + "/" +  CodeGeneratorUtil.pathSpotToSlash(fileConfiguration.getTargetPackage());
            for (TableMateData tableMateData : tableMateDatas) {
                fileConfiguration.setTableMateData(tableMateData);
                fileConfiguration.execute();
                String fileName = fileConfiguration.getFileName() + fileConfiguration.getSuffix();
                String code = fileConfiguration.getCode();
                log.debug("{}表生成文件：{}/{}", tableMateData.getName(), path, fileName);
                log.debug("{}表生成文件内容：{}", tableMateData.getName(), code);
                this.fileCreator.createFile(path, code, fileName);
            }
        }
        return null;
    }
}
