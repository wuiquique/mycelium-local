plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.micronaut.application") version "3.7.0"
    id("com.google.cloud.tools.jib") version "2.8.0"
    id("io.micronaut.test-resources") version "3.7.0"
    id("org.sonarqube") version "3.5.0.2730"
    jacoco
}

sonar {
  properties {
    property("sonar.projectName", "Uat-Back")
    property("sonar.projectKey", "Uat-Back")
    property("sonar.host.url", "http://150.136.162.189:9997")
    property("sonar.login", "sqp_b1cbbe8166da0c644632b1bcf1dd42a77280aaf7")
  }
}

version = "0.1"

group = "com.mycelium.local"

repositories { mavenCentral() }

dependencies {
    annotationProcessor("io.micronaut.data:micronaut-data-processor:3.9.6")
    annotationProcessor("io.micronaut:micronaut-http-validation:3.8.4")
    annotationProcessor("io.micronaut.openapi:micronaut-openapi:4.8.3")
    annotationProcessor("io.micronaut.security:micronaut-security-annotations:3.9.2")
    implementation("io.micronaut:micronaut-http-client:3.8.4")
    implementation("io.micronaut:micronaut-jackson-databind:3.8.4")
    implementation("io.micronaut.reactor:micronaut-reactor:2.5.0")
    implementation("io.micronaut.reactor:micronaut-reactor-http-client:2.5.0")
    implementation("io.micronaut.data:micronaut-data-jdbc:3.9.6")
    implementation("io.micronaut.security:micronaut-security-jwt:3.9.2")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari:4.7.2")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.7")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    runtimeOnly("ch.qos.logback:logback-classic:1.2.11")
    runtimeOnly("com.oracle.database.jdbc:ojdbc8:21.7.0.0")
    runtimeOnly("com.h2database:h2")
    implementation("io.micronaut:micronaut-validation:3.8.4")
    implementation("io.micronaut.sql:micronaut-jdbc-ucp:4.7.2")
    implementation("com.google.guava:guava:31.1-jre")
    testImplementation("io.micronaut.test:micronaut-test-rest-assured:3.8.2")
}

application { mainClass.set("com.mycelium.local.Application") }

java {
    sourceCompatibility = JavaVersion.toVersion("17")
    targetCompatibility = JavaVersion.toVersion("17")
}

tasks { jib { to { image = "gcr.io/myapp/jib-image" } } }

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.isEnabled = true
    }
}

graalvmNative.toolchainDetection.set(false)

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.mycelium.local.*")
    }
}
