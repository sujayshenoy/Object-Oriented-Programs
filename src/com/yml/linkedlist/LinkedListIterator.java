package com.yml.linkedlist;

import java.util.Iterator;

public class LinkedListIterator <T> implements Iterator<Node<T>> {
    Node<T> current;

    LinkedListIterator(Node<T> start) {
        this.current = start;
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public Node<T> next() {
        Node<T> temp = current;
        current = current.next;
        return temp;
    }
    
}
