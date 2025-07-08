package com.Daryappa.Inventory.ds;

import java.util.ArrayList;
import java.util.List;

public class PriorityQueue<T> {
    private static class Node<T>{
        T item;
        int priority;
        Node(T item, int priority){
            this.item= item;
            this.priority=priority;
        }
    }
    public boolean isEmpty(){
        return heap.isEmpty();
    }
    private List<Node<T>> heap;
    public PriorityQueue(){
        this.heap= new ArrayList<>();
    }
    public void add(T item, int priority){
        Node<T> newNode= new Node<>(item,priority);
        heap.add(newNode);
        heapifyUp(heap.size()-1);
    }

    public T poll() {
        if (heap.isEmpty()) return null;

        T result = heap.get(0).item;
        Node<T> last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }
        return result;
    }
    
    public void heapifyUp(int index){
        while(index>0){
            int parentIdx= (index-1)/2;
            if(heap.get(index).priority>= heap.get(parentIdx).priority) break;

            Node<T> temp = heap.get(index);
            heap.set(index, heap.get(parentIdx));
            heap.set(parentIdx, temp);

            index=parentIdx;
        }
    }
    public void heapify(List<T> items, List<Integer> priorities) {
        heap.clear(); // Clear existing heap
        for (int i = 0; i < items.size(); i++) {
            heap.add(new Node<>(items.get(i), priorities.get(i)));
        }

        // Start heapifyDown from the last parent node to root
        for (int i = (heap.size() / 2) - 1; i >= 0; i--) {
            heapifyDown(i);
        }
    }

    private void heapifyDown(int index){
        int size = heap.size();
        while(index<size){
            int left= 2*index+1;
            int right = 2*index+2;
            int smallest= index;
            if(left<size && heap.get(left).priority < heap.get(smallest).priority){
                smallest=left;
            }
            if(right<size && heap.get(right).priority < heap.get(smallest).priority){
                smallest=right;
            }
            if (smallest == index) break;
            Node<T> temp = heap.get(index);
            heap.set(index, heap.get(smallest));
            heap.set(smallest,temp);

            index=smallest;

        }
    }

}
