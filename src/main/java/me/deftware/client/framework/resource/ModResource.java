package me.deftware.client.framework.resource;

import net.minecraft.resource.Resource;
import net.minecraft.resource.metadata.ResourceMetadataReader;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Deftware
 */
public class ModResource extends Resource {

    private final InputStream stream;
    private final Identifier id;

    public ModResource(InputStream stream, Identifier id) {
        super(id.getPath(), () -> stream);
        this.id = id;
        this.stream = stream;
    }

    @Override
    public InputStream getInputStream() {
        return stream;
    }

    @Override
    public String getResourcePackName() {
        return "Minecraft";
    }

}
