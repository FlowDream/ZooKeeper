/**
 * 
 */
package chapter05;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 使用 Curator 创建会话
 */
public class Create_Session_Sample {

	public static void main(String[] args) throws InterruptedException {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", 5000, 3000, retryPolicy);
		client.start();
		Thread.sleep(Integer.MAX_VALUE);
	}

}
