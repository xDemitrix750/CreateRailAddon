Create Rail Addon - example source
=================================

What this is
------------
A minimal example Forge 1.20.1 mod that attempts to place short stretches of Create train tracks
(create:train_track) when chunks load. It's intended as a starting point for the addon you asked for.

Important notes / caveats
-------------------------
- This is a *starting prototype* — it is NOT a production-ready mod. The code places blocks on chunk load
  (a simple approach for demonstration) and uses a probabilistic check to reduce spam. It does not
  implement stations, branching, structure generation, or advanced integration with Create APIs.
- To build you need a proper Forge development environment with Java 17 and Gradle.
- The build.gradle declares a runtime dependency for Create (com.simibubi.create:create-1.20.1:6.0.0-83).
  You may need to adjust the version to the Create version you have and add the appropriate repository
  (Curse Maven or ModMaven) if Gradle cannot resolve it.
- After building, put the resulting JAR into your Minecraft 'mods' folder alongside Create and Forge.

How to build
------------
1. Install Java 17 and Gradle (or use the Gradle wrapper).
2. From the project root run: gradle build
3. The mod JAR will be in build/libs/

If you want, I can:
- try to add simple station structures as vanilla structure files (nbt) and a safer worldgen registration,
- extend the generator to make forks/branches or small dungeon structures,
- or attempt to cross-compile and provide a ready .jar (requires a build environment and careful testing).

