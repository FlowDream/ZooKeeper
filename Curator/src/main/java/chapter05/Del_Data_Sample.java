/**
 * 
 */
package chapter05;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * 使用 Curator 删除节点
 */
public class Del_Data_Sample {

	public static void main(String[] args) throws Exception {
		String path = "/zk-book/c1";

		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
				.sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();

		client.start();

		client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path,
				"init".getBytes());

		Stat stat = new Stat();

		client.getData().storingStatIn(stat).forPath(path);

		client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion()).forPath(path);
	}

}
