package app.modules;

import app.util.*;
public class CategoryManager {
    private final SimpleSet<Category> categories;
    private final SimpleMap<String, SimpleArrayList<Expenditure>> categoryExpenditures;

    public CategoryManager() {
        categories = new SimpleSet<>();
        categoryExpenditures = new SimpleMap<>();
    }

    public boolean addCategory(Category category) {
        if (category == null || !category.isValid()) return false;
        if (categories.contains(category)) return false;
        categories.add(category);
        if (!categoryExpenditures.containsKey(category.getName())) {
            categoryExpenditures.put(category.getName(), new SimpleArrayList<>());
        }
        return true;
    }

    public boolean validateCategory(String categoryName) {
        if (categoryName == null) return false;
        for (Category c : categories) {
            if (c.getName().equalsIgnoreCase(categoryName)) return true;
        }
        return false;
    }

    public SimpleArrayList<Category> getAllCategories() {
        return categories.toList();
    }

    public boolean addExpenditureToCategory(String categoryName, Expenditure expenditure) {
        if (!validateCategory(categoryName) || expenditure == null) return false;
        SimpleArrayList<Expenditure> expenditures = categoryExpenditures.get(categoryName);
        if (expenditures != null) {
            expenditures.add(expenditure);
            return true;
        }
        return false;
    }
}