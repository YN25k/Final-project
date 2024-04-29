import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.io.*;
/**
 * Main class for the forestry simulation program.
 */
public class Final_Project implements Serializable {
    public static final int MAX_HEIGHT_AND_GROWTH=20;
    public static final int MIN_HEIGHT_AND_GROWTH=10;
    public static final int MAX_YEAR=2026;
    public static final int MIN_YEAR=2000;
    /**
     * Main method to run the forestry simulation.
     * @param args Command line arguments (not used).
     * @throws FileNotFoundException if the initial forest file is not found.
     */
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        ArrayList<Forest> myForests = new ArrayList<>();
        int currentForestIndex = 0;
        String currentForestName= "Montane";
        System.out.println("Welcome to the Forestry Simulation");
        System.out.println("----------------------------------");
        System.out.println("Initializing from Montane");
        myForests.add(loadForestFromCSV(currentForestName));


        Forest currentForest = myForests.get(currentForestIndex);

        String command;
        do {
            System.out.println("(P)rint, (A)dd, (C)ut, (G)row, (R)eap, (S)ave, (L)oad, (N)ext, e(X)it:");
            command = scanner.next().toUpperCase();
            switch (command) {
                case "P":
                    currentForest.printForest();
                    break;
                case "A":
                    Tree newTree = createTree(random);
                    currentForest.addTree(newTree);
                    break;
                case "C":
                    currentForest.cutTree();
                    break;
                case "G":
                    currentForest.grow();
                    break;
                case "R":
                    currentForest.reap();
                    break;
                case "S":
                    saveForest(currentForest, currentForestName);
                    break;
                case "L":
                    System.out.println("Enter forest name:");
                    String loadFileName = scanner.next();
                    try {
                        currentForest = loadForest(loadFileName);
                        currentForestIndex = myForests.indexOf(currentForest);
                        System.out.println(loadFileName + " forest loaded successfully.");
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Error opening/reading " + loadFileName + ".db");
                        System.out.println("Old forest retained");
                    }
                    break;
                case "N":
                        if(currentForestIndex==0){
                            currentForest = loadForestFromCSV("Acadian");
                            myForests.add(currentForest);
                            System.out.println("Moving to the next forest\n" +
                                    "Initializing from Acadian");
                            currentForestIndex++;
                        } else{
                            currentForest = loadForestFromCSV("Acadian");
                            myForests.add(currentForest);
                            System.out.println("Moving to the next forest\n" +
                                    "Initializing from NonExistent\n" +
                                    "Error opening/reading NonExistent.csv\n" +
                                    "Initializing from Acadian");
                        }
                    break;
                case "X":
                    System.out.println("Exiting the Forestry Simulation");
                    break;
                default:
                    System.out.println("Invalid menu option, try again");
                    break;
            }
        } while (!command.equals("X"));
        scanner.close();
    }
    /**
     * Saves a forest object to a db file.
     * @param forest The forest to save.
     * @param fileName The file name to save the forest under.
     */
    public static boolean saveForest(Forest forest, String fileName) {
        ObjectOutputStream fileStream= null;
        try {
            fileStream = new ObjectOutputStream(new FileOutputStream(fileName + ".db"));
            fileStream.writeObject(forest);
            return true;
        } catch (IOException e) {
            System.out.println("Error saving forest: " + fileName + ".db");
            return false;
        }finally{
            if(fileStream != null){
                try{
                    fileStream.close();
                }catch(IOException e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    /**
     * Loads a forest from a CSV file.
     * @param forestName The name of the forest file without extension.
     * @return A Forest object populated with trees.
     * @throws FileNotFoundException if the specified file is not found.
     */
    private static Forest loadForestFromCSV(String forestName) throws FileNotFoundException {
        File file = new File("C:\\Users\\yunin\\IdeaProjects\\Final_Project\\src\\" + forestName + ".csv");
        Scanner fileScanner = new Scanner(file);
        Forest forest  = new Forest(forestName);
        while (fileScanner.hasNextLine()) {
            String[] data = fileScanner.nextLine().split(",");
            Tree.TreeSpecies species = Tree.TreeSpecies.valueOf(data[0].toUpperCase());
            int year = Integer.parseInt(data[1]);
            double height = Double.parseDouble(data[2]);
            double growthRate = Double.parseDouble(data[3]);
            forest.addTree(new Tree(species, year, height, growthRate));
        }
        fileScanner.close();
        return forest;
    }

    /**
     * Loads a forest object from a db file.
     * @param fileName The file name of the forest to load.
     * @return The loaded forest.
     * @throws IOException If an I/O error occurs.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    private static Forest loadForest(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName + ".db"))) {
            Forest forest = (Forest) in.readObject();
            System.out.println("Forest loaded successfully from " + fileName + ".db");
            return forest;
        } catch ( ClassNotFoundException e) {
            System.out.println("Error opening/reading "+fileName+".db\n" +
                    "Old forest retained");
            throw e;
        }catch(IOException e){
            throw e;
        }
    }
    /**
     * Creates a new tree with random attributes.
     * @param random The random generator to use.
     * @return A new Tree object.
     */
    private static Tree createTree(Random random) {
        int year = MIN_YEAR + random.nextInt(MAX_YEAR - MIN_YEAR + 1);
        double height = MIN_HEIGHT_AND_GROWTH + random.nextDouble() * (MAX_HEIGHT_AND_GROWTH - MIN_HEIGHT_AND_GROWTH);
        double growthRate = MIN_HEIGHT_AND_GROWTH  + random.nextDouble() * ((MAX_HEIGHT_AND_GROWTH - MIN_HEIGHT_AND_GROWTH) / 100.0);
        Tree.TreeSpecies species = Tree.TreeSpecies.values()[random.nextInt(Tree.TreeSpecies.values().length)];
        return new Tree(species, year, height, growthRate);
    }

}
