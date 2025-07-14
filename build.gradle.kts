plugins {
    id("java")
    id("com.gradleup.shadow") version "9.0.0-rc1"
    id("de.eldoria.plugin-yml.bukkit") version "0.7.1"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.momirealms.net/releases/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:${rootProject.properties["paper_version"]}-R0.1-SNAPSHOT")
    compileOnly("net.momirealms:craft-engine-core:${rootProject.properties["craftengine_version"]}")
    compileOnly("net.momirealms:craft-engine-bukkit:${rootProject.properties["craftengine_version"]}")
    compileOnly("net.momirealms:craft-engine-nms-helper:${rootProject.properties["nms_helper_version"]}")
    compileOnly("it.unimi.dsi:fastutil:${rootProject.properties["fastutil_version"]}")
    compileOnly("com.google.code.gson:gson:${rootProject.properties["gson_version"]}")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(21)
    dependsOn(tasks.clean)
}

tasks.processResources {
    filteringCharset = "UTF-8"
    filesMatching(arrayListOf("craft-engine-blocks.properties")) {
        expand(rootProject.properties)
    }
}

bukkit {
    main = "cn.gtemc.craftEngineBlocks.CraftEngineBlocks"
    version = rootProject.properties["project_version"] as String
    name = "CraftEngineBlocks"
    apiVersion = "1.20"
    author = "jhqwqmc"
    website = "https://github.com/jhqwqmc"
    depend = listOf("CraftEngine")
    foliaSupported = true
}

artifacts {
    archives(tasks.shadowJar)
}

tasks {
    shadowJar {
        archiveFileName = "${rootProject.name}-${rootProject.properties["project_version"]}.jar"
        destinationDirectory.set(file("$rootDir/target"))
        relocate("net.bytebuddy", "cn.gtemc.libraries.bytebuddy")
    }
}