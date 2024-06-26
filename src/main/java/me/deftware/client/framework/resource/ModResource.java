package me.deftware.client.framework.resource;

import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import java.io.InputStream;

/**
 * @author Deftware
 */
public class ModResource extends Resource {

    private final InputStream stream;
    private final Identifier id;

    public ModResource(InputStream stream, Identifier id) {
        super(null, () -> stream);
        this.id = id;
        this.stream = stream;
    }

    @Override
    public InputStream getInputStream() {
        return stream;
    }

    @Override
    public String getPackId() {
        return id.toString();
    }

}
