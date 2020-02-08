public class MinHeap
{
    private Building[] buildings;
    private int heapSize;
    private int maxActiveBuildings;
    public static final int ROOTNODE = 1;

    //MinHeap Constructor
    public MinHeap(int maxActiveBuildings) {
        this.maxActiveBuildings = maxActiveBuildings;
        this.heapSize = 0;
        buildings = new Building[this.maxActiveBuildings + 1];
        buildings[0] = new Building(0,0,0);
    }


    public int getHeapSize()
    {
        return heapSize;
    }

    //Get the root node of the Min Heap
    public Building getMinNode()
    {
        Building minNode = buildings[ROOTNODE];
        return minNode;
    }

    // Function to insert a node into the heap
    public void insertBuilding(Building building) {
        if (heapSize >= maxActiveBuildings)
            return;
        buildings[++heapSize] = building;
    }

    // Function to build the min heap
    public void buildMinHeaps() {
        for (int index = (heapSize / 2); index >= 1; index--) {
            minHeapifyNode(index);
        }
    }

    // Function to  set the node at its correct position in the MinHeap
    public void minHeapifyNode(int index) {

        // If the node is not a leaf node then minHeapify
        if (!checkLeaf(index))
        {

            if (buildings[leftChildNode(index)]!=null && (buildings[index].getExecuted_time() > buildings[leftChildNode(index)].getExecuted_time())
                    || (buildings[rightChildNode(index)]!=null && (buildings[index].getExecuted_time() > buildings[rightChildNode(index)].getExecuted_time())))
            {

                // Interchange with the left child and minHeapify
                if(buildings[leftChildNode(index)]==null)
                {
                    interchangeNodes(index, rightChildNode(index));
                    minHeapifyNode(rightChildNode(index));
                }
                else if (buildings[rightChildNode(index)]==null)
                {
                    interchangeNodes(index, leftChildNode(index));
                    minHeapifyNode(leftChildNode(index));
                }
                else
                {
                    if (buildings[leftChildNode(index)].getExecuted_time() < buildings[rightChildNode(index)].getExecuted_time())
                    {
                        interchangeNodes(index, leftChildNode(index));
                        minHeapifyNode(leftChildNode(index));
                    }
                    // Interchange with the right child and minHeapify
                    else if (buildings[leftChildNode(index)].getExecuted_time() > buildings[rightChildNode(index)].getExecuted_time()) {
                        interchangeNodes(index, rightChildNode(index));
                        minHeapifyNode(rightChildNode(index));
                    }
                    else
                    {
                        int minIndex = Math.min(buildings[leftChildNode(index)].getBuildingNum(), buildings[rightChildNode(index)].getBuildingNum());
                        if(buildings[leftChildNode(index)].getBuildingNum() == minIndex)
                        {
                            interchangeNodes(index, leftChildNode(index));
                            minHeapifyNode(leftChildNode(index));
                        }
                        else
                        {
                            interchangeNodes(index, rightChildNode(index));
                            minHeapifyNode(rightChildNode(index));
                        }
                    }
                }
            }
            else
            {
                if (buildings[leftChildNode(index)]!=null && buildings[index].getExecuted_time() == buildings[leftChildNode(index)].getExecuted_time())
                {
                    if (buildings[index].getBuildingNum() > buildings[leftChildNode(index)].getBuildingNum())
                    {
                        interchangeNodes(index, leftChildNode(index));
                        minHeapifyNode(leftChildNode(index));
                    }
                }

                if (buildings[rightChildNode(index)]!=null && buildings[index].getExecuted_time() == buildings[rightChildNode(index)].getExecuted_time())
                {
                    if (buildings[index].getBuildingNum() > buildings[rightChildNode(index)].getBuildingNum())
                    {
                        interchangeNodes(index, rightChildNode(index));
                        minHeapifyNode(rightChildNode(index));
                    }
                }
            }
        }
    }

    //Function to get the parent node
    private int parentNode(int index)
    {
        int parentIndex = index / 2;
        return parentIndex;
    }

    // Function to get the left child node
    private int leftChildNode(int index)
    {
        int leftChildIndex = (2 * index);;
        return  leftChildIndex;
    }

    // Function to get the right child of the node
    private int rightChildNode(int index)
    {
        int rightChildIndex = (2 * index) + 1;
        return rightChildIndex;
    }

    // Function to check whether a node is a leaf node
    private boolean checkLeaf(int index) {
        if (parentNode(index) > heapSize && rightChildNode(index) > heapSize && index <= heapSize)
            return true;
        return false;
    }

    // Function to interchange two nodes of the heap
    private void interchangeNodes(int firstNode, int secondNode) {
        Building tempBuilding;
        tempBuilding = buildings[firstNode];
        buildings[firstNode] = buildings[secondNode];
        buildings[secondNode] = tempBuilding;
    }

    public Building removeMinNode()
    {
        Building building = buildings[ROOTNODE];
        buildings[ROOTNODE] = buildings[heapSize];
        buildings[heapSize] = null;
        heapSize = heapSize-1;
        buildMinHeaps();
        return building;
    }
}

