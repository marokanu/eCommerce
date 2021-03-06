package com.shopme.admin.service.impl;

import java.util.List;

import com.shopme.common.error.CategoryNotFoundException;
import com.shopme.admin.util.CategoryPageInfo;
import com.shopme.common.entity.Category;

public interface ICategoryService {

    public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNum, String sortDir, String keyword);

    public List<Category> listCategoriesUsedInForm();

    public Category save(Category category);

    public void delete(Integer id) throws CategoryNotFoundException;

    public Category getID(Integer id) throws CategoryNotFoundException;

    public String checkUnique(Integer id, String name, String alias);

    public void updateCategoryEnabledStatus(Integer id, boolean enabled);
}