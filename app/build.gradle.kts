plugins {
    java
    war
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.glassfish:jakarta.faces:4.0.1")

    implementation("jakarta.enterprise:jakarta.enterprise.cdi-api:4.0.1")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")

    implementation("org.hibernate.orm:hibernate-core:6.3.0.Final")

    implementation("org.postgresql:postgresql:42.7.4")

    implementation("com.google.guava:guava:32.1.3-jre")
    implementation("com.google.code.gson:gson:2.10.1")

}

tasks.war {
    archiveBaseName.set("web3")
    archiveVersion.set("")

    from(sourceSets.main.get().output)
}

tasks.jar {
    enabled = false
}
