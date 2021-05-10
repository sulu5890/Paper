plugins {
    java
    id("com.github.johnrengelman.shadow") version "6.1.0" apply false
    id("io.papermc.paperweight") version "1.0.0-SNAPSHOT"
}

group = "com.destroystokyo.paper"
version = providers.gradleProperty("projectVersion").forUseAtConfigurationTime().get()

val mcVersion = providers.gradleProperty("mcVersion")
val packageVersion = providers.gradleProperty("packageVersion")

allprojects {
    apply(plugin = "java")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(11))
        }
    }
}

subprojects {
    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(8)
    }

    if (name == "Paper-MojangAPI") {
        return@subprojects
    }

    repositories {
        mavenCentral()
        maven("https://repo1.maven.org/maven2/")
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://ci.emc.gs/nexus/content/groups/aikar/")
        maven("https://repo.md-5.net/content/repositories/releases/")
        maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    }
}

repositories {
    mavenLocal()

    maven("https://wav.jfrog.io/artifactory/repo/") {
        content {
            onlyForConfigurations("paperclip")
        }
    }
    maven("https://maven.quiltmc.org/repository/release/") {
        content {
            onlyForConfigurations("paramMappings", "remapper")
        }
    }
    maven("https://files.minecraftforge.net/maven/") {
        content {
            onlyForConfigurations("decompiler")
        }
    }
}

dependencies {
    paramMappings("org.quiltmc:yarn:1.16.5+build.6:mergedv2")
    remapper("org.quiltmc:tiny-remapper:0.3.2:fat@jar")
    decompiler("net.minecraftforge:forgeflower:1.5.498.5@jar")
    paperclip("io.papermc:paperclip:2.0.0-SNAPSHOT@jar")
}

paperweight {
    minecraftVersion.set(mcVersion)
    versionPackage.set(packageVersion)
    serverProject.set(project(":Paper-Server"))

    paper {
        mappingsPatch.set(file("build-data/mappings-patch.tiny"))

        additionalSpigotMemberMappings.set(file("build-data/additional-spigot-member-mappings.csrg"))

        craftBukkitPatchPatchesDir.set(file("build-data/craftbukkit-patch-patches"))
    }
}
