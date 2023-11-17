package me.deftware.client.framework.resource;

import net.minecraft.resources.IResource;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.io.IOException;
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
    public ResourceLocation getLocation() {
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
    public <T> T getMetadata(IMetadataSectionSerializer<T> serializer) {
        return null;
    }

    @Override
    public String getPackName() {
        return "Minecraft";
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }

}
