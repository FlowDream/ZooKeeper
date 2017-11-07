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
import org.apache.zookeeper.data.Stat;

/**
 * 使用同步 API 更新节点数据内容
 */
public class ZooKeeper_SetData_API_Sync_Usage implements Watcher {
	private static CountDownLatch latch = new CountDownLatch(1);
	private static ZooKeeper zooKeeper = null;

	public static void main(String[] args) throws Exception {
		String path = "/zk-book";
		zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZooKeeper_SetData_API_Sync_Usage());
		latch.await();
		zooKeeper.create(path, "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		zooKeeper.getData(path, true, null);

		Stat stat = zooKeeper.setData(path, "456".getBytes(), -1);
		System.out.println(stat.getCzxid() + "," + stat.getMzxid() + "," + stat.getVersion());

		Stat stat2 = zooKeeper.setData(path, "456".getBytes(), stat.getVersion());
		System.out.println(stat2.getCzxid() + "," + stat2.getMzxid() + "," + stat2.getVersion());

		try {
			zooKeeper.setData(path, "456".getBytes(), stat.getVersion());
		} catch (KeeperException e) {
			System.out.println("Error:" + e.code() + ", " + e.getMessage());
		}

		Thread.sleep(Integer.MAX_VALUE);
	}

	@Override
	public void process(WatchedEvent event) {
		if (KeeperState.SyncConnected == event.getState()) {
			if (EventType.None == event.getType() && null == event.getPath()) {
				latch.countDown();
			}
		}
	}

}
