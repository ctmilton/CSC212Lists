package edu.smith.cs.csc212.adtr.real;

import edu.smith.cs.csc212.adtr.ListADT;
import edu.smith.cs.csc212.adtr.errors.BadIndexError;
import edu.smith.cs.csc212.adtr.errors.EmptyListError;
import edu.smith.cs.csc212.adtr.errors.TODOErr;

/**
 * This is a data structure that has an array inside each node of an ArrayList.
 * Therefore, we only make new nodes when they are full. Some remove operations
 * may be easier if you allow "chunks" to be partially filled.
 * 
 * @author jfoley
 * @param <T> - the type of item stored in the list.
 */
public class ChunkyArrayList<T> extends ListADT<T> {
	private int chunkSize;
	private GrowableList<FixedSizeList<T>> chunks;

	public ChunkyArrayList(int chunkSize) {
		this.chunkSize = chunkSize;
		chunks = new GrowableList<>();
	}
	
	private FixedSizeList<T> makeChunk() {
		return new FixedSizeList<>(chunkSize);
	}

	@Override
	public T removeFront() {
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
		T removed = this.chunks.getFront().removeFront();
		
		//If the first chunk is empty, remove chunk
		if (this.chunks.getFront().isEmpty()) {
			this.chunks.removeFront();
		}
		
		return removed;
	}

	@Override
	public T removeBack() {
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
		T removed = this.chunks.getBack().removeBack();
		
		//If the last chunk is empty, remove chunk
		if (this.chunks.getBack().isEmpty()) {
			this.chunks.removeBack();
		}
		
		return removed;
	}

	@Override
	public T removeIndex(int index) {
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
		int start = 0;
		int count = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();
			
			// Check whether the index should be in this chunk:
			if (start <= index && index < end) {
				T removed = chunk.getIndex(index - start);
				chunk.removeIndex(index - start);
				if (chunk.isEmpty()) {
					this.chunks.removeIndex(count);
				}
				
				return removed;
			}
			
			// update bounds of next chunk.
			start = end;
			count++;
		}
		throw new BadIndexError(index);
	}

	@Override
	public void addFront(T item) {
		if (chunks.isEmpty()) {
			FixedSizeList<T> front = makeChunk();
			chunks.addFront(front);
			front.addIndex(0, item);
			
		} else {
			FixedSizeList<T> front = chunks.getFront();
		
			if (front.isFull()) {
				front = makeChunk();
				chunks.addFront(front);
			}
		
			front.addFront(item);
		}
	}

	@Override
	public void addBack(T item) {
		if (chunks.isEmpty()) {
			FixedSizeList<T> back = makeChunk();
			chunks.addBack(back);
			back.addBack(item);
			
		} else {
			FixedSizeList<T> back = chunks.getBack();
		
			if (back.isFull()) {
				back = makeChunk();
				chunks.addBack(back);
			}
		
			back.addBack(item);
		}
	}

	@Override
	public void addIndex(int index, T item) {
		int chunkIndex = 0;
		int start = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();
			
			// check whether the index should be in this chunk:
			if (start <= index && index <= end) {
				if (chunk.isFull()) {
					// initialize a chunk that comes after the current chunk
					FixedSizeList<T> nextChunk;
					
					// checks whether chunk is the last one or if the next chunk is full
					if (chunks.getBack() == chunk ||
							chunks.getIndex(chunkIndex+1).isFull()) {
						// if the next chunk is full, nextChunk is a new chunk
						nextChunk = makeChunk();
						chunks.addIndex(chunkIndex+1, nextChunk);
					} else {
						// otherwise, nextChunk is the already existing next chunk
						nextChunk = chunks.getIndex(chunkIndex+1);
					}
						
					// if the index is the end value, then put item in nextChunk
					if (index == end) {
						nextChunk.addFront(item);
					} else {
						// otherwise, put the last item in current chunk into nextChunk
						nextChunk.addFront(chunk.getBack());
						chunk.removeBack();
						chunk.addIndex(index - start, item);
					}
					
				} else {
					// put right in this chunk, there's space.
					chunk.addIndex(index - start, item);
				}
				// upon adding, return.
				return;
			}
			
			// update bounds of next chunk.
			start = end;
			chunkIndex++;
		}
		throw new BadIndexError(index);
	}
	
	@Override
	public T getFront() {
		return this.chunks.getFront().getFront();
	}

	@Override
	public T getBack() {
		return this.chunks.getBack().getBack();
	}


	@Override
	public T getIndex(int index) {
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
		int start = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();
			
			// Check whether the index should be in this chunk:
			if (start <= index && index < end) {
				return chunk.getIndex(index - start);
			}
			
			// update bounds of next chunk.
			start = end;
		}
		throw new BadIndexError(index);
	}
	
	@Override
	public void setIndex(int index, T value) {
		if (index < 0 || index >= size()) {
			throw new BadIndexError(index);
		}
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
		int start = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();
			
			// Check whether the index should be in this chunk:
			if (start <= index && index < end) {
				chunk.setIndex(index - start, value);
			}
			
			// update bounds of next chunk.
			start = end;
		}
	}

	@Override
	public int size() {
		int total = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			total += chunk.size();
		}
		return total;
	}

	@Override
	public boolean isEmpty() {
		return this.chunks.isEmpty();
	}
}