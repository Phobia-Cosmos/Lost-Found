package org.hnust.interceptor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JwtTest {

    @Test
    public void testGetJwtToken(HttpServletRequest request) {
        // Use the HttpServletRequest object in your test method
        String jwtToken = (String) request.getAttribute("jwtToken");
        System.out.println("JWT Token: " + jwtToken);
    }
}
