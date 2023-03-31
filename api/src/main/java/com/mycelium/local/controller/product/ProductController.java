package com.mycelium.local.controller.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycelium.local.dynamic.search.SearchCriteria;
import com.mycelium.local.dynamic.search.SearchManager;
import com.mycelium.local.repository.categorie.CategorieRepo;
import com.mycelium.local.repository.picture.Picture;
import com.mycelium.local.repository.picture.PictureRepo;
import com.mycelium.local.repository.product.Product;
import com.mycelium.local.repository.product.ProductRepo;
import com.mycelium.local.repository.technical.Technical;
import com.mycelium.local.repository.technical.TechnicalRepo;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.annotation.Nullable;

class ProductCreateRequest {
    public String name;
    public String desc;
    public int categorieId;
    public String brand;
    public int weight;
    public int quantity;
    public int price;
    public List<String> pictures;
    public List<Technical> technical;
}

class BasicTechnical {
    public String type;
    public String value;

    public BasicTechnical(String type, String value) {
        this.type = type;
        this.value = value;
    }
}

@Introspected
@JsonInclude(Include.ALWAYS)
class ProductResponse {
    public Integer id;
    public String name;
    public String desc;
    public int categorieId;
    public String brand;
    public int weight;
    public int quantity;
    public int price;
    public List<String> pictures;
    public List<BasicTechnical> technical;

    public ProductResponse(Integer id, String name, String desc, int categorieId,
            String brand, int weight, int quantity, int price, List<String> pictures, List<BasicTechnical> technical) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.categorieId = categorieId;
        this.brand = brand;
        this.weight = weight;
        this.quantity = quantity;
        this.price = price;
        this.pictures = pictures;
        this.technical = technical;
    }

    static public ProductResponse fromProduct(Product prod) {
        List<String> pics = Lists.newArrayList();
        for (var pic : prod.pictures) {
            pics.add(pic.url);
        }
        List<BasicTechnical> techs = Lists.newArrayList();
        for (var tech : prod.technical) {
            techs.add(new BasicTechnical(tech.type, tech.value));
        }
        return new ProductResponse(prod.id, prod.name, prod.desc, prod.categorie.id, prod.brand, prod.weight,
                prod.quantity, prod.price, pics, techs);
    }

    static public ProductResponse fromProductBasic(Product prod) {
        List<String> pics = Lists.newArrayList();
        List<BasicTechnical> techs = Lists.newArrayList();
        return new ProductResponse(prod.id, prod.name, prod.desc, prod.categorie.id, prod.brand, prod.weight,
                prod.quantity, prod.price, pics, techs);
    }

    static public List<ProductResponse> fromProductList(Iterable<Product> products) {
        List<ProductResponse> res = Lists.newArrayList();
        for (Product product : products) {
            res.add(ProductResponse.fromProduct(product));
        }
        return res;
    }
}

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/product")
public class ProductController {

    private ProductRepo productRepo;
    private PictureRepo pictureRepo;
    private CategorieRepo categorieRepo;
    private TechnicalRepo technicalRepo;
    private SearchManager searchManager;

    public ProductController(ProductRepo productRepo, PictureRepo pictureRepo, CategorieRepo categorieRepo,
            TechnicalRepo technicalRepo, SearchManager searchManager) {
        this.productRepo = productRepo;
        this.pictureRepo = pictureRepo;
        this.categorieRepo = categorieRepo;
        this.technicalRepo = technicalRepo;
        this.searchManager = searchManager;
    }

    @Get("/")
    public List<ProductResponse> list() {
        return ProductResponse.fromProductList(productRepo.findAll());
    }

    @Get("/{id}")
    public ProductResponse get(int id) {
        return ProductResponse.fromProduct(productRepo.findById(id).get());
    }

    @Get("/byCategory/{categorieId}")
    public List<ProductResponse> listPCategorie(int categorieId) {
        return ProductResponse.fromProductList(productRepo.findByCategorieId(categorieId));
    }

    @Get("/topSales")
    public List<ProductResponse> listSales() {
        List<ProductResponse> res = new ArrayList<ProductResponse>();
        for (Product product : productRepo.findTop3Sales()) {
            List<String> urls = new ArrayList<String>();
            for (Picture picture : pictureRepo.findByProductId(product.id)) {
                urls.add(picture.url);
            }
            res.add(new ProductResponse(product.id, product.name, product.desc, product.categorie.id,
                    product.brand, product.weight, product.quantity, product.price, urls, Lists.newArrayList()));
        }
        return res;
    }

    @Get("/lastBought")
    public List<ProductResponse> listLast() {
        List<ProductResponse> res = new ArrayList<ProductResponse>();
        for (Product product : productRepo.findLastBought()) {
            List<String> urls = new ArrayList<String>();
            for (Picture picture : pictureRepo.findByProductId(product.id)) {
                urls.add(picture.url);
            }
            res.add(new ProductResponse(product.id, product.name, product.desc, product.categorie.id,
                    product.brand, product.weight, product.quantity, product.price, urls, Lists.newArrayList()));
        }
        return res;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/")
    public String create(@Body ProductCreateRequest body) {
        var newProduct = new Product();

        newProduct.name = body.name;
        newProduct.desc = body.desc;
        newProduct.categorie = categorieRepo.findById(body.categorieId).get();
        newProduct.brand = body.brand;
        newProduct.weight = body.weight;
        newProduct.quantity = body.quantity;
        newProduct.price = body.price;

        productRepo.save(newProduct);

        for (int i = 0; i < body.pictures.size(); i++) {
            var newPic = new Picture();

            newPic.url = body.pictures.get(i);
            newPic.product = newProduct;

            pictureRepo.save(newPic);
        }

        for (int i = 0; i < body.technical.size(); i++) {
            var newTech = new Technical();

            newTech.type = body.technical.get(i).type;
            newTech.value = body.technical.get(i).value;
            newTech.product = newProduct;

            technicalRepo.save(newTech);

        }

        return "Success";
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Put("/{id}")
    public String update(int id, @Body ProductCreateRequest body) {
        var prod = productRepo.findById(id).get();

        prod.name = body.name;
        prod.desc = body.desc;
        prod.categorie = categorieRepo.findById(body.categorieId).get();
        prod.brand = body.brand;
        prod.weight = body.weight;
        prod.quantity = body.quantity;
        prod.price = body.price;

        productRepo.update(prod);

        for (var pic : prod.pictures) {
            pictureRepo.delete(pic);
        }
        for (int i = 0; i < body.pictures.size(); i++) {
            var newPic = new Picture();
            newPic.url = body.pictures.get(i);
            newPic.product = prod;
            pictureRepo.save(newPic);
        }

        for (var tech : prod.technical) {
            technicalRepo.delete(tech);
        }
        for (int i = 0; i < body.technical.size(); i++) {
            var newTech = new Technical();
            newTech.type = body.technical.get(i).type;
            newTech.value = body.technical.get(i).value;
            newTech.product = prod;
            technicalRepo.save(newTech);
        }

        return "Success";
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Delete("/{id}")
    public void delete(int id) {
        // TODO
    }

    @Get("/search")
    public List<ProductResponse> search(@Nullable @QueryValue(value = "q", defaultValue = "") String query,
            @Nullable @QueryValue(value = "pricemin", defaultValue = "") String priceMinStr,
            @Nullable @QueryValue(value = "pricemax", defaultValue = "") String priceMaxStr,
            @Nullable @QueryValue(value = "categories", defaultValue = "") String categoriesStr) {
        List<SearchCriteria> criteria = Lists.newArrayList();
        if (query.trim() != "") {
            criteria.add(new SearchCriteria.TextContains(query.trim()));
        }
        if (priceMinStr.trim() != "" && priceMaxStr.trim() != "") {
            try {
                criteria.add(new SearchCriteria.PriceBetween(Integer.parseInt(priceMinStr.trim()),
                        Integer.parseInt(priceMaxStr.trim())));
            } catch (NumberFormatException e) {
                // Do nothing
            }
        }
        if (categoriesStr.trim() != "") {
            List<Integer> ids = Lists.newArrayList();
            for (var categId : categoriesStr.split(",")) {
                try {
                    ids.add(Integer.parseInt(categId));
                } catch (NumberFormatException e) {
                    // Do nothing
                }
            }
            criteria.add(new SearchCriteria.CategoryIn(ids));
        }
        return ProductResponse.fromProductList(searchManager.search(criteria));
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Get("/export")
    public List<Map<String, Object>> export() {
        List<Map<String, Object>> res = Lists.newArrayList();

        for (var product : productRepo.findAll()) {
            Map<String, Object> map = Maps.newHashMap();

            map.put("id", product.id);
            map.put("name", product.name);
            map.put("desc", product.desc);
            map.put("brand", product.brand);
            map.put("weight", product.weight);
            map.put("quantity", product.quantity);
            map.put("price", product.price);

            List<String> pictures = Lists.newArrayList();
            for (var pic : product.pictures) {
                pictures.add(pic.url);
            }
            map.put("pictures", pictures);

            List<Map<String, String>> technical = Lists.newArrayList();
            for (var tech : product.technical) {
                Map<String, String> techMap = Maps.newHashMap();

                techMap.put("type", tech.type);
                techMap.put("value", tech.value);

                technical.add(techMap);
            }
            map.put("technical", technical);

            res.add(map);
        }

        return res;
    }
}
