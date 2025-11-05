plugins {
    java
    war
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:32.1.3-jre")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")

    implementation("jakarta.enterprise:jakarta.enterprise.cdi-api:4.0.1")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
}

// WAR configuration
tasks.war {
    archiveBaseName.set("web3")
    archiveVersion.set("")

    from(sourceSets.main.get().output)
}

tasks.jar {
    enabled = false
}
