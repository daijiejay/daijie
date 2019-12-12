package org.daijie.test;

import org.daijie.test.listener.JunitRunListenerManager;
import org.junit.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 继承BlockJUnit4ClassRunner，自定义监听类
 * @author daijie_jay
 * @since 2019年11月10日
 */
public class JunitRunner extends SpringJUnit4ClassRunner {

    /**
     * Creates a BlockJUnit4ClassRunner to run {@code klass}
     *
     * @param klass
     * @throws InitializationError if the test class is malformed.
     */
    public JunitRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    public void run(RunNotifier notifier) {
        JunitRunListenerManager.initRegisterRunListener();
        notifier.addListener(JunitRunListenerManager.newProxyInstance(JunitRunListener.class));
        EachTestNotifier testNotifier = new EachTestNotifier(notifier, getDescription());
        notifier.fireTestRunStarted(getDescription());
        try {
            Statement statement = classBlock(notifier);
            statement.evaluate();
        } catch (AssumptionViolatedException av) {
            testNotifier.addFailedAssumption(av);
        } catch(StoppedByUserException sbue) {
            throw sbue;
        } catch(Throwable e) {
            testNotifier.addFailure(e);
        }
    }
}
