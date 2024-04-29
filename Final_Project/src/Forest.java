import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
/**
 * Class representing a forest, which consists of multiple trees.
 */
public class Forest implements Serializable {
    Scanner scanner = new Scanner(System.in);
    private String name;
    private List<Tree> trees = new ArrayList<>();
    /**
     * Constructor for creating a new Forest.
     * @param name The name of the forest.
     */
    public Forest(String name) {
        this.name = name;
    }
    /**
     * Adds a tree to the forest.
     * @param tree The tree to add.
     */
    public void addTree(Tree tree) {
        trees.add(tree);
    }
    /**
     * Prompts the user to select a tree to cut down, then removes it from the forest.
     */
    public void cutTree() {
        int index;
        boolean isValid = false;

        while (!isValid) {
            System.out.println("Tree number to cut down:");
            try {
                index = Integer.parseInt(scanner.nextLine());
                if (index >= 0 && index < trees.size()) {
                    trees.remove(index);
                    isValid = true;
                }else{
                    System.out.println("Tree number "+index+" does not exist");
                }
            } catch (NumberFormatException e) {
                System.out.println("That is not an integer");
            }
        }
    }
    /**
     * Grows all trees in the forest by their respective growth rates.
     */
    public void grow() {
        for (Tree tree : trees) {
            tree.grow();
        }
    }
    /**
     * Reaps (replaces) trees that exceed a certain height threshold.
     */
    public void reap() {
        Random rand = new Random();
        boolean isValid=false;
        double heightThreshold;
        while (isValid!=true) {
            System.out.println("Height to reap from:");
            try {
                heightThreshold = Double.parseDouble(scanner.nextLine());
                isValid=true;
                for (int i = 0; i < trees.size(); i++) {
                    Tree tree = trees.get(i);
                    if (tree.getHeight() > heightThreshold) {
                        System.out.println("Reaping the tall tree  " + tree);
                        Tree.TreeSpecies newSpecies = Tree.TreeSpecies.values()[rand.nextInt(Tree.TreeSpecies.values().length)];
                        int newYear = Final_Project.MIN_YEAR + rand.nextInt(Final_Project.MAX_YEAR - Final_Project.MIN_YEAR + 1);
                        double newHeight = Final_Project.MIN_HEIGHT_AND_GROWTH + rand.nextDouble() * (Final_Project.MAX_HEIGHT_AND_GROWTH - Final_Project.MIN_HEIGHT_AND_GROWTH);
                        double newGrowthRate = Final_Project.MIN_HEIGHT_AND_GROWTH + rand.nextDouble() * (Final_Project.MAX_HEIGHT_AND_GROWTH - Final_Project.MIN_HEIGHT_AND_GROWTH);
                        Tree newTree = new Tree(newSpecies, newYear, newHeight, newGrowthRate);
                        trees.set(i, newTree);
                        System.out.println("Replaced with new tree " + newTree);
                    }
                }
            }catch(NumberFormatException e){
                System.out.println("This is not an integer");
            }
        }
    }

    /**
     * Prints the details of all trees in the forest.
     */
    public void printForest() {
        System.out.println("Forest name: " + name);
        for (int i = 0; i < trees.size(); i++) {
            System.out.println("     " + i + " " + trees.get(i));
        }
        System.out.println("There are " + trees.size() + ", with an average height of " + calculateAverageHeight());
    }
    /**
     * Calculates the average height of the trees in the forest.
     * @return The average height of the trees.
     */
    private double calculateAverageHeight() {
        double totalHeight = 0;
        for (Tree tree : trees) {
            totalHeight += tree.getHeight();
        }
        double average = totalHeight / trees.size();
        return Double.parseDouble(String.format("%.2f", average));
    }

}
