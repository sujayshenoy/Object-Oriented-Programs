package com.yml.linkedlist;

public class Node<E> {
    E data;
    Node<E> next;

    Node(E data) {
        this.data = data;
        next = null;
    }

    public E getData(){
        return data;
    }
}
