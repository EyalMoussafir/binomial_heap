/**
 * BinomialHeap
 * An implementation of binomial heap over non-negative integers.
 * Based on exercise from previous semester.
 */
public class BinomialHeap
{
	public int size;
	public HeapNode last;
	public HeapNode min;
	
	public BinomialHeap() {
		this.size = 0;
		this.last = null;
		this.min = null;
	}
	public BinomialHeap(HeapNode node) {
		this.size = 1;
		this.last = node;
		this.min = node;
		node.next = node;
	}
	
	
	/**
	 * 
	 * Find and returns the minimum node of the heap.
	 * Complexity: O(log(n)) W.C
	 */
	public HeapNode heapMin(BinomialHeap heap) {
		if(heap.size == 0) {return null;}
		
		HeapNode curr = heap.last.next;
		HeapNode min = heap.last;
		while(curr != heap.last) {
			min = (curr.getKey() < min.getKey()) ? curr : min;
			curr = curr.next;
		}
		return min;
	}
	
	
	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapItem.
	 * Complexity: O(log(n)) W.C
	 */
	public HeapItem insert(int key, String info) 
	{    
		HeapNode node = new HeapNode(key, info);
		BinomialHeap heap2 = new BinomialHeap(node);
		this.meld(heap2);
		return node.item;
	}

	
	/**
	 * 
	 * Delete the minimal item
	 * Complexity: O(log(n)) W.C
	 */
	public void deleteMin()
	{
		BinomialHeap heap = new BinomialHeap();
		if (this.size <= 1) {
			this.size = 0;
			this.last = null;
			this.min = null;
			return;
		}
		this.size--;
		
		if (this.last == this.last.next) {
			this.last = this.last.child;
			this.last.parent = null;
			HeapNode curr = this.last.next;
			
			//set the old minimum children's parent to null. 
			do {
				curr.parent = null;
				curr = curr.next;
			} 
			while(curr != this.last.next);
			
			this.min = heapMin(this);
		}
		
		else {
			HeapNode curr = this.min.next;
			while(curr.next != this.min) {
				curr = curr.next;
			}
			curr.next = this.min.next;
			if (this.last == this.min) {
				this.last = curr;
			}
			
			HeapNode old_min = this.min;
			this.min = heapMin(this);
			
			if(old_min.child != null) {
				heap.size = (int)Math.pow(2, old_min.rank) - 1;
				this.size -= heap.size;
				heap.last = old_min.child;
				heap.last.parent = null;
				heap.min = heapMin(heap);
				
				//set the old minimum children's parent to null. 
				curr = heap.last.next;
				do {
					curr.parent = null;
					curr = curr.next;
				} 
				while(curr != heap.last.next);

				this.meld(heap);
			}
		}
	}

	
	/**
	 * 
	 * Return the minimal HeapItem
	 * Complexity: O(1)
	 */
	public HeapItem findMin()
	{
		if (this.min == null) {
			return null;
		}
		return this.min.item;
	} 

	/**
	 * 
	 * pre: 0 < diff < item.key
	 * 
	 * Decrease the key of item by diff and fix the heap. 
	 * Complexity: O(log(n)) W.C
	 */
	public void decreaseKey(HeapItem item, int diff) 
	{    
		if (item.node == null) {return;}
		
		HeapNode node = item.node;
		item.key -= diff;
		HeapItem temp = null;
		
		while(node.parent != null && node.getKey() < node.parent.getKey()) {
			temp = node.parent.item;
			
			node.parent.item = node.item;
			node.parent.item.node = node.parent;
			
			node.item = temp;
			node.item.node = node;
			
			node = node.parent;
		}
		
		this.min = (this.min.getKey() > node.getKey()) ? node : this.min;
	}

	
	/**
	 * 
	 * Delete the item from the heap.
	 * Complexity: O(log(n)) W.C
	 */
	public void delete(HeapItem item) 
	{    
		this.decreaseKey(item, item.getKey()+1);
		this.deleteMin();
	}

	
	/**
	 * 
	 * Meld the heap with heap2
	 * Complexity: O(log(n)) W.C
	 */
	public void meld(BinomialHeap heap2)
	{
		//if one of the heaps is empty return the other.
		if (heap2.size == 0) {return;}
		if (this.size == 0) {
			this.last = heap2.last;
			this.size = heap2.size;
			this.min = heap2.min;
			return;
		}
		
		HeapNode curr_1 = this.last.next; //pointer to the current tree in this heap.
		HeapNode curr_2 = heap2.last.next; //pointer to the current tree in heap2.
		HeapNode carry = null; //hold the temporary carry in tree melding.
		BinomialHeap res = new BinomialHeap(); //temp heap holding the correct meld result.
		HeapNode res_curr = res.last; //pointer to the last tree of the res heap.
		HeapNode first = null; //pointer to the first tree inserted to res.
		
		HeapNode merge_1 = null; //temp pointer to the tree to be merged in this heap.
		HeapNode merge_2 = null; //temp pointer to the tree to be merged in heap2.
		
		//disconnecting the last node from the first node in both heaps.
		this.last.next = null; 
		heap2.last.next = null;
		
		res.size = this.size + heap2.size; //updating the size of the heap.
		res.min = (this.min.getKey() > heap2.min.getKey()) ? heap2.min : this.min; //updating the minimum of the heap.
		
		
		while (curr_1 != null && curr_2 != null) {
			if (carry == null) {
				//melding same rank trees and putting the result in carry.
				if (curr_1.rank == curr_2.rank) { 
					merge_1 = curr_1;
					merge_2 = curr_2;
					curr_1 = curr_1.next;
					curr_2 = curr_2.next;
					carry = HeapNode.link(merge_1, merge_2, res.min);
				}
				//adding the current tree to res.
				else {
					if (res_curr == null) {
						if (curr_1.rank < curr_2.rank) {
							first = curr_1;
							res.last = curr_1;
							curr_1 = curr_1.next;
							}
						
						else {
							first = curr_2;
							res.last = curr_2;
							curr_2 = curr_2.next;
							}
						res.last.next = res.last;
						res_curr = res.last;
					}
					else {
						if(curr_1.rank < curr_2.rank) {
							res_curr.next = curr_1;
							curr_1 = curr_1.next;
							}
						
						else {
							res_curr.next = curr_2;
							curr_2 = curr_2.next;
							}
						res_curr = res_curr.next;
					}
				}
			}
			//we have a carry.
			else {
				//carry is the only tree of its rank, add carry to res.
				if (carry.rank < curr_1.rank && carry.rank < curr_2.rank) {
					if (res_curr == null) {
						first = carry;
						res.last = carry;
						res_curr = res.last;	
					}
					else {
						res_curr.next = carry;
						res_curr = res_curr.next;
					}
					carry = null;
				}
				//we have 3 trees of same rank, adding one to result and linking the other two into the carry.
				else if (curr_1.rank == curr_2.rank) { 
					if (res_curr == null) {
						first = carry;
						res.last = carry;
						res_curr = res.last;
					}
					else {
						res_curr.next = carry;
						res_curr = res_curr.next;
					}
					merge_1 = curr_1;
					merge_2 = curr_2;
					curr_1 = curr_1.next;
					curr_2 = curr_2.next;
					carry = HeapNode.link(merge_1, merge_2, res.min);
				}
				//we have 2 trees of same rank, linking them into the carry.
				else if (curr_1.rank < curr_2.rank) {
					merge_1 = curr_1;
					curr_1 = curr_1.next;
					carry = HeapNode.link(carry, merge_1, res.min);
				}
				else{
					merge_2 = curr_2;
					curr_2 = curr_2.next;
					carry = HeapNode.link(carry, merge_2, res.min);
				}
			}	
		}
		//we have reached the end of at least one of the heaps.
		if (carry == null){ //adding the remainder of the longer heap to res.
			if(this.size > heap2.size) {
				res_curr.next = curr_1;
				res.last = this.last;
			}
			if(heap2.size > this.size) {
				res_curr.next = curr_2;
				res.last = heap2.last;
			}
		}
		//we have a carry
		else {
			//both heaps ended, add the carry to res.
			if (this.size == heap2.size) { 
				if (res_curr == null) {
					first = carry;
					res.last = carry;
				}
				else {
					res_curr.next = carry;
					res.last = carry;
				}
			}
			//melding the carry with the remainder of the longer heap.
			else {
				HeapNode rem; //pointer to the first node of the remainder.
				HeapNode stop; //pointer to the last node of the remainder.
				if(this.size > heap2.size) {
					rem = curr_1;
					stop = this.last;
					}
				else {
					rem = curr_2;
					stop = heap2.last;
					}
				while (rem != null && carry != null) {
					//we have 2 trees of same rank, linking them into the carry.
					if(carry.rank == rem.rank) {
						merge_1 = rem;
						rem = rem.next;
						carry = HeapNode.link(carry, merge_1, res.min);
					}
					//carry is the only tree of its rank, add carry to res and then the remainder of the heap.
					else {
						if (res_curr == null) {
							first = carry;
							res.last = carry;
							res_curr = carry;
						}
						else {
							res_curr.next = carry;
							res_curr = res_curr.next;
							res.last = carry;
						}
						res_curr.next = rem;
						res.last = stop;
						carry = null;	
					}
				}
				//adding the remainder at the end of res.
				if (carry != null) {
					if (res_curr == null) {
						first = carry;
						res.last = carry;
					}
					else {
						res_curr.next = carry;
						res.last = carry;
					}
				}
			}	
		}
		res.last.next = first;
		//updating this heap to be res.
		this.size = res.size;
		this.min = res.min;
		this.last = res.last;
	}

	
	/**
	 * 
	 * Return the number of elements in the heap
	 * Complexity: O(1)
	 */
	public int size()
	{
		return this.size;
	}

	
	/**
	 * 
	 * The method returns true if and only if the heap
	 * is empty.
	 * Complexity: O(1)
	 */
	public boolean empty()
	{
		return this.size == 0;
	}

	
	/**
	 * 
	 * Return the number of trees in the heap.
	 * Complexity: O(log(n)) W.C
	 */
	public int numTrees()
	{
		if (this.size == 0){
			return 0;
		}
		
		HeapNode curr = this.last.next;
		int counter = 0;
		
		while(curr != this.last) {
			counter++;
			curr = curr.next;
		}
		return counter + 1;
		
	}

	
	/**
	 * Class implementing a node in a Binomial Heap.
	 * 
	 */
	public class HeapNode{
		public HeapItem item;
		public HeapNode child;
		public HeapNode next;
		public HeapNode parent;
		public int rank;
		
		public HeapNode(int key, String info) {
			this.item = new HeapItem(this, key, info);
			this.child = null;
			this.next = null;
			this.parent = null;
			this.rank = 0;
		}
		
		public int getKey() {
			return this.item.getKey();
		}
		
		
		/**
		 * pre: node_1.rank = node_2.rank.
		 * 
		 * Connects two trees with the same rank.
		 * Complexity: O(1)
		 */
		public static HeapNode link(HeapNode node_1, HeapNode node_2, HeapNode min) {
			if (node_1 == null) {
				return node_2;
			}
			else if (node_2 == null) {
				return node_1;
			}
				
			HeapNode small = node_1;
			HeapNode big = node_2;
			if (node_1.getKey() > node_2.getKey()) {
				small = node_2;
				big = node_1;
			}
			
			//if one of the nodes is equal to min of the heap then make sure it is the tree root.
			if (node_1 == min) {
				small = node_1;
				big = node_2;
			}
			if (node_2 == min) {
				small = node_2;
				big = node_1;
			}
			
			if (small.child == null) {
				big.next = big;
			}
			else {
				big.next = small.child.next;
				small.child.next = big;
			}
			small.child = big;
			big.parent = small;
			small.rank++;
			return small;
		}
	}

	
	/**
	 * Class implementing an item in a Binomial Heap.
	 *  
	 */
	public class HeapItem{
		public HeapNode node;
		public int key;
		public String info;
		
		public int getKey() {
			return this.key;
		}
		
		public HeapItem(HeapNode node, int key, String info) {
			this.node = node;
			this.key = key;
			this.info = info;
		}
	}
}
