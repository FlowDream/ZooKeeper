/**
 * 
 */
package chapter05;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 使用 Fluent 风格的 API 接口来创建一个 ZooKeeper 客户端
 */
public class Create_Session_Sample_Fluent {

	public static void main(String[] args) throws InterruptedException {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
				.sessionTimeoutMs(5000).retryPolicy(retryPolicy).namespace("base").build();
		client.start();
		Thread.sleep(Integer.MAX_VALUE);
	}

}
