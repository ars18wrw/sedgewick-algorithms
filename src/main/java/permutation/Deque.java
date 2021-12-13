package permutation;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private static final String TEST_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private Node first;
    private Node last;

    private int size = 0;

    // construct an empty deque
    public Deque() {
        // Empty constructor
    }

    // is the deque empty?
    public boolean isEmpty() {
        return null == first;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (null == item) {
            throw new IllegalArgumentException();
        }
        Node toAdd = new Node(item);
        if (isEmpty()) {
            last = toAdd;
        } else {
            first.setPrevious(toAdd);
            toAdd.setNext(first);
        }
        first = toAdd;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (null == item) {
            throw new IllegalArgumentException();
        }
        Node toAdd = new Node(item);
        if (isEmpty()) {
            first = toAdd;
        } else {
            last.setNext(toAdd);
            toAdd.setPrevious(last);
        }
        last = toAdd;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } else {
            Node toReturn = first;
            Node second = first.getNext();
            // one element only
            if (null == second) {
                first = null;
                last = null;
            } else {
                second.setPrevious(null);
                first = second;
            }
            size--;
            return toReturn.getContent();
        }
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        } else {
            Node toReturn = last;
            Node second = last.getPrevious();
            // one element only
            if (null == second) {
                first = null;
                last = null;
            } else {
                second.setNext(null);
                last = second;
            }
            size--;
            return toReturn.getContent();
        }
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ItemIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        assert deque.isEmpty();

        int middle = TEST_STRING.length() / 2;
        int i = middle - 1;
        int j = middle;
        while (i >= 0 && j < TEST_STRING.length()) {
            deque.addFirst(Character.toString(TEST_STRING.charAt(i)));
            deque.addLast(Character.toString(TEST_STRING.charAt(j)));
            i--;
            j++;
            assert deque.size() == j - i + 1;
        }
        for (String item : deque) {
            System.out.println(item);
        }
        while (i < middle && j > middle) {
            deque.removeFirst();
            deque.removeLast();
            i++;
            j--;
            assert deque.size() == j - i + 1;
        }
        assert deque.isEmpty();
    }

    private class ItemIterator implements Iterator<Item> {

        private Node current;

        public ItemIterator() {
            current = first;
        }

        public boolean hasNext() {
            return null != current;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = current.getContent();
            current = current.getNext();
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class Node {
        private Node next;
        private Node previous;
        private final Item content;

        public Node(Item content) {
            this.content = content;
        }

        public void setNext(Node aNext) {
            this.next = aNext;
        }

        public void setPrevious(Node aPrevious) {
            this.previous = aPrevious;
        }

        public Node getNext() {
            return next;
        }

        public Node getPrevious() {
            return previous;
        }

        public Item getContent() {
            return content;
        }
    }
}