package accrue.query.util;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Many primitives match on a set of Objects this class provides utility methods
 * for these classes.
 * 
 * @param <T>
 *            Type of object to match
 */
public abstract class StringMatcher<T> {
    /**
     * Get a string to match against for a T.
     * 
     * @param t
     *            T to get the String for
     * @return String to match against
     */
    protected abstract String getStringToMatch(T t);

    /**
     * Get Ts matching a given string. What it matches against is defined
     * by the return value of {@link StringMatcher#getStringToMatch(Object)}.
     * 
     * @param candidates
     *            Candidates to pull matches from
     * @param pattern
     *            String to match against, regular expression are formatted as
     *            "r[regex]"
     * 
     * @return Ts matching the pattern
     */
    public Set<T> getMatches(Set<T> candidates, String pattern) {
        Set<T> matching = new LinkedHashSet<T>();
        if (pattern.startsWith("r[") && pattern.endsWith("]")) {
            pattern = pattern.substring(2, pattern.length() - 1);
            Pattern p = Pattern.compile(pattern);
            Matcher m = null;
            for (T c : candidates) {
                m = p.matcher(getStringToMatch(c));
                if (m.matches()) {
                    matching.add(c);
                }
            }
        } else {
            for (T n : candidates) {
                if (getStringToMatch(n).contains(pattern)) {
                    matching.add(n);
                }
            }
        }
        return matching;
    }
}
