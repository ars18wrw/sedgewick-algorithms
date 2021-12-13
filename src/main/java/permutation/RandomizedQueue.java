package permutation;

import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final String TEST_STRING = "0123456789";
    private static final int INITIAL_CAPACITY = 1;

    private Item[] array;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        array = (Item[]) new Object[INITIAL_CAPACITY];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return null == array[0];
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (null == item) {
            throw new IllegalArgumentException();
        }
        if (size == array.length) {
            increaseSize();
        }
        array[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (0 == size) {
            throw new NoSuchElementException();
        }
        int indexToRemove = getRandomInt(size);
        Item toReturn = array[indexToRemove];
        array[indexToRemove] = array[size - 1];
        array[size - 1] = null;
        size--; // it could have been done with decrement, but this way the code seems more readable
        if (size <= array.length / 4) {
            decreaseSize();
        }
        return toReturn;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (0 == size) {
            throw new NoSuchElementException();
        }
        int toRemoveIndex = getRandomInt(size);
        return array[toRemoveIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ItemIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<String>();
        assert randomizedQueue.isEmpty();

        for (int i = 0; i < TEST_STRING.length(); i++) {
            randomizedQueue.enqueue(Character.toString(TEST_STRING.charAt(i)));
        }
        assert TEST_STRING.length() == randomizedQueue.size();
        for (int i = 0; i < 10; i++) {
            Iterator<String> iter1 = randomizedQueue.iterator();
            while (iter1.hasNext()) {
                System.out.print(iter1.next());
            }
            System.out.println();
        }
        for (int i = 0; i < TEST_STRING.length(); i++) {
            randomizedQueue.sample();
            randomizedQueue.dequeue();
        }
        assert randomizedQueue.isEmpty();
    }

    private void increaseSize() {
        Item[] copyArray = (Item[]) new Object[array.length * 2];
        for (int i = 0; i < array.length; i++) {
            copyArray[i] = array[i];
        }
        array = copyArray;
    }

    private void decreaseSize() {
        if (array.length > 1) {
            Item[] copyArray = (Item[]) new Object[array.length / 2];
            for (int i = 0; i < array.length / 2; i++) {
                copyArray[i] = array[i];
            }
            array = copyArray;
        }
    }

    private class ItemIterator implements Iterator<Item> {

        private int processed;
        private Item[] items;

        public ItemIterator() {
            items = (Item[]) new Object[array.length];
            for (int i = 0; i < items.length; i++) {
                items[i] = array[i];
            }
            processed = 0;
        }

        public boolean hasNext() {
            return size != processed;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int randomIndex = getRandomInt(size - processed);
            Item item = items[randomIndex];

            items[randomIndex] = items[size - 1 - processed];
            items[size - 1 - processed] = item;
            processed++;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static int getRandomInt(int upperBound) {
        return StdRandom.uniform(upperBound);
    }
}