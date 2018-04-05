package com.sudnya.ecomm.Model;

/**
 * @ Created by Dell on 07-Sep-17.
 */

public class Todays_Offer_Model {
    private String id;
    private String name;
    private String image;
    private String offers;
    private String category, subCategory, productName, productCompany, productPrice, productPriceBeforeDiscount, productDescription, productImage1, productImage2, productImage3, shippingCharge, productAvailability;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage1() {
        return productImage1;
    }


}
