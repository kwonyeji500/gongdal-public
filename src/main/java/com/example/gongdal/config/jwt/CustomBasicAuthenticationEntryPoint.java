package com.example.gongdal.config.jwt;

import com.google.gson.Gson;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        Map responseMap = new HashMap();
        responseMap.put("success", false);
        responseMap.put("resultMsg", "인증 실패했습니다");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = new Gson().toJson(responseMap);
        response.getWriter().println(json);
    }

    @Override
    public void afterPropertiesSet() {
        super.setRealmName("GongDal");
        super.afterPropertiesSet();
    }
}
