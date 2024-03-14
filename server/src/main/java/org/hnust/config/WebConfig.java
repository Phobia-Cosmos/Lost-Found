package org.hnust.config;

import lombok.extern.slf4j.Slf4j;
import org.hnust.interceptor.JwtTokenAdminInterceptor;
import org.hnust.interceptor.JwtTokenUserInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.Resource;

@Configuration
@Slf4j
public class WebConfig extends WebMvcConfigurationSupport {

    @Resource
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;
    @Resource
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    //    TODO：这个拦截器如何配置？什么时候发挥作用？
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");

        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/login")
                .excludePathPatterns("/user/user/register");  // Exclude GET, PUT, DELETE requests
    }

    @Bean
    public Docket docket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("失物招领项目接口文档")
                .version("2.0")
                .description("失物招领项目接口文档")
                .build();

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("管理端")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.hnust.controller.admin"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    @Bean
    public Docket docket1() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("失物招领项目接口文档")
                .version("2.0")
                .description("失物招领项目接口文档")
                .build();

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("用户端")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.hnust.controller.user"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}