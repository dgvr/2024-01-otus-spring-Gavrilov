package ru.otus.hw.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(basePackages = "ru.otus.hw")
@PropertySource("classpath:application.properties")
public class AppConfig {

}
