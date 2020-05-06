package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created By Zzyy
 **/
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    IUserService iUserService;
    @Autowired
    ICategoryService iCategoryService;


    @RequestMapping(value = "add_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> addCategory(HttpSession session, String categoryName,
                                      @RequestParam(value = "parentId",defaultValue="0")int parentId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createdByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkUserAdminRole(user).isSuccess()){
            return iCategoryService.addCategory(categoryName,parentId);
        }else{
            return ServerResponse.createdByErrorMessage("无权限操作，需要Admin权限");
        }
    }

    @RequestMapping(value = "set_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> setCategory(HttpSession session, String categoryName,Integer categoryId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createdByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkUserAdminRole(user).isSuccess()){
                return iCategoryService.updateCategory(categoryName,categoryId);
        }else{
            return ServerResponse.createdByErrorMessage("无权限操作，需要Admin权限");
        }
    }

    @RequestMapping(value = "get_child_parallel_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getChildParallelCategory(HttpSession session,
                                                   @RequestParam(value = "categoryId",defaultValue="0")Integer categoryId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createdByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkUserAdminRole(user).isSuccess()){
            return iCategoryService.getChildParallelCategory(categoryId);
        }else{
            return ServerResponse.createdByErrorMessage("无权限操作，需要Admin权限");
        }
    }

    @RequestMapping(value = "get_child_category_by_recursion.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getChildCategoryByRecursion(HttpSession session,
                                                   @RequestParam(value = "categoryId",defaultValue="0")Integer categoryId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createdByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkUserAdminRole(user).isSuccess()){
            return iCategoryService.selectChildCategoryByRecursion(categoryId);
        }else{
            return ServerResponse.createdByErrorMessage("无权限操作，需要Admin权限");
        }
    }


}
