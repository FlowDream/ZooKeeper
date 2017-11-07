/**
 * 
 */
package chapter05;

import org.I0Itec.zkclient.ZkClient;

/**
 * 使用 ZkClient 创建节点
 * 
 */
public class Create_Node_Sample {

	public static void main(String[] args) {
		String path = "/zk-book/c1";
		ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
		zkClient.createPersistent(path, true);
	}

}
