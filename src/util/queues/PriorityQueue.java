package src.util.queues;

import java.util.Set;

public interface PriorityQueue<E> {
	
	/**
     * Adds an element to the heap.
     * @param x The element to be added
     * @param priority The priority of the element
	 * @return Returns true on successful call
     */
	boolean push(E e, int prio);

	/**
     * Removes and retrieves the head of the heap.
     * @return The elements with the smalles priority from this queue.
     */
	E popMin();

	/**
     * Returns the head of the queue without removing it. This will not affect
     * the internal order of the heap.
     * @return The element with the lowest priority
     */
	E peek();

	/**
     * Returns whether the heap is empty. The elements are not ordered.
     * @return True if heap is empty
     */
	boolean isEmpty();

	/**
     * Returns the current amount of elements inside the heap.
     * @return the size of the heap
     */
	int size();

	/**
     * Decreases the priority of the given element in the queue.
     * @param v The element to reduce the priority of.
     * @param prio The priority
     */
	void decPrio(E e, int prio);

	/**
     * Returns a view of the contents of the heap.
     * Changes made to this List will be reflected in the Heap.
     * @return A unmodifiable view of the contents.
     */
	Set<E> contents();
}
