package com.gateway.ssogateway.config;


import cn.hutool.core.util.ArrayUtil;
import com.gateway.ssogateway.compoent.IgnoreUrlsRemoveJwtFilter;
import com.gateway.ssogateway.compoent.RestAuthenticationEntryPoint;
import com.gateway.ssogateway.compoent.RestfulAccessDeniedHandler;
import com.gateway.ssogateway.constant.AuthConstant;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gateway.ssogateway.authorization.AuthorizationManager;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;


@AllArgsConstructor
@Configuration
@EnableWebFluxSecurity
    public class ResourceServerConfig {
        @Autowired
        private final AuthorizationManager authorizationManager; //鉴权管理器配置 - 验证授权
        @Autowired
        private final IgnoreUrlsConfig ignoreUrlsConfig;///白名单配置
        @Autowired
        private final RestfulAccessDeniedHandler restfulAccessDeniedHandler; //处理未进行授权的请求
        @Autowired
        private final RestAuthenticationEntryPoint restAuthenticationEntryPoint; //处理未认证的请求
        @Autowired
        private final IgnoreUrlsRemoveJwtFilter ignoreUrlsRemoveJwtFilter;//对白名单路径，直接移除JWT请求头


        //安全过滤器链
        @Bean
        public SecurityWebFilterChain springSecurityWebFilterChain(ServerHttpSecurity http){
            http.oauth2ResourceServer().jwt()
                    .jwtAuthenticationConverter(jwtAuthenticationConverter());

            //自定义处理JWT请求头过期或签名错误的结果
            http.oauth2ResourceServer().authenticationEntryPoint(restAuthenticationEntryPoint);
            //对白名单路径，直接移除JWT请求头
            http.addFilterBefore(ignoreUrlsRemoveJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);
            http.authorizeExchange()
                    .pathMatchers(ArrayUtil.toArray(ignoreUrlsConfig.getUrls(), String.class)).permitAll()
                    .anyExchange().access(authorizationManager)
                    .and().exceptionHandling() //执行处理
                    .accessDeniedHandler(restfulAccessDeniedHandler)//处理未授权
                    .authenticationEntryPoint(restAuthenticationEntryPoint)//处理未认证
                    .and().csrf().disable();
            return http.build();
        }


        @Bean
        public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter(){
            JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
            jwtGrantedAuthoritiesConverter.setAuthorityPrefix(AuthConstant.AUTHORITY_PREFIX);
            jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(AuthConstant.AUTHORITY_CLAIM_NAME);
            JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
            jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
            return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
        }
    }
