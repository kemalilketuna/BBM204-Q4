import java.io.*;
import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children;
    boolean isEndOfWord;
    char c;
    long val;

    TrieNode(char c) {
        children = new HashMap<>();
        isEndOfWord = false;
        this.c = c;
    }

    TrieNode(){
        children = new HashMap<>();
        isEndOfWord = false;
    }

    public void insert(String word, long val) {
        TrieNode current = this;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            TrieNode node = current.children.get(c);
            if (node == null) {
                node = new TrieNode(c);
                current.children.put(c, node);
            }
            current = node;
        }
        current.isEndOfWord = true;
        current.val = val;
    }
}

class Entry implements Comparable<Entry> {
    long priority;
    String name;

    public Entry(long priority, String name) {
        this.priority = priority;
        this.name = name;
    }

    @Override
    public int compareTo(Entry other) {
        int res = -Long.compare(priority, other.priority);
        if (res == 0) {
            res = name.compareTo(other.name);
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
        while (n-- > 0 && (line = reader.readLine()) != null){
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

    private void dfs(TrieNode node, String prefix, PriorityQueue<Entry> pq) {
        if (node.isEndOfWord) {
            pq.add(new Entry(node.val, prefix));
        }
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            dfs(entry.getValue(), prefix + entry.getKey(), pq);
        }
    }

    private void search(String word, int limit) {
        // Query received: "ab" with limit 1. Showing results:
        System.out.println("Query received: \"" + word + "\" with limit " + limit + ". Showing results:");

        PriorityQueue<Entry> pq = new PriorityQueue<>();
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            TrieNode node = current.children.get(c);
            if (node == null) {
                System.out.println("No results.");
                return;
            }
            current = node;
        }

        // DFS to current node
        dfs(current, word, pq);

        if (pq.isEmpty() || limit == 0) {
            System.out.println("No results.");
            return;
        }

        // Print results
        while ((limit-- > 0) && !pq.isEmpty()) {
            System.out.println(pq.poll());
        }
    }

    public static void main(String[] args) throws IOException {
        Quiz4 quiz = new Quiz4();
        quiz.readDatabase(args[0]);
        quiz.readQueries(args[1]);
    }
}
