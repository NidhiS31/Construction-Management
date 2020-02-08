import java.util.ArrayList;

public class RedBlackTree
{
    static RBTreeNode rootNode;

    // Create a RBTreeNode of Red colour having black children
    public RBTreeNode createNodes(Building building)
    {
        RBTreeNode rbTreeNode = new RBTreeNode(building, "RED");
        rbTreeNode.leftChildNode = new RBTreeNode(null, "BLACK");
        rbTreeNode.rightChildNode = new RBTreeNode(null, "BLACK");
        return rbTreeNode;
    }

    // Insert a building in the RedBlack Tree
    public boolean insertBuildings(Building building)
    {
        RBTreeNode rbTreeNode = createNodes(building);
        //If root is note present, the new node will become the root
        if (rootNode == null) {
            rootNode = rbTreeNode;
            rootNode.colour = "BLACK";
            return true;
        }
        //Check the position of the building in the Tree with respect to Root node
        RBTreeNode findNode = rootNode;
        while (findNode != null) {
            if (findNode.building.getBuildingNum() == building.getBuildingNum())
            {
                return false;
            }
            //Insert the node in a Binary tree and then fix the tree to make it a Red Black Tree
            if (findNode.building.getBuildingNum() > building.getBuildingNum())
            {
                if (findNode.leftChildNode.building== null) {
                    findNode.leftChildNode = rbTreeNode;
                    rbTreeNode.parentNode = findNode;
                    FixUpRedBlackTree(rbTreeNode);
                    return true;
                }
                findNode = findNode.leftChildNode;
                continue;
            }
            if (findNode.building.getBuildingNum() < building.getBuildingNum()) {
                if (findNode.rightChildNode.building == null) {
                    findNode.rightChildNode = rbTreeNode;
                    rbTreeNode.parentNode = findNode;
                    FixUpRedBlackTree(rbTreeNode);
                    return true;
                }
                findNode = findNode.rightChildNode;
            }
        }
        return true;
    }

    // Delete a node from the Red Black Tree
    public void deleteNode(Building building) {
        if (rootNode == null)
            return;

        // search for the given element in the tree.
        RBTreeNode findNode = rootNode;
        while (findNode.building != null) {
            if (findNode.building == building) {
                break;
            }
            if (findNode.building.getBuildingNum() >= building.getBuildingNum()) {
                findNode = findNode.leftChildNode;
                continue;
            }
            if (findNode.building.getBuildingNum() < building.getBuildingNum()) {
                findNode = findNode.rightChildNode;
                continue;
            }
        }

        // If the node to be deleted is not present in the tree, return
        if (findNode.building == null) {
            return;
        }

        // find the next greater node to replace the deleted Node
        RBTreeNode replaceNode = replacementNode(findNode);

        // interchange the  given node and replacement node
        Building tempBuilding = findNode.building;
        findNode.building = replaceNode.building;
        replaceNode.building = tempBuilding;

        // remove the node
        RBTreeNode parent = replaceNode.parentNode;
        if (parent == null) {
            if (replaceNode.leftChildNode.building == null) {
                rootNode = null;
            } else {
                rootNode = replaceNode.leftChildNode;
                replaceNode.parentNode = null;
                rootNode.colour = "BLACK";
            }
            return;
        }

        if (parent.rightChildNode == replaceNode) {
            parent.rightChildNode = replaceNode.leftChildNode;
        } else {
            parent.leftChildNode = replaceNode.leftChildNode;
        }
        replaceNode.leftChildNode.parentNode = parent;
        String colour = replaceNode.leftChildNode.colour + replaceNode.colour;
        FixUpRedBlackTree(replaceNode.leftChildNode, colour); // balance the tree.
    }

    // fix a Red Black Tree after the deletion of a node
    private static void FixUpRedBlackTree(RBTreeNode rbTreeNode, String colour) {

        // if node and its child is a combination of red and black
        if (rbTreeNode.parentNode == null || colour.equals("BLACKRED") || colour.equals("REDBLACK")) {
            rbTreeNode.colour = "BLACK";
            return;
        }

        RBTreeNode parentNode = rbTreeNode.parentNode;
        RBTreeNode sibling = fetchSiblingNode(rbTreeNode);
        //Get the children of the sibling
        RBTreeNode siblingLeftChild = sibling.leftChildNode;
        RBTreeNode siblingRightChild = sibling.rightChildNode;

        if(siblingLeftChild == null && siblingRightChild == null) {
            return;
        }

        // if sibling and its children are balck.
        if (sibling.colour == "BLACK" && siblingLeftChild.colour == "BLACK" && siblingRightChild.colour == "BLACK") {
            sibling.colour = "RED";
            rbTreeNode.colour = "BLACK";
            String nodeColour = parentNode.colour + "BLACK";
            FixUpRedBlackTree(parentNode, nodeColour);
            return;
        }

        // if sibling is red
        if (sibling.colour == "RED") {
            if (parentNode.rightChildNode == sibling) {
                leftRotation(sibling);
            } else {
                rightRotation(sibling);
            }
            FixUpRedBlackTree(rbTreeNode, colour);
            return;
        }

        // sibling is red but one of its child is red.
        if(siblingLeftChild==null) {
            return;
        }
        if (parentNode.leftChildNode == sibling) {
            if (siblingLeftChild.colour == "RED") {
                rightRotation(sibling);
                siblingLeftChild.colour = "BLACK";
            } else {
                leftRotation(siblingRightChild);
                rightRotation(siblingRightChild);
                siblingRightChild.leftChildNode.colour = "BLACK";
            }
            return;
        } else {
            if (siblingRightChild.colour == "RED") {
                leftRotation(sibling);
                siblingRightChild.colour = "BLACK";
            } else {
                rightRotation(siblingLeftChild);
                leftRotation(siblingLeftChild);
                siblingLeftChild.rightChildNode.colour = "BLACK";
            }
            return;
        }
    }

    // function to find the replacement of a given node which is to be deleted
    private static RBTreeNode replacementNode(RBTreeNode rbTreeNode) {
        RBTreeNode next = rbTreeNode.rightChildNode;
        if (next.building == null) {
            return rbTreeNode;
        }
        while (next.leftChildNode.building != null) {
            next = next.leftChildNode;
        }
        return next;
    }

    // function to balance the Red Black Tree after insertion of a node
    public static void FixUpRedBlackTree(RBTreeNode rbTreeNode) {

        // if given node is root node
        if (rbTreeNode.parentNode == null) {
            rootNode = rbTreeNode;
            rootNode.colour = "BLACK";
            return;
        }

        // if node's parent colour is black
        if (rbTreeNode.parentNode.colour == "BLACK") {
            return;
        }

        // get the node's uncle node
        RBTreeNode sibling = fetchUncleNode(rbTreeNode);

        // if uncle is red.
        if (sibling.colour == "RED") {
            rbTreeNode.parentNode.colour = "BLACK";
            sibling.colour = "BLACK";
            rbTreeNode.parentNode.parentNode.colour = "RED";
            FixUpRedBlackTree(rbTreeNode.parentNode.parentNode);
            return;
        }

        // if uncle is black.
        else {
            if (rbTreeNode.parentNode.leftChildNode == rbTreeNode && rbTreeNode.parentNode.parentNode.leftChildNode == rbTreeNode.parentNode) {
                rightRotation(rbTreeNode.parentNode);
                FixUpRedBlackTree(rbTreeNode.parentNode);
                return;
            }
            if (rbTreeNode.parentNode.rightChildNode == rbTreeNode && rbTreeNode.parentNode.parentNode.rightChildNode == rbTreeNode.parentNode) {
                leftRotation(rbTreeNode.parentNode);
                FixUpRedBlackTree(rbTreeNode.parentNode);
                return;
            }
            if (rbTreeNode.parentNode.rightChildNode == rbTreeNode && rbTreeNode.parentNode.parentNode.leftChildNode == rbTreeNode.parentNode) {
                leftRotation(rbTreeNode);
                rightRotation(rbTreeNode);
                FixUpRedBlackTree(rbTreeNode);
                return;
            }
            if (rbTreeNode.parentNode.leftChildNode == rbTreeNode && rbTreeNode.parentNode.parentNode.rightChildNode == rbTreeNode.parentNode) {
                rightRotation(rbTreeNode);
                leftRotation(rbTreeNode);
                FixUpRedBlackTree(rbTreeNode);
                return;
            }
        }
    }

    // function to perform Left Rotation.
    private static void leftRotation(RBTreeNode rbTreeNode) {
        RBTreeNode parent = rbTreeNode.parentNode;
        RBTreeNode left = rbTreeNode.leftChildNode;
        rbTreeNode.leftChildNode = parent;
        parent.rightChildNode = left;
        performRotation(rbTreeNode, parent, left);
    }

    // function to perform Right Rotation.
    private static void rightRotation(RBTreeNode rbTreeNode) {
        RBTreeNode parentNode = rbTreeNode.parentNode;
        RBTreeNode rightChildNode = rbTreeNode.rightChildNode;
        rbTreeNode.rightChildNode = parentNode;
        parentNode.leftChildNode = rightChildNode;
        performRotation(rbTreeNode, parentNode, rightChildNode);
    }

    private static void performRotation(RBTreeNode rbTreeNode, RBTreeNode parentNode, RBTreeNode childNode) {
        if (childNode != null) {
            childNode.parentNode = parentNode;
        }
        String colour = parentNode.colour;
        parentNode.colour = rbTreeNode.colour;
        rbTreeNode.colour = colour;
        RBTreeNode grandParentNode = fetchParentNode(parentNode);
        parentNode.parentNode = rbTreeNode;
        rbTreeNode.parentNode = grandParentNode;

        if (grandParentNode == null) {
            rootNode = rbTreeNode;
            return;
        } else {
            if (grandParentNode.leftChildNode == parentNode) {
                grandParentNode.leftChildNode = rbTreeNode;
            } else {
                grandParentNode.rightChildNode = rbTreeNode;
            }
        }
    }

    //Get the uncle node of a given  node
    private static  RBTreeNode fetchUncleNode(RBTreeNode nephewNode)
    {
        RBTreeNode grandParentNode = fetchGrandparentNode(nephewNode);
        RBTreeNode uncleNode;
        if (grandParentNode == null)
            return null;
        else if(grandParentNode.rightChildNode == nephewNode.parentNode)
            uncleNode = grandParentNode.leftChildNode;
        else
            uncleNode = grandParentNode.rightChildNode;
        return uncleNode;
    }

    //Get the parent node of a given  node
    private static RBTreeNode fetchParentNode(RBTreeNode childNode)
    {
        RBTreeNode parentNode;
        if(childNode.parentNode != null)
            parentNode = childNode.parentNode;
        else
            parentNode = null;
        return parentNode;
    }

    //Get the grandparentNode of a given node
    private static RBTreeNode fetchGrandparentNode(RBTreeNode grandchildNode)
    {
        RBTreeNode grandParentNode;
        if(grandchildNode.parentNode != null && grandchildNode.parentNode.parentNode != null)
        {
            grandParentNode = grandchildNode.parentNode.parentNode;
            return grandParentNode;
        }
        else
            return null;
    }
    //Get the sibling node of a given node
    private static RBTreeNode fetchSiblingNode(RBTreeNode node) {
        RBTreeNode siblingNode;
        if (node.parentNode.leftChildNode == node) {
            siblingNode = node.parentNode.rightChildNode;
        }
        else {
            siblingNode = node.parentNode.leftChildNode;
        }
        return siblingNode;
    }

    //search a building in the Red Black Tree
    public static Building searchBuilding(RBTreeNode rbTreeNode, int buildingNumber) {
        if(rbTreeNode == null || rbTreeNode.building == null) {
            return null;
        }
        if(rbTreeNode.building.getBuildingNum()==buildingNumber) {
            return rbTreeNode.building;
        }else if(rbTreeNode.building.getBuildingNum()<buildingNumber) {
            return searchBuilding(rbTreeNode.rightChildNode, buildingNumber);
        }else if(rbTreeNode.building.getBuildingNum()>buildingNumber) {
            return searchBuilding(rbTreeNode.leftChildNode, buildingNumber);
        }
        return null;
    }

    //search all the buildings in the given range
    public static ArrayList<Building> searchInRange(ArrayList<Building> list, RBTreeNode rbTreeNode, int startBuilding, int endBuilding){
        if(rbTreeNode==null || rbTreeNode.building == null) {
            return list;
        }
        if(CheckRange(rbTreeNode.building.getBuildingNum(), startBuilding, endBuilding))
        {
            searchInRange(list, rbTreeNode.leftChildNode, startBuilding, endBuilding);
            list.add(rbTreeNode.building);
            searchInRange(list, rbTreeNode.rightChildNode, startBuilding, endBuilding);
        }
        else if(rbTreeNode.building.getBuildingNum() >= startBuilding)
        {
            searchInRange(list, rbTreeNode.leftChildNode, startBuilding, endBuilding);
        }
        else if(rbTreeNode.building.getBuildingNum() <= startBuilding)
        {
            searchInRange(list, rbTreeNode.rightChildNode, startBuilding, endBuilding);
        }
        return list;
    }

    private static boolean CheckRange(int buildingNum, int startBuilding, int endBuilding)
    {
        if(buildingNum >= startBuilding && buildingNum<=endBuilding)
            return true;
        return false;
    }
}
