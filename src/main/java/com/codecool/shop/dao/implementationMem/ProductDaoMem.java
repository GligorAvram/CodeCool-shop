package com.codecool.shop.dao.implementationMem;


import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDaoMem implements ProductDao {

    private List<Product> data = new ArrayList<>();
    private static ProductDaoMem instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private ProductDaoMem() {
    }

    @Override
    public Product find(String  productName) {
        return data.stream().filter(t -> t.getName().equals(productName)).findFirst().orElse(null);
    }

    public static ProductDaoMem getInstance() {
        if (instance == null) {
            instance = new ProductDaoMem();
        }
        return instance;
    }

    @Override
    public void add(Product product) {
        product.setId(data.size() + 1);
        data.add(product);
    }


    public boolean isProductMissing(SupplierDao supplierDao, ProductCategoryDao productCategoryDao, String productNameInput, String defaultpriceInput, String defaultcurrencyInput, String descriptionInput, String productcategoryInput, String supplierInput, String imgInput) {
        BigDecimal defaultPrice=BigDecimal.valueOf(Integer.parseInt(defaultpriceInput));
//        Currency defaultCurrency = Currency.getInstance(defaultcurrencyInput.toUpperCase());
        String defaultCurrency = defaultcurrencyInput;

        //verific daca exista o categorie cu numele dat; daca nu atunci return false
        ProductCategory productCategory = null;
        var categories = productCategoryDao.getAll();
        for (ProductCategory category : categories) if (category.getName().equals(productcategoryInput)) productCategory = category;
        if (productCategory==null) return false;
        //verific daca exista o categorie cu numele dat; daca nu atunci return false
        Supplier supplier=null;
        var suppliers = supplierDao.getAll();
        for (Supplier standardSupplier : suppliers) if (standardSupplier.getName().equals(supplierInput)) supplier = standardSupplier;
        if (supplier==null) return false;
        //

        String img = imgInput;
        String name = productNameInput;
        String description = descriptionInput;
        Product newProduct = new Product(name,defaultPrice,defaultCurrency,description,productCategory,supplier,img);
        add(newProduct);
        return true;
    }


    @Override
    public Product find(int id) {
        return data.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void remove(int id) {
        data.remove(find(id));
    }

    @Override
    public List<Product> getAll() {
        return data;
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        return data.stream().filter(t -> t.getSupplier().equals(supplier)).collect(Collectors.toList());
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        return data.stream().filter(t -> t.getProductCategory().equals(productCategory)).collect(Collectors.toList());
    }
}
