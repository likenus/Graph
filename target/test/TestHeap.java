package target.test;

import org.junit.Assert;
import org.junit.Test;

import src.util.queues.FibonacciHeap;
import src.util.queues.PriorityQueue;

public class TestHeap {
    
    @Test
    public void testHeapPopMin() {
        PriorityQueue<Integer> fh = new FibonacciHeap<>();
        fh.push(1, 10);
        fh.push(2, 9);
        fh.push(3, 8);
        fh.push(4, 7);
        fh.push(5, 6);
        fh.push(6, 5);
        fh.push(7, 4);
        fh.push(8, 3);
        fh.push(9, 2);
        fh.push(10, 1);


        Assert.assertEquals(10, fh.popMin().intValue());
        Assert.assertEquals(9, fh.popMin().intValue());
        Assert.assertEquals(8, fh.popMin().intValue());
        Assert.assertEquals(7, fh.popMin().intValue());
        Assert.assertEquals(6, fh.popMin().intValue());
        Assert.assertEquals(5, fh.popMin().intValue());
        Assert.assertEquals(4, fh.popMin().intValue());
        Assert.assertEquals(3, fh.popMin().intValue());
        Assert.assertEquals(2, fh.popMin().intValue());
        Assert.assertEquals(1, fh.popMin().intValue());
    }

    @Test
    public void testDecPrio() {
        PriorityQueue<Integer> fh = new FibonacciHeap<>();
        fh.push(1, 10);
        fh.push(2, 9);
        fh.push(3, 8);
        fh.push(4, 7);
        fh.push(5, 6);
        fh.push(6, 5);
        fh.push(7, 4);
        fh.push(8, 3);
        fh.push(9, 2);
        fh.push(10, 1);

        Assert.assertEquals(10, fh.popMin().intValue());

        fh.decPrio(1, 1);
        fh.decPrio(3, 0);
        fh.decPrio(2, 2);

        Assert.assertEquals(3, fh.popMin().intValue());

    }

    public static void main(String[] args) {
        PriorityQueue<Integer> fh = new FibonacciHeap<>();
        for (int i = 0; i < 65; i++) {
            fh.push(i, i);
        }
        fh.popMin();
    }
}
