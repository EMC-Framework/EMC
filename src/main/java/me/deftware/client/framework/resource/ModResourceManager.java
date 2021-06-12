package me.deftware.client.framework.resource;

import com.google.common.collect.Sets;
import me.deftware.client.framework.main.EMCMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Deftware
 */
public class ModResourceManager implements IResourceManager {

    private final Set<String> namespaces = Sets.newLinkedHashSet();
    private final ZipFile zipFile;
    private final String type;

    public ModResourceManager(EMCMod mod, String type) throws IOException {
        this.zipFile = getZipFile(mod);
        this.namespaces.add(mod.getMeta().getName().toLowerCase());
        this.type = type;
    }

    public ZipFile getZipFile(EMCMod mod) throws IOException {
        return new ZipFile(mod.physicalFile);
    }

    public ZipEntry getEntry(String name) {
        return zipFile.getEntry(type + "/" + name);
    }

    @Override
    public List<IResource> getAllResources(ResourceLocation id) throws IOException {
        return Collections.singletonList(getResource(id));
    }

    @Override
    public Set<String> getResourceDomains() {
        return namespaces;
    }

    @Override
    public IResource getResource(ResourceLocation id) throws IOException {
        ZipEntry entry = getEntry(id.getResourcePath());
        if (entry == null) {
            return Minecraft.getMinecraft().getResourceManager().getResource(id);
        }
        return new ModResource(zipFile.getInputStream(entry), id);
    }

}
