package me.deftware.client.framework.resource;

import java.io.InputStream;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;

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
    public InputStream open() {
        return stream;
    }

    @Override
    public String sourcePackId() {
        return id.toString();
    }

}
