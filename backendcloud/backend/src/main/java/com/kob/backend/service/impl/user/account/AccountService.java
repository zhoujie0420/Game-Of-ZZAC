package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.utils.JwtUtil;
import com.kob.backend.utils.RegularUtil;
import com.kob.backend.utils.UserUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;
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

    public Map<String,String>  photo(String photo) {  //修改用户信息

        Map<String, String> map = new HashMap<>();
        User user = UserUtil.getUser();

        if (!RegularUtil.isAvatarUrl(photo)) {
            map.put("error_message","图片路径错误, 请检查~");
            return map;
        }
        user.setPhoto(photo);
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("id", user.getId());
        userMapper.update(user, query);
        map.put("error_message", "success");
        return  map;
    }

    public Map<String, String> getToken(String username, String password) {  //登录
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username,password);

        Authentication authenticate = authenticationManager.authenticate(authenticationToken); //登录失败会自己处理
        UserDetailsImpl loginUser =  (UserDetailsImpl) authenticate.getPrincipal();
        User user =loginUser.getUser(); // 取出user
        String jwt = JwtUtil.createJWT(user.getId().toString());

        Map<String,String> map = new HashMap<>();
        map.put("error_message" ,"success");
        map.put("token",jwt);

        return map;

    }


    public Map<String, String> getinfo() {  //获取用户信息
        User user = UserUtil.getUser();

        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("id", user.getId().toString());
        map.put("username", user.getUsername());
        map.put("photo", user.getPhoto());
        return map;
    }

    public Map<String, String> register(String username, String password, String confirmedPassword) {  //注册
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

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<User> users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()) {
            map.put("error_message", "用户名存在");
            return map;
        }

        String encodePassword = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/user/profile/photo/175167_lg_2151948c45.jpg";
        User user = new User(null, username, encodePassword, photo, 1500);
        userMapper.insert(user);
        map.put("error_message", "success");
        return map;
    }






}
