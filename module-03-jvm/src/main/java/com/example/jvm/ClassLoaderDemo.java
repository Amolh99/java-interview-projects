package com.example.jvm;

/** Shows which class loader defined a type. Parent delegation makes core classes come from the bootstrap loader. */
public final class ClassLoaderDemo {

    private ClassLoaderDemo() {}

    public static boolean isBootstrapLoaded(Class<?> type) {
        return type.getClassLoader() == null;
    }

    public static String describe(Class<?> type) {
        ClassLoader loader = type.getClassLoader();
        String loaderName = loader == null ? "bootstrap" : loader.getClass().getName();
        return type.getName() + " loaded by " + loaderName;
    }
}
