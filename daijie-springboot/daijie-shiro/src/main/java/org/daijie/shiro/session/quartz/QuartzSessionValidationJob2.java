package org.daijie.shiro.session.quartz;

import org.apache.shiro.session.mgt.ValidatingSessionManager;  
import org.quartz.Job;  
import org.quartz.JobDataMap;  
import org.quartz.JobExecutionContext;  
import org.quartz.JobExecutionException;  
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  
  
/** 
 * 基于Quartz 2.* 版本的实现
 * @author daijie
 * @since 2017年6月22日
 */
public class QuartzSessionValidationJob2 implements Job {  
  
    public static final String SESSION_MANAGER_KEY = "sessionManager";  
  
    private static final Logger log = LoggerFactory.getLogger(QuartzSessionValidationJob2.class);  
  
    public void execute(JobExecutionContext context) throws JobExecutionException {  
  
        JobDataMap jobDataMap = context.getMergedJobDataMap();  
        ValidatingSessionManager sessionManager = (ValidatingSessionManager) jobDataMap.get(SESSION_MANAGER_KEY);  
  
        if (log.isDebugEnabled()) {  
            log.debug("Executing session validation Quartz job...");  
        }  
  
        sessionManager.validateSessions();  
  
        if (log.isDebugEnabled()) {  
            log.debug("Session validation Quartz job complete.");  
        }  
    }  
  
}
