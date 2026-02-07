dependencies {
    compileOnly ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")	

    implementation ("org.springframework.boot:spring-boot-starter-web:3.5.6")
    implementation ("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation ("org.springframework.boot:spring-boot-starter-data-jdbc")
    runtimeOnly ("com.h2database:h2")
}