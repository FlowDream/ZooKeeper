/**
 * 
 */
package chapter05;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * 删除节点接口的权限控制
 */
public class ZooKeeper_Auth_Delete_API_Usage {
	private static final String PATH = "/zk-book-auth-test";
	private static final String PATH2 = "/zk-book-auth-test/child";

	public static void main(String[] args) throws Exception {
		ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, null);
		zooKeeper.addAuthInfo("digest", "foo:true".getBytes());
		zooKeeper.create(PATH, "init".getBytes(), Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
		zooKeeper.create(PATH2, "init".getBytes(), Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);

		try {
			ZooKeeper zooKeeper2 = new ZooKeeper("127.0.0.1:2181", 5000, null);
			zooKeeper2.delete(PATH2, -1);
		} catch (Exception e) {
			System.out.println("删除节点失败:" + e.getMessage());
		}

		ZooKeeper zooKeeper3 = new ZooKeeper("127.0.0.1:2181", 5000, null);
		zooKeeper3.addAuthInfo("digest", "foo:true".getBytes());
		zooKeeper3.delete(PATH2, -1);
		System.out.println("成功删除节点:" + PATH2);

		ZooKeeper zooKeeper4 = new ZooKeeper("127.0.0.1:2181", 5000, null);
		zooKeeper4.delete(PATH, -1);
		System.out.println("成功删除节点:" + PATH);
		Thread.sleep(Integer.MAX_VALUE);
	}

}
