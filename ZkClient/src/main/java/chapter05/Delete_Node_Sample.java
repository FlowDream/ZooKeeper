/**
 * 
 */
package chapter05;

import org.I0Itec.zkclient.ZkClient;

/**
 * 使用 ZkClient 删除节点
 * 
 */
public class Delete_Node_Sample {

	public static void main(String[] args) {
		String path = "/zk-book";
		String path2 = path + "/c1";
		ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
		zkClient.createPersistent(path2, true);
		System.out.println("create success" + path);
		// 逐层遍历删除节点
		boolean result = zkClient.deleteRecursive(path);
		System.out.println("delete result:" + result);
	}

}
