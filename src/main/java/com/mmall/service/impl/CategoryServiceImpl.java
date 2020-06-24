package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created By Zzyy
 **/
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger= LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse<String> addCategory(String categoryName,Integer parentId){
        if(parentId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createdByErrorMessage("添加品类参数错误");
        }
        Category category=new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//分类可用

        int rowCount = categoryMapper.insert(category);
        if(rowCount>0){
            return ServerResponse.createdBySuccess("添加品类成功");
        }
        return ServerResponse.createdByErrorMessage("添加品类失败");
    }

    public ServerResponse<String> updateCategory(String categoryName,Integer categoryId){
        if(categoryId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createdByErrorMessage("添加品类参数错误");
        }
        Category category=new Category();
        category.setName(categoryName);
        category.setId(categoryId);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount>0){
            return ServerResponse.createdBySuccess("更新品类成功");
        }
        return ServerResponse.createdByErrorMessage("更新类失败");
    }

    public ServerResponse<List<Category>> getChildParallelCategory(Integer categoryId){
        List<Category> categoryList=categoryMapper.selectCategoryChildById(categoryId);//查询子节点的category信息，保持平级
        if(CollectionUtils.isEmpty(categoryList)){//判断集合是否为空
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createdBySuccess(categoryList);
    }


    /**
     * 递归查询本节点及其孩子结点的Id
     * @param categoryId
     * @return
     */
    public ServerResponse<List<Integer>> selectCategoryAndChildCategoryByRecursion(Integer categoryId){
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategoryByRecursion(categorySet,categoryId);
        List<Integer> categoryIdList= Lists.newArrayList();
        if(categoryId != null){
            for (Category categoryItem:categorySet){
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createdBySuccess(categoryIdList);
    }

    private Set<Category>  findChildCategoryByRecursion(Set<Category> categorySet,Integer categoryId){
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null){
            categorySet.add(category);
        }
        List<Category> categoryList = categoryMapper.selectCategoryChildById(categoryId);//mybatis返回集合不会是null

        for(Category categoryItem:categoryList){
            findChildCategoryByRecursion(categorySet,categoryItem.getId());
        } /**
            * 递归算法，算出子节点
            * 把方法的参数当做返回值返回给方法自身
            * 再拿方法自身的返回值当做方法的参数调用这个方法
            */

        return categorySet;

    }
}
