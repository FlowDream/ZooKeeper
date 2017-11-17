/**
 * 
 */
package chapter05;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * NodeCache 使用示例
 */
public class Node_Cache_Sample {

	public static void main(String[] args) throws Exception {
		String path = "/zk-book/nodecache";
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
				.sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();
		client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path,
				"init".getBytes());

		NodeCache cache = new NodeCache(client, path, false);
		cache.start();
		cache.getListenable().addListener(new NodeCacheListener() {

			@Override
			public void nodeChanged() throws Exception {
				System.out.println("Node data updae, new data:" + new String(cache.getCurrentData().getData()));
			}
		});

		client.setData().forPath(path, "u".getBytes());
		Thread.sleep(1000);
		client.delete().deletingChildrenIfNeeded().forPath(path);
		Thread.sleep(Integer.MAX_VALUE);
	}

}
