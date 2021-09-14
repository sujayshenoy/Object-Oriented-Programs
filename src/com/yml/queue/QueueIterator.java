package com.yml.queue;

import java.util.Iterator;
import com.yml.linkedlist.Node;

public class QueueIterator<T> implements Iterator<Node<T>>{
    Node<T> current;

    QueueIterator(Node<T> front) {
        current = front;
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
