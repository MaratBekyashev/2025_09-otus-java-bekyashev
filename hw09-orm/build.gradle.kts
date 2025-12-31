dependencies {
    compileOnly ("org.projectlombok:lombok")
    annotationProcessor ("org.projectlombok:lombok")	

    implementation("org.flywaydb:flyway-core")
    implementation("com.zaxxer:HikariCP")
    implementation("org.postgresql:postgresql")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")

    implementation("ch.qos.logback:logback-classic")
}
