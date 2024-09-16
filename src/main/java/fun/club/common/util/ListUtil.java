package fun.club.common.util;

import java.util.List;

public class ListUtil {
        public static <T> List<T> getOrElseThrowList(List<T> list, String message){
        if (list == null || list.isEmpty()) {
                throw new RuntimeException(message);
            }
            return list;
    }
}
/**
 * list<> 깂이 비어있거나 null값일 때 예외를 던진다.
 */
