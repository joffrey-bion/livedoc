package org.hildan.livedoc.springmvc.scanner.utils;

public class ClasspathUtils {

    public static boolean isRestControllerOnClasspath() {
        return isOnClassPath("org.springframework.web.bind.annotation.RestController");
    }

    public static boolean isRepositoryRestControllerOnClassPath() {
        return isOnClassPath("org.springframework.data.rest.webmvc.RepositoryRestController");
    }

    public static boolean isMessageMappingOnClasspath() {
        return isOnClassPath("org.springframework.messaging.handler.annotation.MessageMapping");
    }

    public static boolean isSubscribeMappingOnClasspath() {
        return isOnClassPath("org.springframework.messaging.simp.annotation.SubscribeMapping");
    }

    private static boolean isOnClassPath(String fullyQualifiedClassName) {
        try {
            Class.forName(fullyQualifiedClassName);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
