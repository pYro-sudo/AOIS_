package by.losik;

import java.util.ArrayList;
import java.util.List;

class ListUtils {
    public static List<List<String>> distinctValue(List<List<String>> list) {
        List<List<String>> result = new ArrayList<>();
        for (List<String> inner : list) {
            if (!result.contains(inner)) {
                result.add(inner);
            }
        }
        return result;
    }
}
