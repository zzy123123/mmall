package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.impl.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * Created By Zzyy
 **/
@Controller
@RequestMapping("/order/")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    IOrderService iOrderService;

    @RequestMapping(value = "pay.do")
    @ResponseBody
    public ServerResponse pay(HttpSession session, Long orderNumber, HttpServletRequest request){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createdByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(orderNumber,user.getId(),path);
    }

    @RequestMapping(value = "aliPay_callback.do")
    @ResponseBody
    public Object aliPayCallback(HttpServletRequest request) {
        Map<String, String> params = Maps.newHashMap();

        Map requestParams = request.getParameterMap();
        for (Iterator iterator = requestParams.keySet().iterator(); iterator.hasNext(); ) {
            String name = (String) iterator.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        logger.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());

        //验证回调正确性
        //避免重复通知
        params.remove("sign_type");
        boolean aliPayRsaCheckedV2 = false;
        try {
            aliPayRsaCheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            if(!aliPayRsaCheckedV2){
                return ServerResponse.createdByErrorMessage("非法请求，验证不通过！");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常！");
        }

        //回调处理
        ServerResponse serverResponse =iOrderService.aliCallBack(params);

        if(serverResponse.isSuccess()){
            return Const.AliPayCallBack.RESPONSE_SUCCESS;
        }else
            return Const.AliPayCallBack.RESPONSE_FAILED;
    }

    @RequestMapping(value = "query_order_pay_status.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session, Long orderNumber){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createdByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }

        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(user.getId(),orderNumber);
        if(serverResponse.isSuccess()){
            return ServerResponse.createdBySuccess(true);
        }else
            return ServerResponse.createdBySuccess(false);
    }
}
