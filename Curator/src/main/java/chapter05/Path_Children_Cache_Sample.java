/**
 * 
 */
package chapter05;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * PathChildrenCache 使用示例
 */
public class Path_Children_Cache_Sample {

	public static void main(String[] args) throws Exception {
		String path = "/zk-book";
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
				.sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();
		PathChildrenCache cache = new PathChildrenCache(client, path, true);
		cache.start(StartMode.POST_INITIALIZED_EVENT);
		cache.getListenable().addListener(new PathChildrenCacheListener() {

			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				switch (event.getType()) {
				case CHILD_ADDED:
					System.out.println("CHILD_ADDED" + event.getData().getPath());
					break;
				case CHILD_UPDATED:
					System.out.println("CHILD_UPDATED" + event.getData().getPath());
					break;
				case CHILD_REMOVED:
					System.out.println("CHILD_REMOVED" + event.getData().getPath());
					break;
				default:
					break;
				}
			}
		});

		client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
		Thread.sleep(1000);
		client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path + "/c1");
		Thread.sleep(1000);
		client.delete().forPath(path + "/c1");
		Thread.sleep(1000);
		client.delete().forPath(path);
		Thread.sleep(Integer.MAX_VALUE);
	}

}
