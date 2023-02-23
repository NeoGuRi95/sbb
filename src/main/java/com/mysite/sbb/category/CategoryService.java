package com.mysite.sbb.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.mysite.sbb.exception.ErrorCode;
import com.mysite.sbb.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
	
	private final CategoryRepository categoryRepository;
	
	public List<Category> getList() {
		return categoryRepository.findAll();
	}
	
	public Category getCategory(String categoryName) {
        Optional<Category> category = this.categoryRepository.findByName(categoryName);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND);
        }
	}
}
