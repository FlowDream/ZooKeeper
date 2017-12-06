/**
 * 
 */
package chapter05;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.EnsurePath;

/**
 * 工具类 EnsurePath 使用示例
 */
public class EnsurePathDemo {
	private static String path = "/zk-book/c1";

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
				.sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();
		client.usingNamespace("zk-book");

		EnsurePath ensurePath = new EnsurePath(path);
		ensurePath.ensure(client.getZookeeperClient());
		ensurePath.ensure(client.getZookeeperClient());

		EnsurePath ensurePath2 = client.newNamespaceAwareEnsurePath("/c1");
		ensurePath2.ensure(client.getZookeeperClient());
	}

}
