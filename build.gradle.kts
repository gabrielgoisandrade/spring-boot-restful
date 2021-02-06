import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.21"
    kotlin("plugin.spring") version "1.4.21"
    kotlin("plugin.jpa") version "1.4.21"
}

group = "com.gga"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["springBootAdminVersion"] = "2.3.1"

dependencies {

    /* Security */
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("org.springframework.boot:spring-boot-starter-security")

    /* OpenAPI */
    implementation("org.springdoc:springdoc-openapi-ui:1.5.3")

    /* Content negotiation */
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")

    /* Model mapper */
    implementation("com.github.dozermapper:dozer-core:6.5.0")

    /* JPA */
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    /* HATEOAS */
    implementation("org.springframework.boot:spring-boot-starter-hateoas")

    /* Jackson - Serialização de objetos */
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    /* JUnit5 */
    implementation("org.junit.jupiter:junit-jupiter:5.4.2")

    /* DevTools */
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    /* PostgreSQL */
    runtimeOnly("com.h2database:h2")

    /* Kotlin */
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    /* Spring */
    implementation("org.springframework.boot:spring-boot-starter-web")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
    imports {
        mavenBom("de.codecentric:spring-boot-admin-dependencies:${property("springBootAdminVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}