package com.yml.linkedlist;

public class Node<E> {
    E data;
    Node<E> next;

    public Node(E data) {
        this.data = data;
        next = null;
    }

    public E getData() {
        return data;
    }
    
    public void setData(E data) {
        this.data = data;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }

    public Node<E> getNext() {
        return next;
    }
}
