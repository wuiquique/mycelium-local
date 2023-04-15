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
import com.mycelium.local.repository.integorderproduct.IntegOrderProduct;
import com.mycelium.local.repository.integorderproduct.IntegOrderProductRepo;
import com.mycelium.local.repository.integration.Integration;
import com.mycelium.local.repository.integration.IntegrationRepo;
import com.mycelium.local.repository.picture.Picture;
import com.mycelium.local.repository.picture.PictureRepo;
import com.mycelium.local.repository.product.Product;
import com.mycelium.local.repository.product.ProductRepo;
import com.mycelium.local.repository.technical.Technical;
import com.mycelium.local.repository.technical.TechnicalRepo;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.annotation.Nullable;
import jakarta.inject.Inject;

class ProductCreateRequest {
    public String name;
    public String desc;
    public int categorieId;
    public String brand;
    public int weight;
    public int quantity;
    public int price;
    public List<String> pictures;
    public List<BasicTechnical> technical;
}

class ProductImportRequest extends ProductCreateRequest {
    @Nullable
    public Integer id;
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
@JsonInclude
class IntegrationProductResponse {
    public Integer id;
    public String productId;
    public int quantity;
    public int price;
    public Integration integration;

    public IntegrationProductResponse(Integer id, String productId, int quantity, int price, Integration integration) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.integration = integration;
    }
}

@Introspected
@JsonInclude(Include.ALWAYS)
class ProductResponse {
    public Object id;
    @Nullable
    public Integer integrationId;
    public String name;
    public String desc;
    public String category;
    public String brand;
    public Integer weight;
    public Integer quantity;
    public Integer price;
    public List<String> pictures;
    public List<BasicTechnical> technical;

    public ProductResponse(Object id, @Nullable Integer integrationId, String name, String desc, String category,
            String brand, Integer weight, Integer quantity, Integer price, List<String> pictures,
            List<BasicTechnical> technical) {
        this.id = id;
        this.integrationId = integrationId;
        this.name = name;
        this.desc = desc;
        this.category = category;
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
        return new ProductResponse(prod.id, null, prod.name, prod.desc, prod.categorie.name, prod.brand, prod.weight,
                prod.quantity, prod.price, pics, techs);
    }

    static public ProductResponse fromProductBasic(Product prod) {
        List<String> pics = Lists.newArrayList();
        List<BasicTechnical> techs = Lists.newArrayList();
        return new ProductResponse(prod.id, null, prod.name, prod.desc, prod.categorie.name, prod.brand, prod.weight,
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

    @Inject
    @Client("/")
    HttpClient client;

    private ProductRepo productRepo;
    private PictureRepo pictureRepo;
    private CategorieRepo categorieRepo;
    private TechnicalRepo technicalRepo;
    private SearchManager searchManager;
    private IntegrationRepo integrationRepo;
    private IntegOrderProductRepo integOrderProductRepo;

    public ProductController(ProductRepo productRepo, PictureRepo pictureRepo, CategorieRepo categorieRepo,
            TechnicalRepo technicalRepo, SearchManager searchManager, IntegrationRepo integrationRepo,
            IntegOrderProductRepo integOrderProductRepo) {
        this.productRepo = productRepo;
        this.pictureRepo = pictureRepo;
        this.categorieRepo = categorieRepo;
        this.technicalRepo = technicalRepo;
        this.searchManager = searchManager;
        this.integrationRepo = integrationRepo;
        this.integOrderProductRepo = integOrderProductRepo;
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
            res.add(new ProductResponse(product.id, null, product.name, product.desc, product.categorie.name,
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
            res.add(new ProductResponse(product.id, null, product.name, product.desc, product.categorie.name,
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

    @Get("/report/supplier")
    public List<IntegrationProductResponse> reportsInetgrations() {
        var res = new ArrayList<IntegrationProductResponse>();
        for (IntegOrderProduct prod : integOrderProductRepo.findAll()) {
            res.add(new IntegrationProductResponse(prod.id, prod.productId, prod.quantity, prod.price,
                    prod.integration));
        }
        return res;
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

        if (priceMaxStr.trim() != "") {
            try {
                criteria.add(new SearchCriteria.PriceComparison(Integer.parseInt(priceMaxStr.trim()), false));
            } catch (NumberFormatException e) {
                // Do nothing
            }
        }

        if (priceMinStr.trim() != "") {
            try {
                criteria.add(new SearchCriteria.PriceComparison(Integer.parseInt(priceMinStr.trim()), true));
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

        var products = ProductResponse.fromProductList(searchManager.search(criteria));

        for (var integ : integrationRepo.findAll()) {
            List<?> response = client.toBlocking().retrieve(
                    HttpRequest.GET(integ.request + "/api/search"),
                    List.class);

            for (var prod : response) {
                if (prod instanceof Map<?, ?> m) {
                    var res = new ProductResponse((String) m.get("_id"), integ.id, (String) m.get("name"),
                            (String) m.get("desc"), (String) m.get("categorie"), (String) m.get("brand"),
                            (Integer) m.get("weight"), (Integer) m.get("stock"), (Integer) m.get("price"), List.of(),
                            List.of());

                    products.add(res);
                }
            }
        }

        return products;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Get("/export")
    public List<ProductResponse> exportProduct() {
        return ProductResponse.fromProductList(productRepo.findAll());
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Get("/{id_integration}/{id_producto}")
    public Map<?, ?> integrationProds(String id_integration, String id_producto) {
        try {
            HttpRequest<?> request = HttpRequest.GET("https://mycelium-international/api/products/{id_producto}");
            System.out.println(request);
            return client.toBlocking().retrieve(request, Map.class);
        } finally {

        }
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Get("/import")
    public void importProduct(@Body List<ProductImportRequest> body) {
        List<Integer> ids = Lists.newArrayList();
        for (var newProd : body) {
            if (newProd.id != null) {
                ids.add(newProd.id);
            }
        }

        Map<Integer, Product> prods = Maps.newHashMap();
        for (var prod : productRepo.findByIdInList(ids)) {
            prods.put(prod.id, prod);
        }

        for (var newProd : body) {
            if (newProd.id != null) {
                ids.add(newProd.id);
            }
        }
        // var prod = productRepo.findById(newProd.id).get();

        // prod.name = body.name;
        // prod.desc = body.desc;
        // prod.categorie = categorieRepo.findById(body.categorieId).get();
        // prod.brand = body.brand;
        // prod.weight = body.weight;
        // prod.quantity = body.quantity;
        // prod.price = body.price;

        // productRepo.update(prod);

        // for (var pic : prod.pictures) {
        // pictureRepo.delete(pic);
        // }
        // for (int i = 0; i < body.pictures.size(); i++) {
        // var newPic = new Picture();
        // newPic.url = body.pictures.get(i);
        // newPic.product = prod;
        // pictureRepo.save(newPic);
        // }

        // for (var tech : prod.technical) {
        // technicalRepo.delete(tech);
        // }
        // for (int i = 0; i < body.technical.size(); i++) {
        // var newTech = new Technical();
        // newTech.type = body.technical.get(i).type;
        // newTech.value = body.technical.get(i).value;
        // newTech.product = prod;
        // technicalRepo.save(newTech);
        // }
    }
}
