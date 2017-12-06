/**
 * 
 */
package chapter05;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 使用 Curator 实现另一种分布式 Barrier
 */
public class RecipesBarrier2 {
	private static String barrier_path = "/curator_recipes_barrier_path";

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 5; i++) {
			new Thread() {

				@Override
				public void run() {
					try {
						CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
								.retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
						client.start();
						DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client, barrier_path, 5);
						Thread.sleep(Math.round(Math.random() * 3000L));
						System.out.println(Thread.currentThread().getName() + "号进入 barrier");
						barrier.enter();
						System.out.println("启动...");
						Thread.sleep(Math.round(Math.random() * 3000L));
						barrier.leave();
						System.out.println("退出...");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}.start();
		}
	}

}
