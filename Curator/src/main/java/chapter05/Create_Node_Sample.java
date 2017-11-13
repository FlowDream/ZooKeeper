/**
 * 
 */
package chapter05;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * 使用 Curator 创建节点
 */
public class Create_Node_Sample {

	public static void main(String[] args) throws Exception {
		String path = "/zk-book/c1";

		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
				.sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

		client.start();

		client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path,
				"init".getBytes());

		Thread.sleep(Integer.MAX_VALUE);
	}

}
