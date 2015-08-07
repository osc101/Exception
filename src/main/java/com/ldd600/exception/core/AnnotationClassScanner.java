package com.ldd600.exception.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ldd600.exception.util.BasicUtils;

public class AnnotationClassScanner extends ClassScanner {
    private static final Log log = LogFactory.getLog(AnnotationClassScanner.class);
    private final String resourceName;
    private boolean scanDirs = true;

    /**
     * Default constructor, the resourceName will be "annotation.properties" and the class loader will be
     * Thread.currentThread().getContextClassLoader()
     */
    public AnnotationClassScanner() {
        super();
        this.resourceName = "annotation.properties";
    }

    // TODO document
    public AnnotationClassScanner(String resourceName, final ClassLoader classLoader) {
        super(classLoader);
        if (BasicUtils.isEmptyString(resourceName)) {
            resourceName = "annotation.properties";
        }
        this.resourceName = resourceName;
    }

    // TODO document
    public AnnotationClassScanner(String resourceName, boolean scanDirs, List<String> includePackages,
            List<String> excludePackages) {
        super(includePackages, excludePackages);
        this.scanDirs = scanDirs;
        this.resourceName = resourceName;
    }

    // TODO document
    public Set<String> getClassResourceNames() {
        final Set<String> result = new HashSet<String>();
        Enumeration<URL> urls;
        final ArrayList<String> dirs = new ArrayList<String>();
        try {
            urls = this.classLoader.getResources(this.resourceName);
        } catch (final IOException ioe) {
            AnnotationClassScanner.log.warn("could not read: " + this.resourceName, ioe);
            return result;
        }
        try {
            if (this.scanDirs) {
                findDirectoriesInClasspath(dirs);
            }
        } catch (final AccessControlException e) {
        }
        while (urls.hasMoreElements()) {
            try {
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
                } else {
                    urlPath = new File(urlPath).getParent();
                }
                AnnotationClassScanner.log.debug("scanning: " + urlPath);
                final File file = new File(urlPath);
                if (file.isDirectory()) {
                    if (dirs.contains(urlPath)) {
                        dirs.remove(urlPath);
                    }
                    this.handleDirectory(result, file, null);
                } else if (file.getName().toLowerCase().endsWith(".jar")) {
                    this.handleArchive(result, file);
                }
            } catch (IOException ioe) {
                AnnotationClassScanner.log.warn("could not read entries", ioe);
            }
        }
        for (final String urlPath : dirs) {
            final File file = new File(urlPath + "/");
            try {
                this.handleDirectory(result, file, null);
            } catch (IOException ioe) {
                AnnotationClassScanner.log.warn("could not read entries", ioe);
            }
        }
        return result;
    }
  
}
