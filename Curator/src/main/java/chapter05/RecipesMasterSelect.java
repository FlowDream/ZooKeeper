/**
 * 
 */
package chapter05;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 使用 Curator 实现分布式 Master 选举
 */
public class RecipesMasterSelect {
	private static final String MASTER_PATH = "/curator_recipes_master_path";

	public static void main(String[] args) throws Exception {
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
				.sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();

		LeaderSelector selector = new LeaderSelector(client, MASTER_PATH, new LeaderSelectorListenerAdapter() {

			@Override
			public void takeLeadership(CuratorFramework client) throws Exception {
				System.out.println("成为 Master 角色");
				Thread.sleep(3000);
				System.out.println("完成 Master 操作，释放 Master 权利");
			}
		});

		selector.autoRequeue();
		selector.start();
		Thread.sleep(Integer.MAX_VALUE);
	}
}
