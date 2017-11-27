/**
 * 
 */
package chapter05;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用 CyclicBarrier 模拟一个赛跑比赛
 */
public class RecipesCyclicBarrier {

	private static CyclicBarrier barrier = new CyclicBarrier(3);

	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(3);
		pool.submit(new MyThread("1 号选手"));
		pool.submit(new MyThread("2 号选手"));
		pool.submit(new MyThread("3 号选手"));
		pool.shutdown();
	}

	private static class MyThread extends Thread {
		private String name;

		public MyThread(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			System.out.println(name + "准备好了");

			try {
				barrier.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}

			System.out.println(name + "起跑");
		}
	}

}
