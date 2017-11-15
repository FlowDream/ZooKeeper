/**
 * 
 */
package chapter05;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * Curator 的异步接口
 */
public class Create_Node_Background_Sample {
	private static CountDownLatch latch = new CountDownLatch(2);
	private static ExecutorService pool = Executors.newFixedThreadPool(2);

	public static void main(String[] args) throws Exception {
		String path = "/zk-book";
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
				.sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
		client.start();
		System.out.println("Main Thread:" + Thread.currentThread().getName());

		client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL)
				.inBackground(new BackgroundCallback() {

					@Override
					public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
						System.out.println("event [code:" + event.getResultCode() + ", type:" + event.getType() + "]");
						System.out.println("Thread of processResult:" + Thread.currentThread().getName());
						latch.countDown();
					}
				}, pool).forPath(path, "init".getBytes());

		client.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL)
				.inBackground(new BackgroundCallback() {

					@Override
					public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
						System.out.println("event [code:" + event.getResultCode() + ", type:" + event.getType() + "]");
						System.out.println("Thread of processResult:" + Thread.currentThread().getName());
						latch.countDown();
					}
				}).forPath(path, "init".getBytes());

		latch.await();
		pool.shutdown();
	}

}
