package com.xxq.dronerent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 无人机出租后台管理系统 - 启动类
 *
 * @author yourcompany
 * @since 2024-01-01
 */
@SpringBootApplication
@MapperScan("com.xxq.dronerent.mapper")
public class DroneRentApplication {

    public static void main(String[] args) {
        SpringApplication.run(DroneRentApplication.class, args);
        System.out.println("===========================================");
        System.out.println("   无人机出租后台管理系统启动成功！");
        System.out.println("   Knife4j接口文档: http://localhost:8080/doc.html");
        System.out.println("===========================================");
    }

}
