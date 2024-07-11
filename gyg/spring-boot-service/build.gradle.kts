plugins {
    java
    id("org.springframework.boot") version "3.0.4"
    id("io.spring.dependency-management") version "1.1.0"
    id("idea") apply true
}

group = "com.getyourguide"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

val hasIntegrationTests = File(projectDir.absolutePath + "/src/intTest").exists()

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }

    if(hasIntegrationTests) {
        val intTestImplementation by creating {
            extendsFrom(configurations["testImplementation"])
        }
        val intTestRuntimeOnly by creating {
            extendsFrom(configurations["testRuntimeOnly"])
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")

    if(hasIntegrationTests) {
        "intTestImplementation"("org.springframework.boot:spring-boot-starter-test")
        "intTestImplementation"("org.springframework.boot:spring-boot-starter-webflux")
    }
}

if (hasIntegrationTests) {
    sourceSets {
        create("intTest") {
            compileClasspath += sourceSets.main.get().output
            runtimeClasspath += sourceSets.main.get().output
        }
    }

    idea {
        module {
            val intTestSourceSets = java.sourceSets["intTest"]
            testSources.from(testSources.plus(intTestSourceSets.java.sourceDirectories))
            testResources.from(testResources.plus(intTestSourceSets.resources.sourceDirectories))
        }
    }
}

tasks {
    if (hasIntegrationTests) {
        val intTest by registering(Test::class) {
            description = "Runs the integration tests."
            group = "verification"
            testClassesDirs = sourceSets["intTest"].output.classesDirs
            classpath = sourceSets["intTest"].runtimeClasspath
            shouldRunAfter("test")
        }

        check {
            dependsOn(intTest)
        }
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs = listOf("-Xlint:deprecation", "-Xlint:unchecked")
    sourceCompatibility = "17"
    targetCompatibility = "17"
}

tasks.withType<Test> {
    useJUnitPlatform()
}
