package org.hildan.livedoc.springmvc.utils;

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

    public static boolean isRequestMappingOnClasspath() {
        return isOnClassPath("org.springframework.web.bind.annotation.RequestMapping");
    }

    public static boolean isGetMappingOnClasspath() {
        return isOnClassPath("org.springframework.web.bind.annotation.GetMapping");
    }

    public static boolean isCompletableFutureOnClasspath() {
        return isOnClassPath("java.util.concurrent.CompletableFuture");
    }

    public static boolean isCompletionStageOnClasspath() {
        return isOnClassPath("java.util.concurrent.CompletionStage");
    }

    public static boolean isSendToOnClasspath() {
        return isOnClassPath("org.springframework.messaging.handler.annotation.SendTo");
    }

    public static boolean isSendToUserOnClasspath() {
        return isOnClassPath("org.springframework.messaging.simp.annotation.SendToUser");
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
