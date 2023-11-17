package me.deftware.client.framework.cosmetics;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface CosmeticProvider {

    List<CosmeticProvider> PROVIDERS = new ArrayList<>();

    void load(UUID id, Runnable callback);

    PlayerTexture getPlayerTexture(UUID id);

}
