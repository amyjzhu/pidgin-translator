package accrue.util;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Functional representation of set1 intersect set2.
 * @author snchong
 *
 * @param <T> Type of the elements of the sets (must be the same in both sets)
 */
public class SetIntersect<T> extends AbstractSet<T> implements Set<T> {
    /**
     * first set
     */
    private final Set<T> set1;
    /**
     * second set
     */
    private final Set<T> set2;
    /**
     * True if every element of set1 is also in set2
     */
    private final boolean set1ContainedInSet2;

    /**
     * Create a new set representing the intersection of two sets
     * 
     * @param set1
     *            first set
     * @param set2
     *            second set
     * @param set1ContainedInSet2
     *            True if every element of set1 is also in set2
     */
    public SetIntersect(Set<T> set1, Set<T> set2, boolean set1ContainedInSet2) {
        this.set1 = set1;
        this.set2 = set2;
        this.set1ContainedInSet2 = set1ContainedInSet2;
    }

    @Override
    public boolean contains(Object o) {
        return set1.contains(o) && set2.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        Set<T> s = set1;
        Set<T> t = set2;
        if (set2.size() < set1.size()) {
            s = set2;
            t = set1;
        }
        return new SetIntersectIterator<T>(s.iterator(), t, set1ContainedInSet2);
    }
    
    /**
     * memoized size of this set
     */
    private Integer size = null;
    @Override
    public int size() {        
        if (set1ContainedInSet2) {
            return set1.size();
        }
        if (this.size == null) {
            int s = 0;
            for (@SuppressWarnings("unused") T v : this) {
                s++;
            }
            this.size = s;
        }
        return this.size;                 
    }
    
    /**
     * Iterator used to go over a set representing the intersection of 2 sets
     * 
     * @param <T>
     *            Type of the elements in the sets
     */
    public static class SetIntersectIterator<T> implements Iterator<T> {
        
        /**
         * Iterator for the one set
         */
        private final Iterator<T> iter;
        /**
         * The other set
         */
        private final Set<T> other;
        /**
         * the next element in this iterator
         */
        private T next = null;
        /**
         * true if the set represented by the iterator is contained in the other
         * set
         */
        private boolean iteratorSetContainedInOther;

        /**
         * Create an iterator for the intersection of the set
         * <code>iterator</code> corresponds to and the set <code>other</code>
         * 
         * @param iterator
         *            iterator for one of the two sets being intersected
         * @param other
         *            other set being intersected
         * @param iteratorSetContainedInOther
         *            true if the set represented by the iterator is contained
         *            in the set <code>other</code>
         */
        public SetIntersectIterator(Iterator<T> iterator, Set<T> other, boolean iteratorSetContainedInOther) {
            this.iter = iterator;
            this.other = other;
            this.iteratorSetContainedInOther = iteratorSetContainedInOther;
        }

        @Override
        public boolean hasNext() {
            if (iteratorSetContainedInOther) {
                return iter.hasNext();
            }
            
            if (next != null) return true;
            while (iter.hasNext()) {
                T in = iter.next();
                if (other.contains(in)) {
                    this.next = in;
                    return true;
                }
            }
            return false;
        }

        @Override
        public T next() {
            if (iteratorSetContainedInOther) {
                return iter.next();
            }
            
            if (hasNext()) {
                T n = next;
                next = null;
                return n;                    
            }
            // nothing left. Throw an exception via the underlying set
            return iter.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }



}