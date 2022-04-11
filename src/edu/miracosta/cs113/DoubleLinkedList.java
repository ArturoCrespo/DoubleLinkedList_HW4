package edu.miracosta.cs113;

import java.util.AbstractSequentialList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class DoubleLinkedList<E> extends AbstractSequentialList<E> {

    // data fields
    private Node<E> head = null ;
    private Node<E> tail = null ;

    private int size = 0 ;

    /**
     * Add an item at the specified index.
     * @param index      The index at which the object is to be inserted.
     * @param obj        The object to be inserted.
     * @throws IndexOutOfBoundsException
     *      if the index is out of range (i < 0 || i > size).
     */
    public void add(int index, E obj) {
        listIterator(index).add(obj) ;
    }

    /**
     * Get the element at position index.
     * @param index     Position of item to be retrieved.
     * @return          The item at index.
     * @throws          IndexOutOfBoundsException if invalid index is provided: index < 0 OR index >= size of list
     */
    public E get(int index) {

        if(index < 0 || index >= size) {
            throw new
                    IndexOutOfBoundsException("Invalid index: indices between only 0 and " + size + " allowed." ) ;
        }
        Node<E> accessedNode = getNode(index) ;
        return accessedNode.data ;
    }

    /**
     * Inserts item as first element of the list.
     * @param item      The item to be inserted.
     */
    public void addFirst(E item) {
        DoubleListIterator listIterator = new DoubleListIterator(0) ;
        listIterator.set(item);
        size++;
    }

    /**
     * Adds item to the end of the list.
     * @param item      The item to be added as last element of the list.
     */
    public void addLast(E item) {
        Node<E> newNode = new Node<E>(item) ;
        newNode.prev = tail ;
        tail.next = newNode ;
        newNode = tail ;
        newNode.next = null ;
        size++;
    }

    /**
     * Accesses first element in the list.
     * @return      data at the head of the list.
     */
    public E getFirst() {
        return head.data ;
    }

    /**
     * Accesses last element in the list.
     * @return      Data at the tail of the list.
     */
    public E getLast() {
        return tail.data ;
    }

    /**
     * Traverses through list beginning at the head and returns node at the index provided.
     * @param index     Specified node location provided to access.
     * @return          Node at specified location.
     */
    private Node<E> getNode(int index) {

        Node<E> currentNode = head ;

        for(int i = 0 ; i < index && currentNode != null ; i++ ) {
            currentNode = currentNode.next ;
        }
        return currentNode ;
    }

    @Override
    public ListIterator<E> listIterator(int index)
    { return new DoubleListIterator(index); }

    public Iterator<E> iterator()
    { return new DoubleListIterator(0) ; }

    public ListIterator<E> listIterator()
    { return new DoubleListIterator(0) ; }

    @Override
    public int size() {
        return size;
    }

    public void clear() {
        head = null ;
        tail = null ;
        size = 0 ;
    }


    public boolean isEmpty() {
        return head == null ;
    }

    public void print() {
        Node current = head ;

        while(current != null) {
            System.out.print(current.data + " ") ;
            current = current.next ;
        }
    }

    // INNER CLASSES

    private static class Node<E> {
        private E data ;
        private Node<E> next = null ;
        private Node<E> prev = null ;

        private Node(E dataItem) {
            data = dataItem ;
            next = null ; //maybe remove
            prev = null ; // maybe remove
        }

        private Node(E dataItem, Node<E> nodeRefNext, Node <E> nodeRefPrev) {
            data = dataItem ;
            next = nodeRefNext ;
            prev = nodeRefPrev ;
        }
    } // end of static inner class Node



    private class DoubleListIterator implements ListIterator<E> {

        private Node<E> nextItem ;
        private Node<E> lastItemReturned ;
        private int index = 0 ;

        /**
         * Iterator constructor to iterate to a specified location, checks for valid locations.
         * @param i     Specified position to for iterator to reach.
         */
        public DoubleListIterator(int i) {
            // error check index
            if(i < 0 || i > size) {
                throw new IndexOutOfBoundsException("Invalid index provided: " + i) ;
            }

            // no item returned yet
            lastItemReturned = null ;

            // last element in list case
            if(i == size) {
                index = size ;
                nextItem = null ;

                //Starting at beginning of list
            } else {
                nextItem = head ;

                for(index = 0 ; index < i ; index++) {
                    nextItem = nextItem.next ;
                }
            }
        }

        /**
         * Tests to see if nextItem to iterate through is null.
         * @return      true if not null, false otherwise.
         */
        public boolean hasNext() {
            return nextItem != null ;
        }

        /**
         * Provides next item in at current location in list and increments iterator to next position.
         * @return      the following item from provided spot in the list.
         */
        public E next() {
            if(!hasNext()) {
                throw new NoSuchElementException() ;
            }
            lastItemReturned = nextItem ;
            nextItem = nextItem.next ;
            index++ ;
            return lastItemReturned.data ;
        }

        /**
         * Identifies if current spot on list has a previous location, otherwise at the beginning of the list.
         * @return      boolean true if there are predecessors, false if at beginning of the list.
         */
        public boolean hasPrevious() {
            if(size == 0) {
                return false ;
            }
            return ((nextItem == null && size != 0)
                    || (nextItem.prev != null)) ;
        }

        /**
         * Provides previous item at current location in list and decrements iterator to previous position.
         * @return      the prior item from provided spot in the list.
         */
        public E previous() {
            if(!hasPrevious()) {
                throw new NoSuchElementException() ;
            }

            // iterator past the last element
            if (nextItem == null) {
                nextItem = tail ;
            } else {
                nextItem = nextItem.prev ;
            }
            lastItemReturned = nextItem ;
            index-- ;
            return lastItemReturned.data;
        }

        /**
         * Adds object to list addressing four cases: Adding to an empty list, adding to the head of the list, add to
         * the tail of the list, and add anywhere in the "middle" of the list.
         * @param obj       object to be added to the list.
         */
        public void add(E obj) {

            // add to an empty list
            if(head == null) {
                head = new Node<E>(obj);
                tail = head ;
                // add to head of list
            } else if(nextItem == head) {
                Node<E> newNode = new Node<E>(obj) ;
                newNode.next = nextItem ;
                nextItem.prev = newNode ;
                head = newNode ;

                // add to tail of the list
            } else if(nextItem == null) {
                Node<E> newNode = new Node<E>(obj) ;
                tail.next = newNode ;
                newNode.prev = tail ;
                tail = newNode ;

                //add element anywhere in the list (middle)
            } else {
                Node<E> newNode = new Node<E>(obj) ;
                newNode.prev = nextItem.prev ;
                nextItem.prev.next = newNode ;
                newNode.next = nextItem ;
                nextItem.prev = newNode ;
            }

            size++ ;
            index++ ;
            lastItemReturned = null ;
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            if(lastItemReturned == null) {
                throw new IllegalStateException() ;
            }

            if(lastItemReturned == head) {
                head = nextItem ;
                head.prev = null ;
            } else if(lastItemReturned == tail) {
                lastItemReturned.prev.next = null ;
                tail = lastItemReturned.prev ;
                tail.next = null ;
            } else if(lastItemReturned != head && lastItemReturned != tail) {
                lastItemReturned.next.prev = lastItemReturned.prev ;
                lastItemReturned.prev.next = lastItemReturned.next ;
            }

            size-- ;
            index-- ;
        }

        @Override
        public void set(E item) {
            if(lastItemReturned != null) {
                lastItemReturned.data = item ;
            } else {
                throw new IllegalStateException();
            }
        }

    } // end of inner class DoubleListIterator

    public static void main(String[] args) {
        DoubleLinkedList dll = new DoubleLinkedList() ;
        dll.addFirst(5);
        dll.addFirst(10);
        dll.print();
    } // end of Driver

} // end of class DoubleLinkedList

