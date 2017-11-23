/**
 * 
 */
package chapter05;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 使用 Curator 实现分布式锁功能
 */
public class RecipesLock {
	private static final String LOCK_PATH = "/curator_recipes_lock_path";

	public static void main(String[] args) throws Exception {
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
				.sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();

		final InterProcessMutex lock = new InterProcessMutex(client, LOCK_PATH);
		final CountDownLatch latch = new CountDownLatch(1);

		for (int i = 0; i < 30; i++) {
			new Thread() {

				@Override
				public void run() {
					try {
						latch.await();
						lock.acquire();
					} catch (Exception e) {
						e.printStackTrace();
					}

					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
					String orderNo = sdf.format(new Date());
					System.out.println("生成的订单号:" + orderNo);

					try {
						lock.release();
					} catch (Exception e) {
					}
				}

			}.start();
		}

		latch.countDown();
		Thread.sleep(Integer.MAX_VALUE);
	}
}
