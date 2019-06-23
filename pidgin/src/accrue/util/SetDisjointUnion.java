package accrue.util;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Functional wrapper representing the union of 2 disjoint sets.
 * @author snchong
 *
 * @param <T>
 */
public class SetDisjointUnion<T> extends AbstractSet<T> implements Set<T> {
    /**
     * first set
     */
    private final Set<T> s1;
    /**
     * second set
     */
    private final Set<T> s2;
    
    /**
     * Wrapper representing the union of two sets. These sets must be disjoint.
     * 
     * @param s1
     *            first set
     * @param s2
     *            second set
     */
    public SetDisjointUnion(Set<T> s1, Set<T> s2) {
        this.s1 = s1;
        this.s2 = s2;
    }

    @Override
    public Iterator<T> iterator() {
        return new SetDisjointUnionIterator(s1.iterator(), s2.iterator());
    }

    @Override
    public int size() {
        return s1.size() + s2.size();
    }
    
    @Override
    public boolean contains(Object o) {
        return s1.contains(o) || s2.contains(o);
    }
    
    /**
     * Iterator implementation for the disjoint union of two other sets.
     */
    public class SetDisjointUnionIterator implements Iterator<T> {
        /**
         * Iterator for first set
         */
        Iterator<T> i1;
        /**
         * Iterator for second set
         */
        Iterator<T> i2;
        
        /**
         * This iterator for union of two disjoint sets
         * 
         * @param i1
         *            Iterator for first set
         * @param i2
         *            Iterator for second set
         */
        public SetDisjointUnionIterator(Iterator<T> i1, Iterator<T> i2) {
            this.i1 = i1;
            this.i2 = i2;
        }

        @Override
        public boolean hasNext() {
            return i1.hasNext() || i2.hasNext();
        }

        @Override
        public T next() {
            if (i1.hasNext()) {
                return i1.next();
            }
            return i2.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }


}