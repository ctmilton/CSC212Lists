package edu.smith.cs.csc212.adtr.real;

import edu.smith.cs.csc212.adtr.ListADT;
import edu.smith.cs.csc212.adtr.errors.BadIndexError;
import edu.smith.cs.csc212.adtr.errors.TODOErr;

public class SinglyLinkedList<T> extends ListADT<T> {
	/**
	 * The start of this list.
	 * Node is defined at the bottom of this file.
	 */
	Node<T> start;
	
	@Override
	public T removeFront() {
		checkNotEmpty();
		T removed = this.start.value;
		this.start = start.next;
		
		return removed;
	}

	@Override
	public T removeBack() {
		checkNotEmpty();
		T removed = null;
		
		if (size() == 1) {
			removed = getIndex(0);
			this.start = null;
		} else {
			for (Node<T> n = this.start; n != null; n = n.next) {
				if (n.next == null) {
					removed = n.value;
					for (Node<T> l = this.start; l != null; l = l.next) {
						if (l.next == n) {
							l.next = null;
						}
					}
				}
			}
		}
		
		return removed;
	}

	@Override
	public T removeIndex(int index) {
		checkNotEmpty();
		T removed = this.getIndex(index);
		
		//Connect the previous node to the next node and return the value of removed node
		
		
		
		return removed;
	}

	@Override
	public void addFront(T item) {
		this.start = new Node<T>(item, start);
	}

	@Override
	public void addBack(T item) {
		
		 Node<T> lastNodeInList = null;
		 
		 for (Node<T> current = this.start; current != null; current = current.next) {
			 lastNodeInList = current;
		 }
		 
		 if (this.isEmpty()) {
			 this.addFront(item);
			 } else {
				 lastNodeInList.next = new Node<T>(item, null);
			 }
	}

	@Override
	public void addIndex(int index, T item) {
		throw new TODOErr();
	}
	
	
	
	@Override
	public T getFront() {
		checkNotEmpty();
		return getIndex(0);
	}

	@Override
	public T getBack() {
		checkNotEmpty();
		int lastIndex = size() -  1;
		return getIndex(lastIndex);
	}

	@Override
	public T getIndex(int index) {
		checkNotEmpty();
		int at = 0;
		for (Node<T> n = this.start; n != null; n = n.next) {
			if (at++ == index) {
				return n.value;
			}
		}
		throw new BadIndexError(index);
	}
	

	@Override
	public void setIndex(int index, T value) {
		checkNotEmpty();
		this.checkExclusiveIndex(index);
		
		int at = 0;
		for (Node<T> n = this.start; n != null; n = n.next) {
			if (at++ == index) {
				n.value = value;
			}
		}
	}

	@Override
	public int size() {
		int count = 0;
		for (Node<T> n = this.start; n != null; n = n.next) {
			count++;
		}
		return count;
	}

	@Override
	public boolean isEmpty() {
		return this.start == null;
	}
	
	/**
	 * The node on any linked list should not be exposed.
	 * Static means we don't need a "this" of SinglyLinkedList to make a node.
	 * @param <T> the type of the values stored.
	 */
	private static class Node<T> {
		/**
		 * What node comes after me?
		 */
		public Node<T> next;
		/**
		 * What value is stored in this node?
		 */
		public T value;
		/**
		 * Create a node with no friends.
		 * @param value - the value to put in it.
		 */
		public Node(T value, Node<T> next) {
			this.value = value;
			this.next = next;
		}
	}

}
