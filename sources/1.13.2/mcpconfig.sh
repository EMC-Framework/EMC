#!/bin/bash

if [ -d MCPConfig ]; then
    exit 0  
fi

git clone -b jitpack https://github.com/DimensionalDevelopment/MCPConfig.git
cd MCPConfig
chmod +x gradlew
./gradlew build
cd build/distributions
mkdir -p maven/com/github/DimensionalDevelopment/MCPConfig/jitpack-SNAPSHOT/
cp mcp_config-1.13.2.zip maven/com/github/DimensionalDevelopment/MCPConfig/jitpack-SNAPSHOT/MCPConfig-jitpack-SNAPSHOT.zip
