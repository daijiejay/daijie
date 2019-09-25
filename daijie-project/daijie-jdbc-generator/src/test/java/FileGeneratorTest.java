import org.daijie.jdbc.generator.config.*;
import org.daijie.jdbc.generator.executor.FileGenerator;
import org.daijie.jdbc.generator.executor.Generator;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jerry.dai
 * @version SDP_V2.00.00
 * @date 2019/9/17
 */
public class FileGeneratorTest {

    private Generator generator;

    @Before
    public void init() throws ReflectiveOperationException {
        GeneratorConfiguration generatorConfiguration = new GeneratorConfiguration();
        Map<String, Object> properties = new HashMap<>();
        properties.put("driverClassName", "com.mysql.jdbc.Driver");
        properties.put("url", "jdbc:mysql://localhost:3306/demo?characterEncoding=UTF-8");
        properties.put("username", "root");
        properties.put("password", "123456");
        generatorConfiguration.addDatasource(properties);
        ModelFileConfiguration modelFileConfiguration = FileConfigurationManager.newInstance(ModelFileConfiguration.class, "org.daijie.model", "src/test/java");
        generatorConfiguration.addFileConfiguration(modelFileConfiguration);
        MapperFileConfiguration mapperFileConfiguration = FileConfigurationManager.newInstance(MapperFileConfiguration.class, "org.daijie.mapper", "src/test/java");
        mapperFileConfiguration.addInterfacePackages("org.daijie.jdbc.session.SessionMapper");
        generatorConfiguration.addFileConfiguration(mapperFileConfiguration);

        AbstractHtmlFileConfiguration htmlFileConfiguration = FileConfigurationManager.newInstance(AbstractHtmlFileConfiguration.class, "C:\\Users\\shiji\\Desktop", "数据结构");
        generatorConfiguration.addFileConfiguration(htmlFileConfiguration);
        this.generator = new FileGenerator(generatorConfiguration);
    }

    @Test
    public void test() {
        this.generator.execute();
    }
}
