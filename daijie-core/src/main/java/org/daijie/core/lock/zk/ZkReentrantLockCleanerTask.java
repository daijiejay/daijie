package org.daijie.core.lock.zk;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;

/**
 * zookeeper作业
 * @author daijie_jay
 * @since 2017年11月24日
 */
public class ZkReentrantLockCleanerTask extends TimerTask {
	
	private static final Logger logger = Logger.getLogger(ZkReentrantLockCleanerTask.class);

    private CuratorFramework client;

    private Timer timer;

    /**
     * 检查周期
     */
    private long period=5000;

    private int maxRetries=3;

    private final int baseSleepTimeMs=1000;

    public ZkReentrantLockCleanerTask(String zookeeperAddress) {
        try{
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
            client = CuratorFrameworkFactory.newClient(zookeeperAddress, retryPolicy);
            client.start();
        }catch (Exception e){
        	logger.error(e.getMessage(),e);
        }catch (Throwable ex){
            ex.printStackTrace();
            logger.error(ex.getMessage(),ex);
        }
    }

    public void start(){
        timer.schedule(this,0,period);
    }

    private boolean isEmpty(List<String> list){
        return list==null||list.isEmpty();
    }


    @Override
    public void run() {
        try {
            List<String> childrenPaths=this.client.getChildren().forPath(ZkReentrantLock.ROOT_PATH);
            for(String path:childrenPaths){
                cleanNode(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cleanNode(String path){
        try {
            if(isEmpty(this.client.getChildren().forPath(path))){
                this.client.delete().forPath(path);//利用存在子节点无法删除和zk的原子性这两个特性.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
