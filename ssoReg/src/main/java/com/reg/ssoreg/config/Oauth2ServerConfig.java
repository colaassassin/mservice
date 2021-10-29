package com.reg.ssoreg.config;

import com.reg.ssoreg.compont.JwtTokenEnhancer;
import com.reg.ssoreg.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.Resource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Configuration
@EnableAuthorizationServer
public class Oauth2ServerConfig extends AuthorizationServerConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenEnhancer jwtTokenEnhancer;
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userDetailsService;


    //定义客户端详细信息服务的配置器。客户详细信息可以初始化，或者可以引用现有的 store
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.inMemory()
                .withClient("client-app")//客户端id（必填）
                .secret(passwordEncoder.encode("123456"))//（要求用于受信任的客户端）客户端的机密，如果有的话
                .scopes("all")//客户范围限制。如果范围未定义或为空（默认），客户端将不受范围限制
                .authorizedGrantTypes("password", "refresh_token")//授权客户端使用的授予类型。默认值为空
                .accessTokenValiditySeconds(3600)//令牌过期时间
                .refreshTokenValiditySeconds(86400);//Refresh_token过期时间
    }

    //定义授权和令牌端点以及令牌服务
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //token增强链
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(jwtTokenEnhancer);
        delegates.add(accessTokenConverter());
        enhancerChain.setTokenEnhancers(delegates);//配置JWT的内容增强器
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService) //导入用户信息
                .accessTokenConverter(accessTokenConverter())
                .tokenEnhancer(enhancerChain);


    }

    //定义令牌端点上的安全约束
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //主要是让/oauth/token支持client_id以及client_secret作登录认证
        security.allowFormAuthenticationForClients();
    }


    //令牌解码
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair());
        return jwtAccessTokenConverter;
    }

    @Bean
    public KeyPair keyPair() {
        //从classpath下的证书中获取秘钥对
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "jbc5507929".toCharArray());
        return keyStoreKeyFactory.getKeyPair("jwt", "jbc5507929".toCharArray());
    }


}
