plugins {
    java
    war
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.glassfish:jakarta.faces:4.0.1")

    providedCompile("jakarta.servlet:jakarta.servlet-api:6.0.0")

    providedCompile("jakarta.enterprise:jakarta.enterprise.cdi-api:4.0.1")
    providedCompile("jakarta.inject:jakarta.inject-api:2.0.1")

    implementation("org.primefaces:primefaces:13.0.1:jakarta")

    implementation("org.hibernate.orm:hibernate-core:6.3.0.Final")

    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    implementation("org.postgresql:postgresql:42.7.7")

    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.slf4j:slf4j-simple:2.0.9")

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.guava:guava:32.1.2-jre")


    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.2")

    providedCompile("jakarta.ws.rs:jakarta.ws.rs-api:3.1.0")

    implementation("io.projectreactor:reactor-core:3.6.3")

    implementation("io.r2dbc:r2dbc-spi:1.0.0.RELEASE")
    implementation("io.r2dbc:r2dbc-pool:1.0.0.RELEASE")

    implementation("org.postgresql:r2dbc-postgresql:1.0.5.RELEASE")

}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    sourceCompatibility = "17"
    targetCompatibility = "17"
}

tasks.war {
    archiveBaseName.set("web3")
    archiveVersion.set("")

    from(sourceSets.main.get().output)
}

tasks.jar {
    enabled = false
}
