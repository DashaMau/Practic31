import java.io.*;
import java.util.Scanner;

class Node {
    int key1, key2;
    String processorName;
    double clockFrequency;
    int cacheSize;
    double busFrequency;
    int specInt;
    int specFp;
    Node left, middle, right;
    boolean isLeaf;

    Node(int key1, String processorName, double clockFrequency, int cacheSize, double busFrequency, int specInt, int specFp) {
        this.key1 = key1;
        this.processorName = processorName;
        this.clockFrequency = clockFrequency;
        this.cacheSize = cacheSize;
        this.busFrequency = busFrequency;
        this.specInt = specInt;
        this.specFp = specFp;
        this.left = null;
        this.middle = null;
        this.right = null;
        this.isLeaf = true;
    }
}

class Tree23 {
    Node root;

    public void insert(int key1, String processorName, double clockFrequency, int cacheSize, double busFrequency, int specInt, int specFp) {
        root = insert(root, key1, processorName, clockFrequency, cacheSize, busFrequency, specInt, specFp);
    }

    private Node insert(Node node, int key1, String processorName, double clockFrequency, int cacheSize, double busFrequency, int specInt, int specFp) {
        if (node == null) {
            return new Node(key1, processorName, clockFrequency, cacheSize, busFrequency, specInt, specFp);
        }

        if (node.isLeaf) {
            if (key1 < node.key1) {
                return split(node, key1, processorName, clockFrequency, cacheSize, busFrequency, specInt, specFp);
            } else if (node.key2 == 0 || key1 < node.key2) {
                return split(node, key1, processorName, clockFrequency, cacheSize, busFrequency, specInt, specFp);
            } else {
                return node;
            }
        }

        if (key1 < node.key1) {
            node.left = insert(node.left, key1, processorName, clockFrequency, cacheSize, busFrequency, specInt, specFp);
        } else if (node.key2 == 0 || key1 < node.key2) {
            node.middle = insert(node.middle, key1, processorName, clockFrequency, cacheSize, busFrequency, specInt, specFp);
        } else {
            node.right = insert(node.right, key1, processorName, clockFrequency, cacheSize, busFrequency, specInt, specFp);
        }
        return node;
    }

    private Node split(Node node, int key1, String processorName, double clockFrequency, int cacheSize, double busFrequency, int specInt, int specFp) {
        Node newNode = new Node(key1, processorName, clockFrequency, cacheSize, busFrequency, specInt, specFp);
        if (key1 < node.key1) {
            newNode.key2 = node.key1;
            node.key1 = key1;
        } else {
            newNode.key2 = key1;
        }
        node.isLeaf = false;
        node.left = newNode;
        return node;
    }

    public void display() {
        display(root);
    }

    private void display(Node node) {
        if (node != null) {
            System.out.print(node.key1);
            if (node.key2 != 0) {
                System.out.print(" - " + node.key2);
            }
            System.out.println();
            display(node.left);
            display(node.middle);
            display(node.right);
        }
    }

    public boolean delete(int key) {
        return false;
    }

    public void saveToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            saveToFile(writer, root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile(BufferedWriter writer, Node node) throws IOException {
        if (node != null) {
            writer.write(node.key1 + "," + node.processorName + "," + node.clockFrequency + "," +
                    node.cacheSize + "," + node.busFrequency + "," + node.specInt + "," + node.specFp);
            writer.newLine();
            saveToFile(writer, node.left);
            saveToFile(writer, node.middle);
            saveToFile(writer, node.right);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Tree23 tree = new Tree23();
        Scanner scanner = new Scanner(System.in);
        loadFromFile(tree, "PROCS.TXT");

        while (true) {
            System.out.println("Введите команду (L, D n, A n, S, E): ");
            String command = scanner.nextLine().trim();

            if (command.equalsIgnoreCase("L")) {
                tree.display();
            } else if (command.startsWith("D ")) {
                int key = Integer.parseInt(command.split(" ")[1]);
                if (tree.delete(key)) {
                    System.out.println("Запись с ключом " + key + " удалена.");
                } else {
                    System.out.println("Запись с ключом " + key + " не найдена.");
                }
            } else if (command.startsWith("A ")) {
                int key = Integer.parseInt(command.split(" ")[1]);
                // Запрашиваем данные для новой записи
                System.out.println("Введите название процессора: ");
                String processorName = scanner.nextLine();
                System.out.println("Введите тактовую частоту: ");
                double clockFrequency = Double.parseDouble(scanner.nextLine());
                System.out.println("Введите размер кеш-памяти: ");
                int cacheSize = Integer.parseInt(scanner.nextLine());
                System.out.println("Введите частоту системной шины: ");
                double busFrequency = Double.parseDouble(scanner.nextLine());
                System.out.println("Введите результат теста SPECint: ");
                int specInt = Integer.parseInt(scanner.nextLine());
                System.out.println("Введите результат теста SPECfp: ");
                int specFp = Integer.parseInt(scanner.nextLine());
                tree.insert(key, processorName, clockFrequency, cacheSize, busFrequency, specInt, specFp);
            } else if (command.equalsIgnoreCase("S")) {
                tree.saveToFile("PROCS.TXT");
                System.out.println("Записи сохранены в файл PROCS.TXT.");
            } else if (command.equalsIgnoreCase("E")) {
                break;
            } else {
                System.out.println("Неверная команда.");
            }
        }
        scanner.close();
    }

    private static void loadFromFile(Tree23 tree, String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int key = Integer.parseInt(parts[0]);
                String processorName = parts[1];
                double clockFrequency = Double.parseDouble(parts[2]);
                int cacheSize = Integer.parseInt(parts[3]);
                double busFrequency = Double.parseDouble(parts[4]);
                int specInt = Integer.parseInt(parts[5]);
                int specFp = Integer.parseInt(parts[6]);
                tree.insert(key, processorName, clockFrequency, cacheSize, busFrequency, specInt, specFp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
