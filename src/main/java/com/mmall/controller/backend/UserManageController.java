package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created By Zzyy
 **/
@Controller
@RequestMapping("/manage/user")
public class UserManageController {

    @Autowired
    private IUserService iuserService;

    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> response =iuserService.login(username,password);
        if(response.isSuccess()){
            User user =response.getData();
            if(user.getRole()== Const.Role.ROLE_ADMIN){
                session.setAttribute(Const.CURRENT_USER,user);
            }else {
                 return ServerResponse.createdByErrorMessage("不是管理员，无法登陆");
            }
        }
        return response;
    }

}
