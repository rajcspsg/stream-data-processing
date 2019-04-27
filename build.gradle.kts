allprojects {
    group = "streams-data-processing"
    version = "1.0"
}

subprojects {

    apply(plugin = "java")
    apply(plugin = "application")
    
    repositories {
        jcenter()
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        "implementation"("org.apache.kafka:kafka-streams:2.2.0")
        "implementation"("org.apache.kafka:kafka-clients:2.2.0")
        "implementation"("org.slf4j:slf4j-api:1.7.25")
        "implementation"("org.slf4j:slf4j-log4j12:1.7.25")

        val testImplementation by configurations
        testImplementation("junit:junit:4.12")
    }

}


