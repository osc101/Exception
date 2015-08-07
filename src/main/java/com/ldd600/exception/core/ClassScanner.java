package com.ldd600.exception.core;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.ldd600.exception.util.BasicUtils;
import com.ldd600.exception.util.ClassLoaderUtils;
import com.ldd600.exception.util.ConfigUtils;

public abstract class ClassScanner {
    protected ClassLoader classLoader;
    protected List<String> excludePackages;
    protected List<String> includePackages;
    
    public ClassScanner() {
      this.classLoader = ClassLoaderUtils.getClassLoader();
    }
    
    public ClassScanner(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
    public ClassScanner(List<String> includePackages, List<String> excludePackages) {
        this();
        this.includePackages = includePackages;
        this.excludePackages = excludePackages;
    }
    
    public ClassScanner(ClassLoader classLoader, List<String> includePackages, List<String> excludePackages) {
        this(classLoader);
        this.includePackages = includePackages;
        this.excludePackages = excludePackages;
    }
    
    protected void handleArchive(final Set<String> result, final File file) throws ZipException, IOException {
        final ZipFile zip = new ZipFile(file);
        final Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            final ZipEntry entry = entries.nextElement();
            final String name = entry.getName();
            this.handleItem(result, name);
        }
    }

    // TODO document
    protected void handleDirectory(final Set<String> result, final File file, final String path) throws ZipException, IOException {
        for (final File child : file.listFiles()) {
            final String newPath = path == null ? child.getName() : path + '/' + child.getName();
            if (child.isDirectory()) {
                this.handleDirectory(result, child, newPath);
            } else if (child.getName().toLowerCase().endsWith(".jar")) {
                handleArchive(result, child);
            } else {
                this.handleItem(result, newPath);
            }
        }
    }
    
    protected void handleItem(final Set<String> result, final String name) {
        handleItem(".class", result, name);
    }

    // TODO document
    protected void handleItem(String postfix, final Set<String> result, final String name) {
        if (name.endsWith(postfix)) {
            if (!BasicUtils.isEmptyList(includePackages)) {
                boolean proceed = false;
                for (final String regexp : this.includePackages) {
                    if (name.matches(ConfigUtils.packageNameToRegex(regexp))) {
                        proceed = true;
                        continue;
                    }
                }
                if (!proceed) {
                    return;
                }
            }
            if (!BasicUtils.isEmptyList(excludePackages)) {
                boolean proceed = true;
                for (final String regexp : this.excludePackages) {
                    if (name.matches(ConfigUtils.packageNameToRegex(regexp))) {
                        proceed = false;
                        continue;
                    }
                }
                if (!proceed) {
                    return;
                }
            }
//            final String classname = ConfigUtils.filenameToClassname(name);
            result.add(name);
        }
    }
    
    protected void  findDirectoriesInClasspath(final ArrayList<String> dirs) {
        try {
            if (classLoader instanceof URLClassLoader) {
                URL[] urls = ((URLClassLoader) classLoader).getURLs();
                for (URL u : urls) {
                    File f = new File(u.toURI());
                    if (f.isDirectory()) {
                        dirs.add(f.getCanonicalPath());
                    }
                }
            } else {
                lookForDirectoriesUsingDot(dirs);
            }
        } catch (final IOException ioe) {
            
        } catch (URISyntaxException e) {
           
        }
    }

    protected void lookForDirectoriesUsingDot(final ArrayList<String> dirs) throws IOException,
            UnsupportedEncodingException {
        final Enumeration<URL> urls = classLoader.getResources(".");
        while (urls.hasMoreElements()) {
            String urlPath = urls.nextElement().getFile();
            urlPath = URLDecoder.decode(urlPath, "UTF-8");
            if (urlPath.startsWith("file:")) {
                // On windows urlpath looks like file:/C: on Linux
                // file:/home
                // substring(5) works for both
                urlPath = urlPath.substring(5);
            }
            if (urlPath.indexOf('!') > 0) {
                urlPath = urlPath.substring(0, urlPath.indexOf('!'));
            }
            final File file = new File(urlPath);
            if (file.isDirectory()) {
                dirs.add(file.toString());
            }
        }
    }
}
