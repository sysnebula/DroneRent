package com.xxq.dronerent.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j 配置类
 * 用于配置 API 文档相关信息
 *
 * @author yourcompany
 * @since 2024-01-01
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("无人机出租后台管理系统 API 文档")
                        .version("1.0.0")
                        .description("本系统提供无人机的租赁管理功能，包括用户管理、无人机管理、订单管理、租赁流程、库存维修管理、财务统计等模块")
                        .contact(new Contact()
                                .name("yourcompany")
                                .email("support@yourcompany.com")
                                .url("https://www.yourcompany.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
