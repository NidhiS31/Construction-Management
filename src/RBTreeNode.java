public class RBTreeNode {
        Building building; // data value.
        RBTreeNode leftChildNode; // points to left child
        RBTreeNode rightChildNode; // points to right child.
        RBTreeNode parentNode; // points to parent node.
        String colour; // color of the node.

        public RBTreeNode(Building building, String colour) {
            this.building = building;
            this.leftChildNode = null;
            this.rightChildNode = null;
            this.parentNode = null;
            this.colour = colour;
        }
}
