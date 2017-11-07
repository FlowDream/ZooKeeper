/**
 * 
 */
package chapter05;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * 使用同步 API 检测节点是否存在
 */
public class ZooKeeper_Exist_API_Sync_Usage implements Watcher {
	private static CountDownLatch latch = new CountDownLatch(1);
	private static ZooKeeper zooKeeper = null;

	public static void main(String[] args) throws Exception {
		String path = "/zk-book";
		zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZooKeeper_Exist_API_Sync_Usage());
		latch.await();
		zooKeeper.exists(path, true);
		zooKeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zooKeeper.setData(path, "123".getBytes(), -1);
		zooKeeper.create(path + "/c1", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zooKeeper.delete(path + "/c1", -1);
		zooKeeper.delete(path, -1);
		Thread.sleep(Integer.MAX_VALUE);
	}

	@Override
	public void process(WatchedEvent event) {
		try {
			if (KeeperState.SyncConnected == event.getState()) {
				if (EventType.None == event.getType() && null == event.getPath()) {
					latch.countDown();
				} else if (event.getType() == EventType.NodeCreated) {
					System.out.println("Node(" + event.getPath() + ")Created");
					zooKeeper.exists(event.getPath(), true);
				} else if (event.getType() == EventType.NodeDeleted) {
					System.out.println("Node(" + event.getPath() + ")Deleted");
					zooKeeper.exists(event.getPath(), true);
				} else if (event.getType() == EventType.NodeDataChanged) {
					System.out.println("Node(" + event.getPath() + ")DataChanged");
					zooKeeper.exists(event.getPath(), true);
				}
			}
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
