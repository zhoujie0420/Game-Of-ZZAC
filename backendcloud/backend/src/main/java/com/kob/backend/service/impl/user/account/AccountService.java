package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.utils.JwtUtil;
import com.kob.backend.utils.RegularUtil;
import com.kob.backend.utils.UserUtil;
import com.kob.backend.utils.redis.RedisUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
@AllArgsConstructor
public class AccountService {

    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;

    public Map<String, String> photo(String photo) {  //修改用户信息

        Map<String, String> map = new HashMap<>();
        User user = UserUtil.getUser();

//        if (!RegularUtil.isAvatarUrl(photo)) {
//            map.put("error_message","图片路径错误, 请检查~");
//            return map;
//        }
        user.setPhoto(photo);
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("id", user.getId());
        userMapper.update(user, query);
        map.put("error_message", "success");
        return map;
    }

    public Map<String, String> getEmailToken(String email, String code) { //邮箱登录
        Map<String, String> map = new HashMap<>();
        if (!redisUtil.hasKey(email)) {
            System.out.println("不存在这个email");
            map.put("error_message", "邮箱错误");
            return map;
        }


        // 强制转化object会报  java.lang.Integer cannot be cast to java.lang.String 错误
        // String.valueOf 实质调用 object.toString() 方法
        String getcode = String.valueOf(redisUtil.get(email));
        System.out.println(getcode);
        System.out.println(code);
        if (!Objects.equals(code, getcode)) {
            map.put("error_message", "验证码错误");
            return map;
        }

        //下面是成功匹配后的操作
        //需要将 redis 的 key 删除
        redisUtil.del(email);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        User user = userMapper.selectOne(queryWrapper);

        //生成jwt (通过id)
        String jwt = JwtUtil.createJWT(user.getId().toString());

        map.put("error_message", "success");
        map.put("token", jwt);
        return map;
    }

    public Map<String, String> getToken(String username, String password) {  //登录
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        Authentication authenticate = authenticationManager.authenticate(authenticationToken); //登录失败会自己处理
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
        User user = loginUser.getUser(); // 取出user
        String jwt = JwtUtil.createJWT(user.getId().toString());

        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("token", jwt);

        return map;

    }


    public Map<String, String> getinfo() {  //获取用户信息
        User user = UserUtil.getUser();

        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("id", user.getId().toString());
        map.put("email", user.getEmail());
        map.put("username", user.getUsername());
        map.put("photo", user.getPhoto());
        return map;
    }

    public Map<String, String> register(String username, String password, String confirmedPassword, String email) {  //注册
        Map<String, String> map = new HashMap<>();

        if (username == null) {
            map.put("error_message", "用户名不能为空");
            return map;
        }

        if (password == null || confirmedPassword == null) {
            map.put("error_message", "密码不能为空");
            return map;
        }

        username = username.trim();    //删掉空白字符
        if (username.length() == 0) {
            map.put("error_message", "用户名不能为空");
            return map;
        }

        if (password.length() == 0 || confirmedPassword.length() == 0) {
            map.put("error_message", "密码不能为空");
            return map;
        }

        if (username.length() > 100) {
            map.put("error_message", "用户名长度不能大于100");
            return map;
        }

        if (password.length() > 100 || confirmedPassword.length() > 100) {
            map.put("error_message", "密码长度不能大于100");
            return map;
        }

        if (!password.equals(confirmedPassword)) {
            map.put("error_message", "两次输入的密码不一致");
            return map;
        }


        if (!RegularUtil.isEmail(email)) {
            map.put("error_message", "邮箱地址错误，重新填写");
            return map;
        }

        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("email", email);
        List<User> users1 = userMapper.selectList(queryWrapper1);
        if (!users1.isEmpty()) {
            map.put("error_message", "该邮箱已注册，请直接输入");
            return map;
        }


        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<User> users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()) {
            map.put("error_message", "用户名存在");
            return map;
        }

        String encodePassword = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/user/profile/photo/175167_lg_2151948c45.jpg";
        User user = new User(null, username, encodePassword, email, photo, 1500);
        userMapper.insert(user);

        map.put("error_message", "success");
        return map;
    }


}
