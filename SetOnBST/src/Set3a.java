import java.util.Iterator;

import components.binarytree.BinaryTree;
import components.binarytree.BinaryTree1;
import components.set.Set;
import components.set.SetSecondary;

/**
 * {@code Set} represented as a {@code BinaryTree} (maintained as a binary
 * search tree) of elements with implementations of primary methods.
 *
 * @param <T>
 *            type of {@code Set} elements
 * @mathdefinitions <pre>
 * IS_BST(
 *   tree: binary tree of T
 *  ): boolean satisfies
 *  [tree satisfies the binary search tree properties as described in the
 *   slides with the ordering reported by compareTo for T, including that
 *   it has no duplicate labels]
 * </pre>
 * @convention IS_BST($this.tree)
 * @correspondence this = labels($this.tree)
 *
 * @author Teng.Zhang & Kierra.Smith
 *
 */
public class Set3a<T extends Comparable<T>> extends SetSecondary<T> {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Elements included in {@code this}.
     */
    private BinaryTree<T> tree;

    /**
     * Returns whether {@code x} is in {@code t}.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} to be searched
     * @param x
     *            the label to be searched for
     * @return true if t contains x, false otherwise
     * @requires IS_BST(t)
     * @ensures isInTree = (x is in labels(t))
     */
    private static <T extends Comparable<T>> boolean isInTree(BinaryTree<T> t,
            T x) {
        assert t != null : "Violation of: t is not null";
        assert x != null : "Violation of: x is not null";

        boolean result = false;

        /*
         * Disassemble the tree.
         */
        BinaryTree<T> left = t.newInstance();
        BinaryTree<T> right = t.newInstance();

        if (t.size() > 0) {
            //disassemble the tree
            T root = t.disassemble(left, right);
            if (root.equals(x)) {
                result = true;
            }

            //the x is not equal to root, so find it
            //recursively in the left-subtree or right subtree
            else if (root.compareTo(x) < 0) {
                result = isInTree(right, x);
            } else if (root.compareTo(x) > 0) {
                result = isInTree(left, x);
            }
            //recover the tree
            t.assemble(root, left, right);
        }

        return result;

    }

    /**
     * Inserts {@code x} in {@code t}.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} to be searched
     * @param x
     *            the label to be inserted
     * @aliases reference {@code x}
     * @updates t
     * @requires IS_BST(t) and x is not in labels(t)
     * @ensures IS_BST(t) and labels(t) = labels(#t) union {x}
     */
    private static <T extends Comparable<T>> void insertInTree(BinaryTree<T> t,
            T x) {
        assert t != null : "Violation of: t is not null";
        assert x != null : "Violation of: x is not null";

        //an empty tree initially, x will be the root
        if (t.size() == 0) {
            BinaryTree<T> left = t.newInstance();
            BinaryTree<T> right = t.newInstance();
            t.assemble(x, left, right);
        } else {
            //Disassemble the tree and compare the root with the x recursively
            BinaryTree<T> left = t.newInstance();
            BinaryTree<T> right = t.newInstance();
            T root = t.disassemble(left, right);
            if (x.compareTo(root) < 0) {
                insertInTree(left, x);
            } else {
                insertInTree(right, x);
            }
            //assemble the tree
            t.assemble(root, left, right);
        }

    }

    /**
     * Removes and returns the smallest (left-most) label in {@code t}.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} from which to remove the label
     * @return the smallest label in the given {@code BinaryTree}
     * @updates t
     * @requires IS_BST(t) and |t| > 0
     * @ensures <pre>
     * IS_BST(t)  and  removeSmallest = [the smallest label in #t]  and
     *  labels(t) = labels(#t) \ {removeSmallest}
     * </pre>
     */
    private static <T> T removeSmallest(BinaryTree<T> t) {
        assert t != null : "Violation of: t is not null";
        assert t.size() > 0 : "Violation of: |t| > 0";

        //initialize the result
        T smallest;

        //disassemble the tree
        BinaryTree<T> left = t.newInstance();
        BinaryTree<T> right = t.newInstance();
        T root = t.disassemble(left, right);

        //find the left-most subtree
        if (left.size() > 0) {
            //the left tree is not empty so find the left-most one recursively
            smallest = removeSmallest(left);
            t.assemble(root, left, right);
        } else {
            //found the left-most one,return it
            smallest = root;
            //the right one become the new root
            t.transferFrom(right);
        }
        return smallest;

    }

    /**
     * Finds label {@code x} in {@code t}, removes it from {@code t}, and
     * returns it.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} from which to remove label {@code x}
     * @param x
     *            the label to be removed
     * @return the removed label
     * @updates t
     * @requires IS_BST(t) and x is in labels(t)
     * @ensures <pre>
     * IS_BST(t)  and  removeFromTree = x  and
     *  labels(t) = labels(#t) \ {x}
     * </pre>
     */
    private static <T extends Comparable<T>> T removeFromTree(BinaryTree<T> t,
            T x) {
        assert t != null : "Violation of: t is not null";
        assert x != null : "Violation of: x is not null";
        assert t.size() > 0 : "Violation of: x is in labels(t)";

        /*
         * Initialize the node to be removed, assume it is the root
         */
        T removed = t.root();
        BinaryTree<T> left = t.newInstance();
        BinaryTree<T> right = t.newInstance();

        /*
         * the root is not the object we want, find the ambition recursively
         */
        if (!t.root().equals(x)) {
            T temp = t.disassemble(left, right);
            if (temp.compareTo(x) < 0) {
                removed = removeFromTree(right, x);
            } else {
                removed = removeFromTree(left, x);
            }
            t.assemble(temp, left, right);

        } else {
            // Disassemble the 't' object into two components 'left' and 'right'
            t.disassemble(left, right);

            // If the left subtree is empty and right has elements
            if (left.size() == 0 && right.size() > 0) {
                //right becomes the new root
                t.transferFrom(right);
            }
            // If the right subtree is empty and left has elements
            else if (left.size() > 0 && right.size() == 0) {
                //left tree becomes the new root tree
                t.transferFrom(left);
            }
            // If both  contain elements
            else if (right.size() > 0 && left.size() > 0) {
                // the smallest element of the right tree becomes the new root
                t.assemble(removeSmallest(right), left, right);
            }
        }
        return removed;
    }

    /**
     * Creator of initial representation.
     */
    private void createNewRep() {

        this.tree = new BinaryTree1<T>();

    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Set3a() {
        this.createNewRep();

    }

    /*
     * Standard methods -------------------------------------------------------
     */

    @SuppressWarnings("unchecked")
    @Override
    public final Set<T> newInstance() {
        try {
            return this.getClass().getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "Cannot construct object of type " + this.getClass());
        }
    }

    @Override
    public final void clear() {
        this.createNewRep();
    }

    @Override
    public final void transferFrom(Set<T> source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof Set3a<?> : ""
                + "Violation of: source is of dynamic type Set3<?>";
        /*
         * This cast cannot fail since the assert above would have stopped
         * execution in that case: source must be of dynamic type Set3a<?>, and
         * the ? must be T or the call would not have compiled.
         */
        Set3a<T> localSource = (Set3a<T>) source;
        this.tree = localSource.tree;
        localSource.createNewRep();
    }

    /*
     * Kernel methods ---------------------------------------------------------
     */

    @Override
    public final void add(T x) {
        assert x != null : "Violation of: x is not null";
        assert !this.contains(x) : "Violation of: x is not in this";

        //insert the x into this tree
        insertInTree(this.tree, x);

    }

    @Override
    public final T remove(T x) {
        assert x != null : "Violation of: x is not null";
        assert this.contains(x) : "Violation of: x is in this";

        //remove an element from the tree
        return removeFromTree(this.tree, x);
    }

    @Override
    public final T removeAny() {
        assert this.size() > 0 : "Violation of: this /= empty_set";

        //remove the smallest one
        return removeSmallest(this.tree);
    }

    @Override
    public final boolean contains(T x) {
        assert x != null : "Violation of: x is not null";

        //check the element is in the tree
        return isInTree(this.tree, x);
    }

    @Override
    public final int size() {

        //return the size of the tree
        return this.tree.size();
    }

    @Override
    public final Iterator<T> iterator() {
        return this.tree.iterator();
    }

}
