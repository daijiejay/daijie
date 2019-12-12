package org.daijie.test.listener;

import org.daijie.test.JunitRunListener;
import org.junit.runner.notification.RunListener;

/**
 * 所有测试开始前被调用的监听类
 * @author daijie_jay
 * @since 2019年11月10日
 */
public abstract class TestRunStartedListener implements TestRunListener{

    @Override
    public RunListener getRunListener() {
        return new JunitRunListener();
    }
}
