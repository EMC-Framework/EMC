#!/bin/bash
git clone -b jitpack https://github.com/DimensionalDevelopment/MCPConfig.git
cd MCPConfig
chmod +x gradlew
./gradlew build -Dorg.gradle.java.home=~/.jdks/corretto-1.8.0_412
cd build/distributions
mkdir -p maven/com/github/DimensionalDevelopment/MCPConfig/jitpack-SNAPSHOT/
cp mcp_config-1.13.2.zip maven/com/github/DimensionalDevelopment/MCPConfig/jitpack-SNAPSHOT/MCPConfig-jitpack-SNAPSHOT.zip
