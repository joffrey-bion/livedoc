package org.hildan.livedoc.springmvc.scanner.builder.path;

import java.util.HashSet;
import java.util.Set;

class PathUtils {

    static Set<String> joinAll(Set<String> pathPrefixes, Set<String> pathsSuffixes) {
        Set<String> mappings = new HashSet<>();
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
