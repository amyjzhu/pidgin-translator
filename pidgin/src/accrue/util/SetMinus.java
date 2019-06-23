package accrue.util;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Functional representation of set1 minus set2.
 * 
 * @author snchong
 * 
 * @param <T>
 *            type of elements of both sets
 */
public class SetMinus<T> extends AbstractSet<T> implements Set<T> {
    /**
     * original set
     */
    private final Set<T> set;
    /**
     * set to subtract
     */
    private final Set<T> minus;
    /**
     * true if every element of minus is contained in the original set
     */
    private final boolean minusContainedInSet;

    /**
     * Create a set representing the set difference of <code>set</code> and
     * <code>minus</code>
     * 
     * @param set
     *            original set
     * @param minus
     *            set to subtract from the original set
     * @param minusContainedInSet
     *            true if every element of minus is contained in the original
     *            set
     */
    public SetMinus(Set<T> set, Set<T> minus, boolean minusContainedInSet) {
        this.set = set;
        this.minus = minus;
        this.minusContainedInSet = minusContainedInSet;
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o) && !minus.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return new SetMinusIterator(set.iterator());
    }

    @Override
    public int size() {
        if (minusContainedInSet) {
            return set.size() - minus.size();
        }
        throw new UnsupportedOperationException("Don't yet support SetMinus when minus is not contained in set");
    }

    /**
     * Iterator for a set that is the difference between two sets
     * 
     * @param <T>
     *            type of the elements of the sets
     */
    public class SetMinusIterator implements Iterator<T> {
        /**
         * iterator for the original set
         */
        private final Iterator<T> iter;
        /**
         * next element of the iteration
         */
        private T next = null;

        /**
         * Create an iterator for the difference of two sets
         * 
         * @param iterator
         *            iterator for the bigger set
         */
        public SetMinusIterator(Iterator<T> iterator) {
            this.iter = iterator;
        }

        @Override
        public boolean hasNext() {
            if (next != null)
                return true;
            while (iter.hasNext()) {
                T in = iter.next();
                if (!minus.contains(in)) {
                    this.next = in;
                    return true;
                }
            }
            return false;
        }

        @Override
        public T next() {
            if (hasNext()) {
                T n = next;
                next = null;
                return n;
            }
            // nothing left. Throw an exception via the underliying set
            return iter.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

}