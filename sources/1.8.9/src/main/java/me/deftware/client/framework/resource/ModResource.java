package me.deftware.client.framework.resource;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.io.InputStream;

/**
 * @author Deftware
 */
public class ModResource implements IResource {

    private final InputStream stream;
    private final ResourceLocation id;

    public ModResource(InputStream stream, ResourceLocation id) {
        this.id = id;
        this.stream = stream;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return id;
    }

    @Override
    public InputStream getInputStream() {
        return stream;
    }

    @Override
    public boolean hasMetadata() {
        return false;
    }

    @Nullable
    @Override
    public <T extends IMetadataSection> T getMetadata(String s) {
        return null;
    }

    @Override
    public String getResourcePackName() {
        return "Minecraft";
    }

}
