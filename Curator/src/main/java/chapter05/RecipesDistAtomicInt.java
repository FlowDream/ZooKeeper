/**
 * 
 */
package chapter05;

import java.util.concurrent.CountDownLatch;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

/**
 * 使用 Curator 实现分布式计数器
 */
public class RecipesDistAtomicInt {
	private static final String PATH = "/curator_recipes_distatomicint_path";

	public static void main(String[] args) throws Exception {
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
				.sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();

		final CountDownLatch latch = new CountDownLatch(1);

		for (int i = 0; i < 10; i++) {
			new Thread() {

				@Override
				public void run() {
					try {
						latch.await();
						long begin = System.currentTimeMillis();
						DistributedAtomicInteger atomicInt = new DistributedAtomicInteger(client, PATH, new RetryNTimes(3, 1000));
						AtomicValue<Integer> rc = atomicInt.increment();
						System.out.println("coust:" + (System.currentTimeMillis() - begin));
						System.out.println(Thread.currentThread().getName() + ", Result:" + rc.succeeded());
						System.out.println(Thread.currentThread().getName() + ", PreValue:" + rc.preValue());
						System.out.println(Thread.currentThread().getName() + ", PostValue:" + rc.postValue());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}.start();
		}

		latch.countDown();
	}
}
