# The EMC (Easy Minecraft Client) Framework

A cross-version Minecraft client framework that
supports Minecraft 1.8.9 through 1.20.3.

## Project Structure

Since there are numerous versions of EMC
for different Minecraft versions, there is a [common](common/) source directory that contains code used by all versions, to avoid duplicating code.

All versions of EMC are located in [sources](sources/).

## Building

For convenience, there is a build script that
automatically compiles each modern version of EMC (>= Minecraft 1.14.4):

```sh
$ node scripts/build.js
```

## License

EMC is licensed under MIT
