package wizest.fx.util;
public class StringUtil {
    private StringUtil() {}

    //    public static boolean isIncludeIgnoreCase(String[] array, String arg){
    //
    //        if ( (array == null) || array.length == 0 || arg == null){
    //            return false ;
    //        }
    //
    //        for(int i = 0, length = array.length ; i < length ; ++i) {
    //            if(arg.equalsIgnoreCase(array[i])) {
    //                return true ;
    //            }
    //        }
    //        return false ;
    //    }
    public static String toArrayString(Object[] arr) {
        if (arr == null)
            return "null";
        StringBuffer buf = new StringBuffer();
        buf.append("{");
        for (int i = 0, len = arr.length - 1; i < len; ++i) {
            buf.append("[" + i + "]:");
            buf.append(arr[i]);
            buf.append(",\n");
        }
        if (arr.length > 0) {
            buf.append("[" + (arr.length - 1) + "]:");
            buf.append(arr[arr.length - 1]);
        }
        buf.append("}");
        return buf.toString();
    }
}