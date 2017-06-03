//package test;
//import org.apache.log4j.Logger;
//
//import java.util.ArrayList;
//import java.util.List;
//import org.mybatis.generator.api.MyBatisGenerator;
//import org.mybatis.generator.config.Configuration;
//import org.mybatis.generator.config.xml.ConfigurationParser;
//import org.mybatis.generator.internal.DefaultShellCallback;
//
//public class _Ibator_Run_Test {
//	private static final Logger logger = Logger.getLogger(_Ibator_Run_Test.class);
//
//	public static void main(String[] args) {
//		_Ibator_Run_Test test = new _Ibator_Run_Test();
////		test.main1("grid1.xml");
////		test.main1("mysql.xml");
////		test.main1("mysql_bank.xml");
//		test.main1("authority_mysql.xml");
////		test.main1("authority_oracle.xml");
////		test.main1("tsm_oracle.xml");
////		test.main1("ejt_oracle.xml");
////		test.main1("datacheck_sql_2.xml");
////		test.main1("oracle.xml");
//	}
//
//	public void main1(String fileName) {
//		try {
//			List<String> warnings = new ArrayList<String>();
//			ConfigurationParser cp = new ConfigurationParser(warnings);
//			Configuration config = cp
//					.parseConfiguration(this.getClass().getClassLoader().getResourceAsStream(fileName));
//
//			DefaultShellCallback shellCallback = new DefaultShellCallback(true);
//
//			MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
//			myBatisGenerator.generate(null);
//		} catch (Exception e) {
//			logger.error("Exception:", e);
//		}
//	}
//
//}
