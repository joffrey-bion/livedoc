package org.hildan.livedoc.springmvc.readers.mappings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PathUtils {

    public static List<String> joinAll(Collection<String> pathPrefixes, Collection<String> pathsSuffixes) {
        List<String> mappings = new ArrayList<>();
        for (String controllerPath : pathPrefixes) {
            for (String methodPath : pathsSuffixes) {
                mappings.add(join(controllerPath, methodPath));
            }
        }
        return mappings;
    }

    private static String join(String path1, String path2) {
        boolean path1HasSep = path1.endsWith("/");
        boolean path2HasSep = path2.startsWith("/");
        if (path1HasSep && path2HasSep) {
            return path1 + path2.substring(1);
        }
        if (!path1HasSep && !path2HasSep && (path1.isEmpty() || !path2.isEmpty())) {
            return path1 + '/' + path2;
        }
        return path1 + path2;
    }
}
