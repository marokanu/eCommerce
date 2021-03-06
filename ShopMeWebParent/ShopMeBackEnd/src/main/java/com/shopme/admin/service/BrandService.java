package com.shopme.admin.service;

import com.shopme.admin.error.BrandNotFoundException;
import com.shopme.admin.repository.BrandRepository;
import com.shopme.admin.service.impl.IBrandService;
import com.shopme.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class BrandService implements IBrandService {

    public static final int BRANDS_PER_PAGE = 10;

    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> listAll() {

        Sort firstNameSorting = Sort.by("name").ascending();

        List<Brand> brandList = new ArrayList<>();

        brandRepository.findAll(firstNameSorting).forEach(brandList::add);

        return brandList;

    }

    @Override
    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand get(Integer id) throws BrandNotFoundException {
        try {
            return brandRepository.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new BrandNotFoundException("Could not find any brand with ID" + id);
        }
    }

    @Override
    public void delete(Integer id) throws BrandNotFoundException {

        Long countById = brandRepository.countById(id);

        if (countById == null || countById == 0) {
            throw new BrandNotFoundException("Could not find any brand with ID " + id);
        }

        brandRepository.deleteById(id);
    }

    @Override
    public String checkUnique(Integer id, String name) {

        boolean isCreatingNew = (id == null || id == 0);

        Brand brandByName = brandRepository.findByName(name);

        if (isCreatingNew) {
            if(brandByName != null) return "Duplicate";
        } else {
            if(brandByName != null && brandByName.getId() != id) {
                return "Duplicate";
            }
        }
        return "OK";
    }

    @Override
    public Page<Brand> listByPage(int pageNum, String sortField, String sortDir, String keyword) {

        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum-1, BRANDS_PER_PAGE, sort);

        if(keyword != null) {
            return brandRepository.findAll(keyword,pageable);
        }
        return brandRepository.findAll(pageable);
    }
}
