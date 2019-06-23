package accrue.util;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Helper class to deserilize JSON
 */
public class JSONHelper {
//
//    /**
//     * Get a polyglot {@link Position} object from a (non-null) JSON object
//     *
//     * @param json
//     *            {@link JSONObject} we are deserializing from
//     * @return new {@link Position} object
//     */
//    public static Position positionFromJSON(JSONObject json) {
//        if (!json.has("path"))
//            return null;
//        Position pos = null;
//        try {
//            String path = null;
//            if (json.has("path")) {
//                path = getCanonical(json.getString("path"));
//            }
//            pos = new Position(path, getCanonical(json.getString("file")),
//                    json.getInt("line"), json.getInt("column"),
//                    json.getInt("endLine"), json.getInt("endColumn"),
//                    json.getInt("offset"), json.getInt("endOffset"));
//        } catch (JSONException e) {
//            System.err.println("Trouble unpacking JSON Position: "
//                    + e.getMessage());
//        }
//        return pos;
//    }
//
    /**
     * In an attempt to reduce the memory footprint, memoize strings
     */
    private static final Map<String,String> internMap = new HashMap<String, String>();
    
    /**
     * Get the canonical representation of the given string. i.e. two .equals string will have the same 
     * reference. This is done for memory efficiency. This is similar to {@link String#intern()}, but 
     * that function 1. uses a statically sized map and 2. behaves differently for different versions 
     * of Java (from 1.6 to 1.7) 
     * 
     * @param s
     *      String we want the canonical rep for
     *      
     * @return Canonical representation of the given string
     */
    public static String getCanonical(String s) {
        String canon = internMap.get(s);
        if (canon == null) {
            canon = s;
            internMap.put(canon, canon);
            if (internMap.size() % 100000 == 0) {
                System.err.println(internMap.size() + " CANONICAL STRINGS");
            }
        }
        return canon;
    }
}
