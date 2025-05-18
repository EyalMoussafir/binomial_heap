# BinomialHeap

A Java implementation of a non-lazy binomial heap.  

Supports the following operations (all methods return integers only when noted):

- **Constructor**  
  `BinomialHeap()`  
  Create an empty binomial heap.

- **Insertion**  
  `insert(key, info)`  
  Insert a new element with the given key and associated info; returns a reference to the new node.

- **Find Minimum**  
  `find_min()`  
  Return the node with the smallest key, or `null` if the heap is empty.

- **Delete Minimum**  
  `delete_min()`  
  Remove the node with the minimum key from the heap.

- **Decrease Key**  
  `decrease_key(node, delta)`  
  Decrease the key of the specified node by `delta` and restore heap order.

- **Delete**  
  `delete(node)`  
  Remove the specified node from the heap entirely.

- **Meld (Union)**  
  `meld(other_heap)`  
  Merge this heap with another `other_heap`; afterward, `other_heap` must not be used.

- **Size**  
  `size()`  
  Return the total number of nodes.
