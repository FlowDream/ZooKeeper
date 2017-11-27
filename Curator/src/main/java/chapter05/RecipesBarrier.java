/**
 * 
 */
package chapter05;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 使用 Curator 实现分布式 Barrier
 */
public class RecipesBarrier {
	private static String barrier_path = "/curator_recipes_barrier_path";
	private static DistributedBarrier barrier;

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 5; i++) {
			new Thread() {

				@Override
				public void run() {
					try {
						CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
								.retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
						client.start();
						barrier = new DistributedBarrier(client, barrier_path);
						System.out.println(Thread.currentThread().getName() + "号 barrier 设置");
						barrier.setBarrier();
						barrier.waitOnBarrier();
						System.out.println("启动...");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}.start();
		}

		Thread.sleep(2000);
		barrier.removeBarrier();
	}

}
