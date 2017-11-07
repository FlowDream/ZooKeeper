/**
 * 
 */
package chapter05;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * 使用错误权限信息的 ZooKeeper 会话访问含权限信息的数据节点
 */
public class ZooKeeper_Auth_Get2_API_Usage {
	private static final String PATH = "/zk-book-auth-test";

	public static void main(String[] args) throws Exception {
		ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, null);
		zooKeeper.addAuthInfo("digest", "foo:true".getBytes());
		zooKeeper.create(PATH, "init".getBytes(), Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

		ZooKeeper zooKeeper2 = new ZooKeeper("127.0.0.1:2181", 5000, null);
		zooKeeper2.addAuthInfo("digest", "foo:true".getBytes());
		System.out.println(zooKeeper2.getData(PATH, false, null));

		ZooKeeper zooKeeper3 = new ZooKeeper("127.0.0.1:2181", 5000, null);
		zooKeeper3.addAuthInfo("digest", "foo:false".getBytes());
		System.out.println(zooKeeper3.getData(PATH, false, null));

		Thread.sleep(Integer.MAX_VALUE);
	}

}
