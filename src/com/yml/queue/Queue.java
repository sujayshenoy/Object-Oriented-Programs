package com.yml.queue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.yml.linkedlist.Node;

public class Queue<T> implements Iterable<Node<T>> {
    private Node<T> front;
    private Node<T> rear;
    private int size;
    
    public Queue() {
        front = null;
        rear = front;
        size = 0;
    }

    public void enqueue(T data) {
        Node<T> newNode = new Node<T>(data);

        if (rear == null) {
            rear = newNode;
            front = rear;
        } else {
            rear.setNext(newNode);
            rear = newNode;
        }
        size++;
    }
    
    public T dequeue() throws NoSuchElementException {
        T dequed = null;

        if (front == null) {
            throw new NoSuchElementException();
        } else if (front == rear) {
            dequed = front.getData();
            front = null;
            rear = front;
        } else {
            dequed = front.getData();
            front = front.getNext();
        }

        size--;
        return dequed;
    }
    
    public Node<T> getFront() {
        return front;
    }

    public void setFront(Node<T> front) {
        this.front = front;
    }

    public Node<T> getRear() {
        return rear;
    }

    public void setRear(Node<T> rear) {
        this.rear = rear;
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<Node<T>> iterator() {
        return new QueueIterator<T>(front);
    }
    
}
