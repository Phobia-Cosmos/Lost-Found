package org.hnust.config;

import lombok.extern.slf4j.Slf4j;
import org.hnust.interceptor.JwtTokenAdminInterceptor;
import org.hnust.interceptor.JwtTokenUserInterceptor;
import org.hnust.interceptor.LoginInterceptor;
import org.hnust.interceptor.RefreshTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.*;
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

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    //    TODO：这个拦截器如何配置？什么时候发挥作用？
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/user/**")
                .excludePathPatterns(
                        "/user/v2/suggest/page",
                        "/user/v2/item/page",
                        "/**/upload/**",
                        "/user/v2/user/login",
                        "/user/v2/user/getCode",
                        "/user/v2/user/register"
                ).order(1);

        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).addPathPatterns("/user/**"
                , "/chatGroup/**", "/chatInfo/**").order(0);

        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/v2/**")
                .excludePathPatterns("/admin/v2/user/login")
                .excludePathPatterns("/admin/v2/user/upload")
                .excludePathPatterns("/admin/v2/user/register");  // Exclude GET, PUT, DELETE requests

        // registry.addInterceptor(jwtTokenUserInterceptor)
        //         .addPathPatterns("/user/**")
        //         .excludePathPatterns("/user/v2/user/login")
        //         .excludePathPatterns("/user/v2/user/qqlogin")
        //         .excludePathPatterns("/user/v2/user/register");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/user/v2/user/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowCredentials(true)
                        .allowedHeaders("*");
            }
        };
    }

    @Bean
    public Docket docket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("失物招领项目接口文档")
                .version("2.0")
                .description("失物招领项目接口文档")
                .build();

        // 指定使用Swagger2规范
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("管理端")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.hnust.controller.v2.admin"))
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
                .apis(RequestHandlerSelectors.basePackage("org.hnust.controller.v2.user"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    // 帮助您优化性能并有效管理资源访问。
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}