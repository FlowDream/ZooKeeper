/**
 * 
 */
package chapter05;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * 使用异步 API 创建一个节点
 */
public class ZooKeeper_Create_API_ASync_Usage implements Watcher {
	private static CountDownLatch latch = new CountDownLatch(1);

	public static void main(String[] args) {
		try {
			ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZooKeeper_Create_API_ASync_Usage());
			latch.await();

			zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
					new IStringCallBack(), "I am context");

			zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
					new IStringCallBack(), "I am context");

			zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,
					new IStringCallBack(), "I am context");
			Thread.sleep(Integer.MAX_VALUE);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(WatchedEvent event) {
		if (KeeperState.SyncConnected == event.getState()) {
			latch.countDown();
		}
	}

	private static class IStringCallBack implements AsyncCallback.StringCallback {

		@Override
		public void processResult(int rc, String path, Object ctx, String name) {
			System.out.println("Create path result:[" + rc + ", " + path + ", " + ctx + ", real path name:" + name);
		}

	}

}
