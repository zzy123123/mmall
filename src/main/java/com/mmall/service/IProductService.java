package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * Created By Zzyy
 **/
public interface IProductService {

    ServerResponse addOrUpdateProduct(Product product);

    ServerResponse<String> setProductSaleStatus(Integer productId,Integer status);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse getProductList(int pageNum,int pageSize);

    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);
}
