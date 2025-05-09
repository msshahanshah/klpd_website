package com.klpdapp.klpd.Services;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.klpdapp.klpd.model.Category;
import com.klpdapp.klpd.model.Segment;
import com.klpdapp.klpd.Repository.CategoryRepo;
import com.klpdapp.klpd.Repository.SegmentRepo;

@Service
public class CategoryService {

    @Autowired
    CategoryRepo ctgRepo;

    @Autowired
    SegmentRepo segmentRepo;

    public void addCategoriesToModel(Model model) {
        List<Category> categories = ctgRepo.findAll();
        categories.sort(Comparator.comparing(Category::getCategoryName));

        Category partsAndAccessories = null;
        Category others = null;

        Iterator<Category> iterator = categories.iterator();
        while (iterator.hasNext()) {
            Category category = iterator.next();
            if ("Parts & Accessories".equalsIgnoreCase(category.getCategoryName())) {
                partsAndAccessories = category;
                iterator.remove();
            } else if ("Others".equalsIgnoreCase(category.getCategoryName())) {
                others = category;
                iterator.remove();
            }
        }

        if (partsAndAccessories != null) {
            categories.add(partsAndAccessories);
        }
        if (others != null) {
            categories.add(others);
        }
        List<Segment> segments = segmentRepo.findAll();
        model.addAttribute("segments", segments);
        model.addAttribute("categories", categories);
    }
    
}
