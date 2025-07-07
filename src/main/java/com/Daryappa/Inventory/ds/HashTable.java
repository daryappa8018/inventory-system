package com.Daryappa.Inventory.ds;

import com.Daryappa.Inventory.model.InventoryRecord;


import java.util.*;

public class HashTable<K,V> {
    public final static int DFAULT_SIZE=16;
    private static class Node<K, V> {
        K key;
        V value;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

    }

    private List<Node<K, V>>[] buckets;
    private int capacity;
    private int size;

    public HashTable() {
        this.capacity = 16;// the default capacity of 16
        this.buckets = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<>();
        }
        this.size = 0;
    }
    public int getCapacity(){
        return this.capacity;
    }

    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    public void put(K key, V value) {
        int index = getBucketIndex(key);
        List<Node<K, V>> bucket = buckets[index];
        for (Node<K, V> node : bucket) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
        }

        bucket.add(new Node<>(key, value));
        size++;
    }



    public V get(K key) {
        int index = getBucketIndex(key);
        List<Node<K, V>> bucket = buckets[index];
        for (Node<K, V> node : bucket) {
            if (node.key.equals(key)) {

                return node.value;
            }
        }
        return null; // in case we dont find the key in the linked list
    }

    public void remove(K key) {
        int index = getBucketIndex(key);
        List<Node<K, V>> bucket = buckets[index];
        for (Node<K, V> node : bucket) {
            if (node.key.equals(key)) {
                bucket.remove(node);
                size--;
                return;
            }
        }
    }

    public boolean containsKey(K key) {
        int index = getBucketIndex(key);
        List<Node<K, V>> bucket = buckets[index];

        for (Node<K, V> node : bucket) {
            if (node.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public List<K> keySet() {
        List<K> keys = new LinkedList<>();
        for (List<Node<K, V>> bucket : buckets) {
            for (Node<K, V> node : bucket) {
                keys.add(node.key);
            }
        }
    return keys;
    }
}
