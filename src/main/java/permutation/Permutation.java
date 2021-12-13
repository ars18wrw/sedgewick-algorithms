package permutation;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        int count = 0;

        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<String>();
        String input;
        while (!StdIn.isEmpty()) {
            count++;
            input = StdIn.readString();
            if (count <= k) {
                randomizedQueue.enqueue(input);
            } else {
                int randomIndex = StdRandom.uniform(count);
                if (randomIndex < k) {
                    randomizedQueue.dequeue();
                    randomizedQueue.enqueue(input);
                }
            }
        }
        for (String str : randomizedQueue) {
            StdOut.println(str);
        }
    }
}