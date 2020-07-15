package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created By Zzyy
 **/
@Service("iShippingService")
public class ShippingServiceImpl  implements IShippingService {

    @Autowired
    ShippingMapper shippingMapper;

    public ServerResponse add(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if(rowCount > 0){
            Map result = Maps.newHashMap();
            result.put("shippingId",shipping.getId());
            return ServerResponse.createdBySuccess("新建地址成功",result);
        }
        return ServerResponse.createdByErrorMessage("新建地址失败");
    }

    public ServerResponse<String> delete(Integer userId, Integer shippingId){
        int resultCount = shippingMapper.deleteByUserIdAndShippingId(userId,shippingId);
        if(resultCount > 0){
            return ServerResponse.createdBySuccess("删除地址成功");
        }
        return ServerResponse.createdByErrorMessage("删除地址失败");
    }

    public ServerResponse update(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShippingIdAndUserId(shipping);
        if(rowCount > 0){
            Map result = Maps.newHashMap();
            result.put("shippingId",shipping.getId());
            return ServerResponse.createdBySuccess("更新地址成功",result);
        }
        return ServerResponse.createdByErrorMessage("更新地址失败");
    }

    public ServerResponse<Shipping> select(Integer userId, Integer shippingId){
        Shipping shipping = shippingMapper.selectByUserIdAndShippingId(userId,shippingId);
        if(shipping == null) {
            return ServerResponse.createdByErrorMessage("无法查询到该地址");
        }
        return ServerResponse.createdBySuccess("查询地址成功",shipping);
    }

    public ServerResponse<PageInfo> list(Integer userId, int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectAllByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return  ServerResponse.createdBySuccess(pageInfo);
    }
}
