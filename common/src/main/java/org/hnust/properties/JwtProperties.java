package org.hnust.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// Component结合ConfigurationProperties实现配置类
@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

    private String adminSecretKey;
    private long adminTtl;
    private String adminTokenName;

    private String userSecretKey;
    private long userTtl;
    private String userTokenName;

}
