package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

/**
 * Created By Zzyy
 **/
public interface ICartService {

    ServerResponse<CartVo> addCart(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> updateCart(Integer userId,Integer productId,Integer count);

    ServerResponse<CartVo> deleteCart(Integer userId,String productIds);

    ServerResponse<CartVo> selectCart(Integer userId);

    ServerResponse<CartVo> checkOrUncheck(Integer userId,Integer productId,Integer checked);

    ServerResponse<Integer> getCartCount(Integer userId);
}
