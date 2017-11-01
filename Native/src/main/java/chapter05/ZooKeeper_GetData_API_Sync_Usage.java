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
 * 使用同步 API 获取节点数据内容
 */
public class ZooKeeper_GetData_API_Sync_Usage implements Watcher {
	private static CountDownLatch latch = new CountDownLatch(1);
	private static ZooKeeper zooKeeper = null;
	private static Stat stat = new Stat();

	public static void main(String[] args) throws Exception {
		String path = "/zk-book";
		zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZooKeeper_GetData_API_Sync_Usage());
		latch.await();
		zooKeeper.create(path, "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		System.out.println(new String(zooKeeper.getData(path, true, stat)));
		System.out.println(stat.getCzxid() + "," + stat.getMzxid() + "," + stat.getVersion());
		zooKeeper.setData(path, "123".getBytes(), -1);
		Thread.sleep(Integer.MAX_VALUE);
	}

	@Override
	public void process(WatchedEvent event) {
		try {
			if (KeeperState.SyncConnected == event.getState()) {
				if (EventType.None == event.getType() && null == event.getPath()) {
					latch.countDown();
				} else if (event.getType() == EventType.NodeDataChanged) {
					System.out.println(new String(zooKeeper.getData(event.getPath(), true, stat)));
					System.out.println(stat.getCzxid() + "," + stat.getMzxid() + "," + stat.getVersion());
				}
			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
