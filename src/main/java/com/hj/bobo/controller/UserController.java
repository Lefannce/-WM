package com.hj.bobo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hj.bobo.common.R;
import com.hj.bobo.entity.User;
import com.hj.bobo.service.UserService;
import com.hj.bobo.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;




    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();
        //电话不为空就生成一个随机验证码,
        if (StringUtils.isNotEmpty(phone)){
        String code = ValidateCodeUtils.generateValidateCode(4).toString();
        log.info("验证码为:{}",code);

        //把验证码保存到session
        //session.setAttribute(phone,code);
        //将生成的验证码缓存到Redis中，并且设置有效期为5分钟
        redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);

        return R.success("验证码发送成功");
        }
        return R.error("验证码发送失败");
    }





    @PostMapping("/login")
    public R<User> login(@RequestBody Map map , HttpSession session){

        //获取手机号
        String phone = map.get("phone").toString();

        //获取验证码
        String code = map.get("code").toString();

        //从Session中获取保存的验证码
        //Object codeInSession = session.getAttribute(phone);

        //从Redis中获取验证码
        Object codeInSession = redisTemplate.opsForValue().get(phone);

        //进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
        if(codeInSession != null && codeInSession.equals(code)){
            //如果能够比对成功，说明登录成功

            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);
            if(user == null){
                //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            redisTemplate.delete(phone);//登陆成功删除验证码
            return R.success(user);
        }
        return R.error("登录失败");
    }

    }






