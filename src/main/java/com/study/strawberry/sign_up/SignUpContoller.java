package com.study.strawberry.sign_up;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
// #### 이용약관 체크시 토큰 처리 컨트롤러
public class SignUpContoller {

    @Autowired
    private SqlSession sqlSession;

    @RequestMapping(value = "sdh_test")
    public ModelAndView home(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //mybatis 세션 호출
        SignUpMapper mapper = sqlSession.getMapper(SignUpMapper.class);

        //토큰 생성
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();

        //클라이언트 ip 추출
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        //토큰정보 입력 Map
        Map<String,Object> param = new HashMap<String,Object>();
        param.put("token", token);
        param.put("ip", ip);
        param.put("ad_agree_yn", "N");
        
        //토큰정보 db 입력
        mapper.insertToken(param);

        return new ModelAndView("example/example");
    }


}
