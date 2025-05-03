package com.healthapi.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app.file-storage")
class FileStorageProperties {
    var dataLocation: String = "data"
}
