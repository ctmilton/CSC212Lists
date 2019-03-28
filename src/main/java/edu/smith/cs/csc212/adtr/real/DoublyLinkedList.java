package edu.smith.cs.csc212.adtr.real;

import edu.smith.cs.csc212.adtr.ListADT;
import edu.smith.cs.csc212.adtr.errors.BadIndexError;
import edu.smith.cs.csc212.adtr.errors.TODOErr;

public class DoublyLinkedList<T> extends ListADT<T> {
	private Node<T> start;
	private Node<T> end;
	
	/**
	 * A doubly-linked list starts empty.
	 */
	public DoublyLinkedList() {
		this.start = null;
		this.end = null;
	}
	

	@Override
	public T removeFront() {
		checkNotEmpty();
		T removed = this.start.value;
		
		if (start.after == null) {
			this.start = null;
		} else {
			this.start = start.after;
		}
		
		return removed;
	}

	@Override
	public T removeBack() {
		checkNotEmpty();
		
		T removed = this.end.value;
		
		if (this.start == this.end) {
			removeFront();
		} else {
			Node<T> secondLast = this.end.before;
			secondLast.after = null;
			this.end = secondLast;
		}
		
		return removed;
		
	}

	@Override
	public T removeIndex(int index) {
		checkNotEmpty();
		if (index >= size() || index < 0) {
			throw new BadIndexError(index);
		}
		T removed = this.getIndex(index);
		Node<T> current = new Node<T> (null);
		Node<T> next = new Node<T> (null);
		
		if (index == 0) {
			removeFront();
		} else {
			int at = 0;
			for (Node<T> n = start; n != null; n = n.after) {
				if (at++ == index) {
					current = n;
				}
			}
			if (current.after == null) {
				removeBack();
			} else {
				for (Node<T> later = start; later != null; later = later.after) {
					if (later.after == current) {
						next = later.after.after;
						later.after = next;
						next.before = later;
					}
				}
			}
		}
		return removed;
	}

	@Override
	public void addFront(T item) {
		if (isEmpty()) {
			this.start = new Node<T>(item);
		} else {
			Node<T> second = start;
			this.start = new Node<T>(item);
			start.after = second;
			second.before = start;
		}
	}

	@Override
	public void addBack(T item) {
		if (end == null) {
			start = end = new Node<T>(item);
		} else {
			Node<T> secondLast = end;
			end = new Node<T>(item);
			end.before = secondLast;
			secondLast.after = end;
		}
	}

	@Override
	public void addIndex(int index, T item) {
		if (index > size() || index < 0) {
			throw new BadIndexError(index);
		}
		Node<T> current = new Node<T> (null);
		Node<T> newNode = new Node<T> (item);
		
		if (isEmpty() || index==0) {
			addFront(item);
		} else {
			int at = 0;
			Node<T> n = this.start;
			for (; n != null; n = n.after) {
				if (at++ == index) {
					current = n;
					break;
				}
			}
			
			if (n == null) {
				addBack(item);
			} else {
				for (Node<T> later = start; later != null; later = later.after) {
					if (later.after == current) {
						later.after = newNode;
						newNode.before = later;
						newNode.after = current;
						current.before = newNode;
						break;
					}
				}
			}			
		}
	}

	@Override
	public T getFront() {
		checkNotEmpty();
		return this.start.value;
	}

	@Override
	public T getBack() {
		checkNotEmpty();
		return this.end.value;
	}
	
	@Override
	public T getIndex(int index) {
		checkNotEmpty();
		int at = 0;
		for (Node<T> n = this.start; n != null; n = n.after) {
			if (at++ == index) {
				return n.value;
			}
		}
		throw new BadIndexError(index);
	}
	
	public void setIndex(int index, T value) {
		checkNotEmpty();
		if (index < 0 || index >= size()) {
			throw new BadIndexError(index);
		}
		int at = 0;
		for (Node<T> n = this.start; n != null; n = n.after) {
			if (at++ == index) {
				n.value = value;
			}
		}
	}

	@Override
	public int size() {
		int count = 0;
		for (Node<T> n = this.start; n != null; n = n.after) {
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
	 * Static means we don't need a "this" of DoublyLinkedList to make a node.
	 * @param <T> the type of the values stored.
	 */
	private static class Node<T> {
		/**
		 * What node comes before me?
		 */
		public Node<T> before;
		/**
		 * What node comes after me?
		 */
		public Node<T> after;
		/**
		 * What value is stored in this node?
		 */
		public T value;
		/**
		 * Create a node with no friends.
		 * @param value - the value to put in it.
		 */
		public Node(T value) {
			this.value = value;
			this.before = null;
			this.after = null;
		}
	}
}
