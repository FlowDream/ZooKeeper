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
 * 创建一个最基本的 ZooKeeper 会话实例
 *
 */
public class ZooKeeper_Constructor_Usage_Simple implements Watcher {
	private static CountDownLatch latch = new CountDownLatch(1);

	/**
	 * Java API -> 创建连接 -> 创建一个最基本的 ZooKeeper 会话实例
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZooKeeper_Constructor_Usage_Simple());
			System.out.println(zooKeeper.getState());
			latch.await();
			System.out.println("ZooKeeper session established");
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
