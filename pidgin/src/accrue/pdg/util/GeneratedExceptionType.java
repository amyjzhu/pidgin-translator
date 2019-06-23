package accrue.pdg.util;

/**
 * Type for a JVM generated exception
 */
public enum GeneratedExceptionType {
    /**
     * {@link NullPointerException}
     */
    NULL_POINTER,
    /**
     * {@link ArrayStoreException}
     */
    ARRAY_STORE,
    /**
     * {@link ArithmeticException} thrown due to division by zero
     */
    DIVIDE_BY_ZERO,
    /**
     * {@link ClassCastException}
     */
    CLASS_CAST,
    /**
     * {@link NegativeArraySizeException}
     */
    NEGATIVE_ARRAY_SIZE,
    /**
     * {@link ArrayIndexOutOfBoundsException}
     */
    ARRAY_INDEX_OUT_OF_BOUNDS, 
    /**
     * Imprecise type used to summarize all Virtual Machine errors that can be
     * thrown (i.e. OutOfMemoryError...).
     */
    ERROR;
}
