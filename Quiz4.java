import java.io.*;
import java.util.*;

class TrieNode {
    TrieNode[] children;
    boolean isEndOfWord;
    long val;

    TrieNode() {
        children = new TrieNode[26];  // Assuming only lowercase letters a-z
        isEndOfWord = false;
    }

    public void insert(String word, long val) {
        TrieNode current = this;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = c - 'a';
            if (current.children[index] == null) {
                current.children[index] = new TrieNode();
            }
            current = current.children[index];
        }
        current.isEndOfWord = true;
        current.val = val;
    }
}

class Item implements Comparable<Item> {
    long priority;
    String name;

    public Item(long priority, String name) {
        this.priority = priority;
        this.name = name;
    }

    @Override
    public int compareTo(Item other) {
        int res = Long.compare(priority, other.priority);
        if (res == 0) {
            res = -name.compareTo(other.name);
        }
        return res;
    }

    @Override
    public String toString() {
        return "- " + priority + " " + name;
    }
}

public class Quiz4 {
    private TrieNode root = new TrieNode();

    private void readDatabase(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = reader.readLine();
        int n = Integer.parseInt(line.trim());
        while (n-- > 0 && (line = reader.readLine()) != null) {
            String[] parts = line.split("\t");
            root.insert(parts[1].toLowerCase(), Long.parseLong(parts[0]));
        }
        reader.close();
    }

    private void readQueries(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\t");
            search(parts[0].toLowerCase(), Integer.parseInt(parts[1]));
        }
        reader.close();
    }

    private void dfs(TrieNode node, StringBuilder prefix, int limit, PriorityQueue<Item> pq) {
        if (node.isEndOfWord) {
            pq.add(new Item(node.val, prefix.toString()));
            if (pq.size() > limit) {
                pq.poll(); // Keep only the top `limit` elements
            }
        }
        
        for (int i = 0; i < 26; i++) {
            if (node.children[i] != null) {
                prefix.append((char) (i + 'a'));
                dfs(node.children[i], prefix, limit, pq);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
    }

    private void search(String word, int limit) {
        System.out.println("Query received: \"" + word + "\" with limit " + limit + ". Showing results:");

        if (limit == 0) {
            System.out.println("No results.");
            return;
        }

        PriorityQueue<Item> pq = new PriorityQueue<>();
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = c - 'a';
            if (current.children[index] == null) {
                System.out.println("No results.");
                return;
            }
            current = current.children[index];
        }

        // Perform DFS to find results
        dfs(current, new StringBuilder(word), limit, pq);

        if (pq.isEmpty()) {
            System.out.println("No results.");
            return;
        }
        
        List<Item> results = new ArrayList<>(pq);
        results.sort(Collections.reverseOrder());  // Highest priorities first

        // Print results
        for (Item item : results) {
            if (limit-- <= 0) break;
            System.out.println(item);
        }
    }

    public static void main(String[] args) throws IOException {
        Quiz4 quiz = new Quiz4();
        quiz.readDatabase(args[0]);
        quiz.readQueries(args[1]);
    }
}
