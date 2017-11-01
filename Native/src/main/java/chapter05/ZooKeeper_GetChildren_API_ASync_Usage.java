/**
 * 
 */
package chapter05;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 使用异步 API 获取节点列表
 */
public class ZooKeeper_GetChildren_API_ASync_Usage implements Watcher {
	private static CountDownLatch latch = new CountDownLatch(1);
	private static ZooKeeper zooKeeper = null;

	public static void main(String[] args) throws Exception {
		String path = "/zk-book";
		zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZooKeeper_GetChildren_API_ASync_Usage());
		latch.await();
		zooKeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zooKeeper.create(path + "/c1", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		zooKeeper.getChildren(path, true, new IChildren2Callback(), null);
		zooKeeper.create(path + "/c2", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		Thread.sleep(Integer.MAX_VALUE);
	}

	private static class IChildren2Callback implements AsyncCallback.Children2Callback {

		@Override
		public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
			System.out.println("Get Children zonde result:[response code:" + rc + ", path:" + path + ", ctx:" + ctx
					+ ", children list:" + children + ", stat:" + stat);
		}

	}
	
	@Override
	public void process(WatchedEvent event) {
		try {
			if (KeeperState.SyncConnected == event.getState()) {
				if (EventType.None == event.getType() && null == event.getPath()) {
					latch.countDown();
				} else if (event.getType() == EventType.NodeChildrenChanged) {
					System.out.println("ReGet Child:" + zooKeeper.getChildren(event.getPath(), true));
				}
			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
}
