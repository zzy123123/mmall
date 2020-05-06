package com.mmall.service.impl;


import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;


/**
 * Created By Zzyy
 **/
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUserName(username);
        if(resultCount==0){
            return ServerResponse.createdByErrorMessage("用户名不存在");
        }

        String md5Password =MD5Util.MD5EncodeUtf8(password);//密码登录MD5

        User user =userMapper.selectLogin(username,md5Password);
        if(user==null){
            return ServerResponse.createdByErrorMessage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createdBySuccess("登陆成功",user);
    }

    public ServerResponse<String> register(User user){
        //checkValid方法复用
        ServerResponse validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSuccess()){
            return  validResponse;
        }
        validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess()){
            return  validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);//设置为普通用户
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword())); //MD5加密密码

        int resultCount=userMapper.insert(user);
        if(resultCount == 0){
            return ServerResponse.createdByErrorMessage("注册失败");
        }
        return ServerResponse.createdBySuccessMessage("注册成功");
    }

    public ServerResponse<String> checkValid(String str,String type) {
        if (StringUtils.isNotBlank(type)) {//如果type里的值不为null也不为空格
            //校验
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUserName(str);
                if (resultCount > 0) {
                    return ServerResponse.createdByErrorMessage("用户名已存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createdByErrorMessage("邮箱已存在");
                }
            }
        } else {
            return ServerResponse.createdByErrorMessage("参数错误");
        }
        return ServerResponse.createdBySuccessMessage("校验成功");
    }

    public ServerResponse<String> selectQuestion(String username){
        //checkValid方法复用
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            return ServerResponse.createdByErrorMessage("用户不存在");
        }
        String question= userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createdBySuccess(question);
        }
        return ServerResponse.createdByErrorMessage("找回密码问题为空");
    }

    public ServerResponse<String> checkAnswer(String username,String question,String answer){
        int resultCount =userMapper.checkAnswer(username,question,answer);
        if(resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken); //把forgetToken放入本地Cache中，设置有效期
            return ServerResponse.createdBySuccess(forgetToken);
        }
        return ServerResponse.createdByErrorMessage("问题答案错误");
    }

    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createdByErrorMessage("参数错误，Token需要传递");
        }
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            return ServerResponse.createdByErrorMessage("用户不存在");
        }
        String token=TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return  ServerResponse.createdByErrorMessage("Token无效或者过期");
        }

        if(StringUtils.equals(forgetToken,token)){
            String md5Password =MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount=userMapper.updatePasswordByUsername(username,md5Password);
            if(rowCount>0){
                return ServerResponse.createdBySuccessMessage("修改密码成功");
            }else {
                return ServerResponse.createdByErrorMessage("修改密码失败");
            }
        }
        return ServerResponse.createdByErrorMessage("Token错误，请重新获取密码重置的Token");
    }

    public ServerResponse<String> loginResetPassword(User user,String passwordOld, String passwordNew){
        //防止横向越权，校验指定用户的旧密码。因为我们会查询一个count(1),如果不指定id，那么结果就是true count>0
        int resultCount=userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(resultCount == 0){
            return ServerResponse.createdByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0){
            return ServerResponse.createdBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createdByErrorMessage("密码更新失败");
    }

    public ServerResponse<User> updateUserInformation(User user){
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount>0){
            return ServerResponse.createdByErrorMessage("Email已经存在，请更换Email再尝试更新");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount=userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount >0){
            return ServerResponse.createdBySuccess("更新个人信息成功",user);
        }
        return ServerResponse.createdByErrorMessage("更新个人信息失败");
    }

    public ServerResponse<User> getUserInformation(Integer userId){
        User user=userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createdByErrorMessage("找不到当前用户");
        }
        //保护
        user.setPassword(StringUtils.EMPTY);
        user.setQuestion(StringUtils.EMPTY);
        user.setAnswer(StringUtils.EMPTY);
        return ServerResponse.createdBySuccess(user);
    }


    //backend

    /**
     * 校验是否是管理员
     * @param user 用户
     * @return 返回
     */
    public ServerResponse<String> checkUserAdminRole(User user){
        if(user != null && user.getRole() ==Const.Role.ROLE_ADMIN){
            return ServerResponse.createdBySuccess("是管理员");
        }
        return ServerResponse.createdByErrorMessage("非管理员或用户不存在");
    }

}
