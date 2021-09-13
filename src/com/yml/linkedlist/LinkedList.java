package com.yml.linkedlist;

import java.util.Iterator;

public class LinkedList<T> implements Iterable<Node<T>> {
    Node<T> head;

    public LinkedList() {
        head = null;
    }

    public void add(T data) {
        if (head == null) {
            head = new Node<T>(data);
            return;
        }

        Node<T> newNode = new Node<T>(data);
        Node<T> temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = newNode;
    }

    public void remove(T data) throws IllegalStateException{
        if (head == null) {
            throw new IllegalStateException("Linked list empty");
        }

        Node<T> temp = head;
        Node<T> prev = temp;

        if (head.data == data) {
            head = head.next;
            return;
        }

        while (temp != null) {
            if (temp.data == data) {
                prev.next = temp.next;
                break;
            }
            prev = temp;
            temp = temp.next;
        }
    }

    public Node<T> get(int index) throws IndexOutOfBoundsException {
        int count = 0;
        if (head == null) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> temp = head;
        while (count != index) {
            temp = temp.next;
            if (temp == null) {
                throw new IndexOutOfBoundsException();
            }
            count++;
        }

        return temp;
    }

    @Override
    public Iterator iterator() {
        return new LinkedListIterator<T>(head);
    }
}
