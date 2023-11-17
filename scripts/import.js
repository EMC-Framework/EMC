const { resolve } = require('path');
const { readdirSync, lstatSync, existsSync, copyFileSync, unlinkSync } = require('fs');

const versionRegex = /^[\d.]+$/
const args = process.argv.slice(2);
const root = resolve(__dirname, '../sources');
const common = resolve(__dirname, '..', 'common', 'src', 'main', 'java');

if (args.length < 1) {
    console.error('Usage: node import.js <file>...');
    process.exit(1);
}

for (const version of readdirSync(root)) {
    const path = resolve(root, version);
    const stat = lstatSync(path);
    if (!stat.isDirectory() || !versionRegex.test(version)) {
        continue;
    }
    console.log(`Modifying source ${version}`);
    for (const file of args) {
        const src = resolve(path, 'src', 'main', 'java', file);
        if (!existsSync(src)) {
            continue;
        }
        const dst = resolve(common, file);
        if (!existsSync(dst)) {
            console.log(`\tCopying ${src} to ${dst}`);
            copyFileSync(src, dst);
        }
        console.log(`Unlinking ${src}`);
        unlinkSync(src);
    }
}
