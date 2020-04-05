package me.deftware.client.framework;

public class FrameworkConstants {

    public static double VERSION = 13.9;
    public static int PATCH = 6;
    public static int SCHEME = 3;

    public static MappingsLoader MAPPING_LOADER = MappingsLoader.Fabric;
    public static MappingSystem MAPPING_SYSTEM = MappingSystem.YarnV2;

    public static String toDataString() {
        return String.format("EMC v%s version %s.%s using %s with %s mappings", SCHEME, VERSION, PATCH, MAPPING_LOADER.name(), MAPPING_SYSTEM.name());
    }

    public enum MappingSystem {
        Yarn, YarnV2, MCPConfig
    }

    public enum MappingsLoader {
        Fabric, MCP
    }

}
