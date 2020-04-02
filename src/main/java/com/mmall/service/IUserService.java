package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created By Zzyy
 **/
public interface IUserService {

    ServerResponse<User> login(String username, String password);

    ServerResponse<User> register(User user);

    ServerResponse<String> checkValid(String str,String type);

    ServerResponse selectQuestion(String username);

    ServerResponse checkAnswer(String username,String question,String answer);

    ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken);

    ServerResponse<String> loginResetPassword(User user,String passwordOld, String passwordNew);

    ServerResponse<User> updateUserInformation(User user);

    ServerResponse<User> getUserInformation(Integer userId);
}
