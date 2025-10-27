dependencies {
    implementation ("ch.qos.logback:logback-classic")
    implementation("org.springframework:spring-context:6.1.3")
// или нужная версия
    //testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    //testImplementation("org.junit.jupiter:junit-jupiter")
    //testImplementation ("org.assertj:assertj-core")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
}

