package org.daijie.test.listener;

import org.junit.runner.notification.RunListener;

/**
 * 单元测试监听执行器
 * @author daijie_jay
 * @since 2019年11月10日
 */
public interface TestRunListener {

    /**
     * 监听单元测试并通知执行具体业务
     * @param object 监听传入的
     */
    void notify(Object object);

    /**
     * 获取具体单元测试实现类的监听器
     * @return RunListener的子类
     */
    RunListener getRunListener();

    TestRunListener getTestRunListener();
}
