import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class RisingCity {
    private static int globalCounter = 0;
    private static int dayListIndex = 0;
    private static Building buildingInConstruction;


    //Main function of Rising City
    public static void main(String[] args) throws IOException {
        MinHeap minHeap = new MinHeap(2000);
        RedBlackTree rbTree = new RedBlackTree();
        //Read the input file using file scanner
        String inputFile = "src/inp1.txt";
        String inputLine;
        ArrayList<String> dayCountList = new ArrayList<>();
        ArrayList<String> activityList = new ArrayList<>();

        Scanner input = new Scanner(new FileReader(inputFile));
        while (input.hasNextLine())
        {
            inputLine = input.nextLine();
            dayCountList.add(inputLine.split(":")[0]);
            activityList.add(inputLine.split(":")[1]);
        }
        input.close();

        int workedDays = 0;
        boolean check = false;

        //After the complete construction of all buildings, exit
        while (true) {
            if (check && minHeap.getHeapSize() == 0 && dayListIndex >= dayCountList.size()) {
                break;
            }
            //select the building to work as the root of Minheap
            if (!check)
            {
                performActivity(minHeap, rbTree, dayCountList, activityList);
                check = true;
                buildingInConstruction = minHeap.getMinNode();
                workedDays = 0;
            }
            else
            {
                performActivity(minHeap, rbTree, dayCountList, activityList);
                if (minHeap.getHeapSize() == 1)
                    buildingInConstruction = minHeap.getMinNode();
            }
            globalCounter++;

            //Build the minheap
            if (workedDays == 0) {
                minHeap.buildMinHeaps();
                buildingInConstruction = minHeap.getMinNode();
            }

            //Start to Work on the selected building
            if (buildingInConstruction != null) {
                workedDays++;
                buildingInConstruction.setExecuted_time((buildingInConstruction.getExecuted_time() + 1));
                if (buildingInConstruction.getExecuted_time() == buildingInConstruction.getTotal_time()) {
                    printBuilding(buildingInConstruction, globalCounter);
                    minHeap.removeMinNode();
                    rbTree.deleteNode(buildingInConstruction);
                    minHeap.buildMinHeaps();
                    buildingInConstruction = minHeap.getMinNode();
                    workedDays = 0;
                }
            }

            //stop working when the work is done on a building for 5 days and select the next building to be worked on
            if (workedDays == 5)
            {
                minHeap.buildMinHeaps();
                buildingInConstruction = minHeap.getMinNode();
                workedDays = 0;
            }
        }
    }

    //Insert a building in the Red Black Tree as well as MinHeap
    public static boolean insertBuilding(MinHeap minHeap, RedBlackTree rbTree, Building building)
    {
        if (!rbTree.insertBuildings(building))
            return false;
        minHeap.insertBuilding(building);
        return true;
    }

    //Function to perform Construction on the building
    private static void performActivity(MinHeap minHeap, RedBlackTree rbTree, ArrayList<String> dayCountList, ArrayList<String> activityList)
    {
        if ((dayListIndex < dayCountList.size()) && globalCounter == Integer.parseInt(dayCountList.get(dayListIndex)))
        {
            if (activityList.get(dayListIndex).contains("Insert")) {
                String buildingNumber = activityList.get(dayListIndex).substring(activityList.get(dayListIndex).indexOf("(") + 1,
                        activityList.get(dayListIndex).indexOf(","));
                String totalTime = activityList.get(dayListIndex).substring(activityList.get(dayListIndex).indexOf(",") + 1,
                        activityList.get(dayListIndex).indexOf(")"));
                if (!insertBuilding(minHeap, rbTree, new Building(Integer.parseInt(buildingNumber), 0, Integer.parseInt(totalTime))))
                    return;
            }
            else if (activityList.get(dayListIndex).contains("Print"))
            {
                String range = activityList.get(dayListIndex).substring(activityList.get(dayListIndex).indexOf("(") + 1,
                        activityList.get(dayListIndex).indexOf(")"));
                if (range.contains(",")) {
                    String[] remainingValues = range.split(",");
                    ArrayList<Building> buildingList = RedBlackTree.searchInRange(new ArrayList<>(), RedBlackTree.rootNode,
                            Integer.parseInt(remainingValues[0]), Integer.parseInt(remainingValues[1]));
                    if (buildingList.isEmpty())
                        System.out.println("(0,0,0)");
                    else {
                        StringBuffer output = new StringBuffer();
                        for (Building building : buildingList) {
                            output.append("(");
                            output.append(building.getBuildingNum());
                            output.append(",");
                            output.append(building.getExecuted_time());
                            output.append(",");
                            output.append(building.getTotal_time());
                            output.append(")");
                            output.append(",");
                        }
                        System.out.println(output.substring(0, output.length() - 1));
                    }
                } else
                {
                    Building building = rbTree.searchBuilding(RedBlackTree.rootNode, Integer.parseInt(range));
                    if (building == null)
                        System.out.println("(0,0,0)");
                    else
                        System.out.println("(" + building.getBuildingNum() + "," + building.getExecuted_time() + "," + building.getTotal_time() + ")");
                }
            }
            dayListIndex++;
        }
    }


    //print
    private static void printBuilding(Building getBuilding, int globalCounter)
    {
        System.out.println("(" + getBuilding.getBuildingNum() + "," + globalCounter + ")");
    }

}
