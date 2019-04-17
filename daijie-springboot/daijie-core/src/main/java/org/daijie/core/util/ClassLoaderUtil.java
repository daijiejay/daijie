package org.daijie.core.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 加载class的工具类
 * @author daijie
 * @since 2019-04-12
 */
public class ClassLoaderUtil {

    /**
     * 从包package中获取所有的Class
     * @param packageName 包路径
     * @return Set
     */
    public static Set<Class<?>> getClassesInPackage(String packageName) {
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        boolean recursive = true;
        String packageNameIn = packageName;
        String packageDirName = packageNameIn.replace('.', '/');
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            java.lang.ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    addClassesInPackageByFile(classLoader, packageNameIn, URLDecoder.decode(url.getFile(), "UTF-8"), recursive, classes);
                } else if ("jar".equals(protocol)) {
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            if (name.charAt(0) == '/') {
                                name = name.substring(1);
                            }
                            // 定义的包名下面
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                if (idx != -1) {
                                    packageNameIn = name.substring(0, idx).replace('/', '.');
                                    name = name.substring(idx+1);
                                }
                                if ((idx != -1) || recursive) {
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        addClass(classLoader,packageNameIn, name,classes);
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    /**
     * 扫描文件获取包下的所有Class
     * @param classLoader class加载类
     * @param packageName 包名
     * @param packagePath 包路径
     * @param recursive 过滤标识
     * @param classes 加载class数据集
     */
    public static void addClassesInPackageByFile(java.lang.ClassLoader classLoader, String packageName, String packagePath, final boolean recursive, Set<Class<?>> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义文件过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        for (File file : dirfiles) {
            if (file.isDirectory()) {
                //目录递归
                addClassesInPackageByFile(classLoader,packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                //class文件
                addClass(classLoader,packageName,file.getName(),classes);
            }
        }
    }

    /**
     * 根据包名以及class文件名添加Class到集合中
     * @param classLoader class加载类
     * @param packageName 包名
     * @param classFileName 文件名
     * @param classes 加载class数据集
     */
    public static void addClass(java.lang.ClassLoader classLoader, String packageName, String classFileName, Set<Class<?>> classes) {
        try {
            // 去掉后面的".class" 获取真正的类名
            if (null!=classes) classes.add(classLoader.loadClass(packageName + '.' + classFileName.substring(0, classFileName.length() - 6)));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
