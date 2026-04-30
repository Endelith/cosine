plugins {
    `java-library`
    `maven-publish`
}

group = "xyz.endelith.cosine"
version = "2.3"

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api("org.jspecify:jspecify:1.0.0")
    api("com.google.code.gson:gson:2.13.0")
    api("io.netty:netty-all:4.2.9.Final")
    api("com.github.Endelith.adventure:adventure-api:5.1.0-SNAPSHOT") 
    api("com.github.Endelith.adventure:adventure-nbt:5.1.0-SNAPSHOT")
    api("com.github.Endelith.adventure:adventure-text-serializer-nbt:5.1.0-SNAPSHOT")
    api("com.github.Endelith.adventure:adventure-text-serializer-json:5.1.0-SNAPSHOT")
    testImplementation("org.junit.jupiter:junit-jupiter-api:6.0.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.0.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.0.1")
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
        pom.licenses {
            license {
                name = "MIT"
                url = "https://choosealicense.com/licenses/mit/"
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
