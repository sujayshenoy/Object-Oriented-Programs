package com.yml.stack;

import java.util.Iterator;

import com.yml.linkedlist.Node;

public class StackIterator <T> implements Iterator<Node<T>> {
    Node<T> current;

    public StackIterator(Node<T> top) {
        current = top;
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public Node<T> next() {
        Node<T> temp = current;
        current = current.getNext();
        return temp;
    }
    
}
