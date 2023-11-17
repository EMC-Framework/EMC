package me.deftware.client.framework.main;

import com.google.gson.annotations.SerializedName;

public class ModMeta {

    @SerializedName("name")
    private String name;

    @SerializedName("website")
    private String website;

    @SerializedName("author")
    private String author;

    @SerializedName("minVersion")
    private String minVersion;

    @SerializedName("version")
    private int version;

    @SerializedName("main")
    private String main;

    @SerializedName("updateLinkOverride")
    private boolean updateLinkOverride;

    @SerializedName("scheme")
    private int scheme;

    public String getName() {
        return name;
    }

    public String getWebsite() {
        return website;
    }

    public String getAuthor() {
        return author;
    }

    public String getMinVersion() {
        return minVersion;
    }

    public int getVersion() {
        return version;
    }

    public String getMain() {
        return main;
    }

    public boolean isUpdateLinkOverride() {
        return updateLinkOverride;
    }

    public int getScheme() {
        return scheme;
    }

}
