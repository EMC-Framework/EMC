package me.deftware.client.framework;

public class FrameworkConstants {

    public static double VERSION = 13.7;
    public static int PATCH = 6;

    public static String AUTHOR = "Deftware";
    public static String FRAMEWORK_NAME = "EMC";
    public static MappingSystem MAPPING_SYSTEM = MappingSystem.Yarn;

	/*
		Mod info
	 */

    public static enum MappingSystem {
        Yarn, MCP
    }

}
