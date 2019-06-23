package accrue.util;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Functional representation of applying a substitution map on a set.
 * 
 * @author snchong
 * 
 * @param <T>
 *            Type of the elements of the set
 */
public class SetWrapper<T> extends AbstractSet<T> implements Set<T> {
    /**
     * Original set
     */
    private final Set<T> set;
    /**
     * Substitution to apply to this set
     */
    private final Map<T, T> subst;
    /**
     * True if every element in the domain of the substitution is contained in
     * the original set
     */
    private final boolean mapDomainContainedInSet;

    /**
     * Create a set from an original set an a substitution to apply to that set
     * 
     * @param set
     *            original set
     * @param subst
     *            substitution to apply
     * @param mapDomainContainedInSet
     *            True if every element in the domain of the substitution is
     *            contained in the original set
     */
    public SetWrapper(Set<T> set, Map<T, T> subst, boolean mapDomainContainedInSet) {
        this.set = set;
        this.subst = subst;
        this.mapDomainContainedInSet = mapDomainContainedInSet;
    }

    @Override
    public Iterator<T> iterator() {
        return new SetWrapperIterator(set.iterator());
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean contains(Object o) {
        if (subst.keySet().contains(o)) {
            // we defintely don't contain it
            return false;
        }
        if (set.contains(o)) {
            // in the set, and not in the domain of the map, we definitely
            // contain it
            return true;
        }
        if (subst.values().contains(o)) {
            if (mapDomainContainedInSet)
                return true;
            // uh oh, it's in the range of the map. Is the preimage in the set?
            Set<T> s = set;
            if (subst.keySet().size() < s.size()) {
                s = subst.keySet();
            }
            for (T mem : s) {
                if (o.equals(subst.get(mem)) && set.contains(mem)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Iterator for a set constituting of a set and a substitution to apply to
     * that set
     */
    public class SetWrapperIterator implements Iterator<T> {
        /**
         * iterator for the original set
         */
        final Iterator<T> iter;

        /**
         * Iterator for a set created from a set and a substitution to apply to
         * that set
         * 
         * @param iterator
         *            for the original set
         */
        public SetWrapperIterator(Iterator<T> iterator) {
            this.iter = iterator;
        }

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public T next() {
            T n = iter.next();
            if (subst.containsKey(n)) {
                return subst.get(n);
            }

            return n;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

}