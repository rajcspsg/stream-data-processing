plugins {
    base
    java
}


allprojects {

    group = "streams-data-processing"
    version = "1.0"

    repositories {
        jcenter()
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        subprojects.forEach {
            compile("org.apache.kafka:kafka-streams:2.2.0")

            testImplementation("junit:junit:4.12")
        }
    }

}


