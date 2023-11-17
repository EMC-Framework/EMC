package me.deftware.client.framework.util.path;

import me.deftware.client.framework.FrameworkConstants;
import me.deftware.client.framework.main.bootstrap.Bootstrap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

/**
 * @author Deftware
 */
public class LocationUtil {

    @Nullable
    private URL url;

    @Nullable
    private File file;

    private LocationUtil(URL url) {
        this.url = url;
    }

    private LocationUtil(File file) {
        this.file = file;
    }

    private static File getModFileById(String id) throws Exception {
        Class<?> clazz = Class.forName("net.minecraftforge.fml.ModList");
        Class<?> fileInfoClass = Class.forName("net.minecraftforge.forgespi.language.IModFileInfo");
        Class<?> modFileClass = Class.forName("net.minecraftforge.forgespi.locating.IModFile");

        Method getInstance = clazz.getDeclaredMethod("get");
        Method getModFileById = clazz.getDeclaredMethod("getModFileById", String.class);
        Method getFile = fileInfoClass.getDeclaredMethod("getFile");
        Method getFilePath = modFileClass.getDeclaredMethod("getFilePath");

        Object modList = getInstance.invoke(null);
        Object fileInfo = getModFileById.invoke(modList, id);
        Object modFile = getFile.invoke(fileInfo);
        Path path = (Path) getFilePath.invoke(modFile);

        return path.toFile();
    }

    @Nonnull
    public static LocationUtil getClassPhysicalLocation(@Nonnull Class<?> c) {
        try {
            URL codeSourceLocation = c.getProtectionDomain().getCodeSource().getLocation();
            if (codeSourceLocation != null) {
                return new LocationUtil(codeSourceLocation);
            }
        } catch (SecurityException | NullPointerException ignored) { }

        URL classResource = c.getResource(c.getSimpleName() + ".class");
        if (classResource == null) {
            return new LocationUtil((URL) null);
        }
        String url = classResource.toString(), suffix = c.getCanonicalName().replace('.', '/') + ".class";
        if (!url.endsWith(suffix)) {
            return new LocationUtil((URL) null);
        }
        String base = url.substring(0, url.length() - suffix.length()), path = base;
        if (path.startsWith("jar:")) {
            path = path.substring(4, path.length() - 2);
        }
        try {
            return new LocationUtil(new URL(path));
        } catch (MalformedURLException e) {
            return new LocationUtil((URL) null);
        }
    }

    @Nonnull
    public static LocationUtil getEMC() {
        if (FrameworkConstants.MAPPING_LOADER == FrameworkConstants.MappingsLoader.Forge) {
            try {
                return new LocationUtil(getModFileById("emc"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return getClassPhysicalLocation(Bootstrap.class);
    }

    @Nullable
    public File toFile() {
        if (file != null) {
            return file;
        }
        if (url != null) {
            String path = url.toString();
            if (path.startsWith("jar:")) {
                path = path.substring(4, path.indexOf("!/"));
            }
            try {
                if (OSUtils.isWindows() && path.matches("file:[A-Za-z]:.*")) {
                    path = "file:/" + path.substring(5);
                }
                return new File(new URL(path).toURI());
            } catch (Throwable ignored) { }
            if (path.startsWith("file:")) {
                return new File(path.substring(5));
            }
        }
        return null;
    }

}
