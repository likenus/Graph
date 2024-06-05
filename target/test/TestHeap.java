package target.test;

import org.junit.Assert;
import org.junit.Test;

import src.util.queues.FibonacciHeap;
import src.util.queues.PriorityQueue;

public class TestHeap {
	
	@Test
	public void testHeap() {
		PriorityQueue<Integer> fh = new FibonacciHeap<>();
		fh.push(1, 7);
		fh.push(2, 10);
		fh.push(3, 15);
		fh.push(4, 1);
		fh.push(5, 2);

		fh.decPrio(3, 3);

		Assert.assertEquals(4, fh.popMin().intValue());
		Assert.assertEquals(5, fh.popMin().intValue());
		Assert.assertEquals(3, fh.popMin().intValue());
	}
}
