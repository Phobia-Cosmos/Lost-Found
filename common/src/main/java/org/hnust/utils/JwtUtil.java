package org.hnust.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

// 为什么要使用JWT？如何使用？
// 后端使用指定的密钥结合算法生成一个指定过期时间的密钥，并将这个密钥返回给前端，前端将该token存入请求的特定字段中（前后端共同协商），然后每一次请求都会携带该token
// 当后端的拦截器截到需要访问数据的请求时，就会根据session或者redis中存储的token来与请求头中的token作对照，成功则放行，否则要登陆（401错误码）。

/**
 * 生成jwt
 * 使用Hs256算法, 私匙使用固定秘钥
 * <p>
 * secretKey jwt秘钥
 * ttlMillis jwt过期时间(毫秒)
 * claims    设置的信息
 *
 * @return
 */

// 如何在这个类中注入JwtProperties？
// 其实没有必要在这里注入JwtProperties，我们只需要让使用该工具类的方法传入JWT所需参数，因为使用该工具类的肯定已经被Spring管理了；
// 上面的说法有问题，因为使用的类已经被管理，因此我们就需要在New的时候传入已经注入的JwtProperties即可，我们就不用在每次使用JWT方法时传入各个参数。

// 注意管理员和普通用户的Tokenkey不同

// TODO:难道static修饰的参数不需要注入就可以使用吗？

public class JwtUtil {

    // private JwtProperties jwtProperties;
    //
    // public JwtUtil(JwtProperties jwtProperties) {
    //     this.jwtProperties = jwtProperties;
    // }

    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        // String secretKey = jwtProperties.getAdminSecretKey();
        // long ttlMillis = jwtProperties.getAdminTtl();

        // 指定签名的时候使用的签名算法，也就是header那部分
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的时间
        // 这个过期时间一定是Date类型吗？对的，JJWT所需要的就是Date参数
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        // 设置jwt的body
        JwtBuilder builder = Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                // 设置签名使用的签名算法和签名使用的秘钥，注意这个密钥要转换为Bytes数组，而且还是URL编码允许的
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置过期时间
                .setExpiration(exp);

        return builder.compact();
    }

    /**
     * Token解密
     * <p>
     * secretKey jwt秘钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     *
     * @param token 加密后的token
     * @return
     */
    public static Claims parseJWT(String secretKey, String token) {
        // String secretKey = jwtProperties.getAdminSecretKey();
        Claims claims = Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置需要解析的jwt
                .parseClaimsJws(token).getBody();
        return claims;
    }

}
