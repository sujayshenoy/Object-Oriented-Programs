package com.yml.queue;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.yml.linkedlist.Node;

public class Queue<T> implements Iterable<Node<T>> {
    Node<T> front;
    Node<T> rear;
    
    public Queue() {
        front = null;
        rear = front;
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

    }
    
    public T dequeue() throws NoSuchElementException {
        T dequed = null;

        if (front == null) {
            throw new NoSuchElementException();
        }
        else if (front == rear) {
            dequed = front.getData();
            front = null;
            rear = front;
        }
        else {
            dequed = front.getData();
            front = front.getNext();
        }

        return dequed;
    }

    @Override
    public Iterator<Node<T>> iterator() {
        return new QueueIterator<T>(front);
    }
    
}
