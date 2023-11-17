const { spawn } = require('child_process');
const { resolve } = require('path');
const { readdirSync, lstatSync } = require('fs');

const legacy = /^1\.(8|12|13)\.[29]$/;
const versionRegex = /^[\d.]+$/
const root = resolve(__dirname, '../sources');

console.log("Building...");
for (const version of readdirSync(root)) {
    if (legacy.test(version)) {
        continue;
    }
    const path = resolve(root, version);
    const stat = lstatSync(path);
    if (!stat.isDirectory() || !versionRegex.test(version)) {
        continue;
    }
    const process = spawn('gradlew.bat', ['build'], {
        cwd: path
    });
    process.on('close', code => {
        if (code != 0) {
            console.error(`EMC ${version} failed to build`);
            return;
        }
        console.log(`EMC ${version} was successfully built`);
    });
}
