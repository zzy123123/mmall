package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
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
@RequestMapping("/user/")
public class UserController {

    @Autowired
    IUserService iUserService;

    /**
     *  登录
     * @param username 用户名
     * @param password 密码
     * @param session  时域
     * @return  返回
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST )
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse<User> response=iUserService.login(username,password);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    @RequestMapping(value = "logout.do",method = RequestMethod.POST )
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createdByErrorMessage("未登录，你退出个机掰");
        }
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createdBySuccess("退出成功");
    }

    @RequestMapping(value = "register.do",method = RequestMethod.POST )
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);

    }

    @RequestMapping(value = "check_valid.do",method = RequestMethod.POST )
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type){
        return iUserService.checkValid(str,type);
    }

    @RequestMapping(value = "get_current_user_information.do",method = RequestMethod.POST )
    @ResponseBody
    public ServerResponse<User> getCurrentUserInfo(HttpSession session){
      User user=(User)session.getAttribute(Const.CURRENT_USER);
      if(user != null){
          return ServerResponse.createdBySuccess(user);
      }
      return ServerResponse.createdByErrorMessage("用户未登录，无法获取当前用户的信息");
    }

    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST )
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }

    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST )
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
        return iUserService.checkAnswer(username,question,answer);
    }

    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        return iUserService.forgetResetPassword(username,passwordNew,forgetToken);
    }

    @RequestMapping(value = "login_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> loginResetPassword(HttpSession session,String passwordOld,String passwordNew){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createdByErrorMessage("用户未登录");
        }
        return iUserService.loginResetPassword(user,passwordOld,passwordNew);
    }

    @RequestMapping(value = "login_update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateUserInformation(HttpSession session,User user){
        User currentUser=(User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createdByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response=iUserService.updateUserInformation(currentUser);
        if(response.isSuccess()){
            response.getData().setUsername(currentUser.getUsername());
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    @RequestMapping(value = "get_user_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInformation(HttpSession session){
        User currentUser=(User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createdByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录,需要强制登录status=10");
        }
        return iUserService.getUserInformation(currentUser.getId());
    }






}

