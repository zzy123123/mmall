package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created By Zzyy
 **/
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    IUserService iUserService;

    @Autowired
    IProductService iProductService;

    @Autowired
    IFileService iFileService;

    @RequestMapping(value = "add_or_update_product.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addOrUpdateProduct(HttpSession session, Product product){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createdByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkUserAdminRole(user).isSuccess()){
            return iProductService.addOrUpdateProduct(product);
        }else{
            return ServerResponse.createdByErrorMessage("无权限操作，需要Admin权限");
        }
    }

    @RequestMapping(value = "set_product_sale_status.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setProductSaleStatus(HttpSession session, Integer productId,Integer status){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createdByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkUserAdminRole(user).isSuccess()){
            return iProductService.setProductSaleStatus(productId, status);
        }else{
            return ServerResponse.createdByErrorMessage("无权限操作，需要Admin权限");
        }
    }

    @RequestMapping(value = "get_product_detail.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getProductDetail(HttpSession session, Integer productId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createdByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkUserAdminRole(user).isSuccess()){
            return iProductService.getProductDetail(productId);
        }else{
            return ServerResponse.createdByErrorMessage("无权限操作，需要Admin权限");
        }
    }

    @RequestMapping(value = "get_product_list.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getProductList(HttpSession session, @RequestParam(value ="pageNUm",defaultValue = "1") int pageNUm, @RequestParam(value ="pageSize",defaultValue = "10")int pageSize  ){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createdByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkUserAdminRole(user).isSuccess()){
            return iProductService.getProductList(pageNUm,pageSize);
        }else{
            return ServerResponse.createdByErrorMessage("无权限操作，需要Admin权限");
        }
    }

    @RequestMapping(value = "search_product.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse searchProduct(HttpSession session, String productName,Integer productId,@RequestParam(value ="pageNUm",defaultValue = "1") int pageNUm, @RequestParam(value ="pageSize",defaultValue = "10")int pageSize  ){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createdByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(iUserService.checkUserAdminRole(user).isSuccess()){
            return iProductService.searchProduct(productName,productId,pageNUm,pageSize);
        }else{
            return ServerResponse.createdByErrorMessage("无权限操作，需要Admin权限");
        }
    }

    @RequestMapping(value = "upload.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(HttpSession session,@RequestParam(value ="upload_file",required = false) MultipartFile file, HttpServletRequest request){
//        User user=(User)session.getAttribute(Const.CURRENT_USER);
//        if(user == null){
//            return ServerResponse.createdByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
//        }
//        if(iUserService.checkUserAdminRole(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            Map fileMap = Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);
            return ServerResponse.createdBySuccess(fileMap);
//        }else{
//            return ServerResponse.createdByErrorMessage("无权限操作，需要Admin权限");
//        }
    }

    @RequestMapping(value = "rich_upload.do",method = RequestMethod.POST)
    @ResponseBody
    public Map richUpload(HttpSession session, @RequestParam(value ="upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
//        User user=(User)session.getAttribute(Const.CURRENT_USER);
//        if(user == null){
//            resultMap.put("sucess",false);
//            resultMap.put("msg","请登录管理员");
//            return resultMap;
//        }
        //富文本中对返回值有自己的要求，我们使用的是simditor，所以按照simditor的要求进行返回
//        https://simditor.tower.im/docs/doc-config.html
//        {
//            "success": true/false,
//                "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }
//        if(iUserService.checkUserAdminRole(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);
            if(StringUtils.isBlank(targetFileName)) {
                resultMap.put("sucess",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
                String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
                resultMap.put("sucess",true);
                resultMap.put("msg","上传成功");
                resultMap.put("file_path",url);
                response.addHeader("Access-Control-Allow-Headers","X-File-Name");
                return resultMap;

//        }else{
//            resultMap.put("sucess",false);
//            resultMap.put("msg","无权限操作");
//            return resultMap;
//        }
    }


}

