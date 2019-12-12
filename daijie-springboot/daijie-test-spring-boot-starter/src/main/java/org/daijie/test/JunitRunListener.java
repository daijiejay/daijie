package org.daijie.test;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * 单元测试监听
 * @author daijie_jay
 * @since 2019年11月10日
 */
public class JunitRunListener extends RunListener {

    @Override
    public void testRunStarted(Description description) throws Exception {
        System.out.println(description);
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        System.out.println(result);
    }

    @Override
    public void testStarted(Description description) throws Exception {
        System.out.println(description);
    }

    @Override
    public void testFinished(Description description) throws Exception {
        System.out.println(description);
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        System.out.println(failure);
    }

    @Override
    public void testAssumptionFailure(Failure failure) {

    }

    @Override
    public void testIgnored(Description description) throws Exception {
        super.testIgnored(description);
    }
}
