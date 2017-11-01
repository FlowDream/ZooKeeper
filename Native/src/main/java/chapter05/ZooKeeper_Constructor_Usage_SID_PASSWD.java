/**
 * 
 */
package chapter05;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * 创建一个复用 sessionId 和 sessionPasswd 的 ZooKeeper 对象实例
 *
 */
public class ZooKeeper_Constructor_Usage_SID_PASSWD implements Watcher {
	private static CountDownLatch latch = new CountDownLatch(1);

	public static void main(String[] args) {
		try {
			ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZooKeeper_Constructor_Usage_SID_PASSWD());
			latch.await();
			long sessionId = zooKeeper.getSessionId();
			byte[] passwd = zooKeeper.getSessionPasswd();

			// Use illegal sessionId and sessionPassWd
			zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZooKeeper_Constructor_Usage_SID_PASSWD(), 1L,
					"test".getBytes());

			// Use correct sessionId and sessionPassWd
			zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZooKeeper_Constructor_Usage_SID_PASSWD(), sessionId,
					passwd);
			Thread.sleep(Integer.MAX_VALUE);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println("Reveive watched event:" + event);

		if (KeeperState.SyncConnected == event.getState()) {
			latch.countDown();
		}
	}
}
