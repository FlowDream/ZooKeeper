/**
 * 
 */
package chapter05;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * 使用 ZkClient 获取节点数据内容
 * 
 */
public class Get_Data_Sample {

	public static void main(String[] args) throws InterruptedException {
		String path = "/zk-book";
		ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000);
		zkClient.createEphemeral(path, "123");
		zkClient.subscribeDataChanges(path, new IZkDataListener() {

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println("Node " + dataPath + "deleted.");
			}

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				System.out.println("Node " + dataPath + "change, new data:" + data);
			}
		});

		System.out.println(zkClient.readData(path).toString());
		zkClient.writeData(path, "456");
		Thread.sleep(1000);
		zkClient.delete(path);
		Thread.sleep(Integer.MAX_VALUE);
	}

}
