package com.mycelium.local.controller.product;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mycelium.local.dynamic.search.SearchCriteria;
import com.mycelium.local.dynamic.search.SearchManager;
import com.mycelium.local.repository.categorie.CategorieRepo;
import com.mycelium.local.repository.errorlog.ErrorLog;
import com.mycelium.local.repository.errorlog.ErrorLogRepo;
import com.mycelium.local.repository.integorderproduct.IntegOrderProduct;
import com.mycelium.local.repository.integorderproduct.IntegOrderProductRepo;
import com.mycelium.local.repository.integration.Integration;
import com.mycelium.local.repository.integration.IntegrationRepo;
import com.mycelium.local.repository.jsonlog.JsonLog;
import com.mycelium.local.repository.jsonlog.JsonLogRepo;
import com.mycelium.local.repository.picture.Picture;
import com.mycelium.local.repository.picture.PictureRepo;
import com.mycelium.local.repository.product.Product;
import com.mycelium.local.repository.product.ProductRepo;
import com.mycelium.local.repository.technical.Technical;
import com.mycelium.local.repository.technical.TechnicalRepo;
import com.mycelium.local.repository.user.UserRepo;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import jakarta.annotation.Nullable;
import jakarta.inject.Inject;

@Introspected
@JsonInclude(Include.ALWAYS)
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

    public ProductCreateRequest(String name, String desc, int categorieId, String brand, int weight, int quantity,
            int price, List<String> pictures, List<BasicTechnical> technical) {
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

    public ProductCreateRequest() {
    }
}

@Introspected
@JsonInclude(Include.ALWAYS)
class ProductImportRequest extends ProductCreateRequest {
    @Nullable
    public Integer id;

    public ProductImportRequest(String name, String desc, Integer categorieId, String brand, int weight, int quantity,
            int price, List<String> pictures, List<BasicTechnical> technical, Integer id) {
        super(name, desc, categorieId, brand, weight, quantity, price, pictures, technical);
        this.id = id;
    }

    public ProductImportRequest() {
        super();
    }
}

@Introspected
@JsonInclude(Include.ALWAYS)
class ProductExportResponse extends ProductCreateRequest {
    public Integer id;

    public ProductExportResponse(String name, String desc, Integer categorieId, String brand, int weight, int quantity,
            int price, List<String> pictures, List<BasicTechnical> technical, Integer id) {
        super(name, desc, categorieId, brand, weight, quantity, price, pictures, technical);
        this.id = id;
    }
}

@Introspected
@JsonInclude(Include.ALWAYS)
class BasicTechnical {
    public String type;
    public String value;

    public BasicTechnical() {
    }

    public BasicTechnical(String type, String value) {
        this.type = type;
        this.value = value;
    }
}

@Introspected
@JsonInclude(Include.ALWAYS)
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
    public Object categorieId;
    public String brand;
    public Integer weight;
    public Integer quantity;
    public Integer price;
    public List<String> pictures;
    public List<BasicTechnical> technical;

    public ProductResponse(Object id, @Nullable Integer integrationId, String name, String desc, String category,
            Object categorieId,
            String brand, Integer weight, Integer quantity, Integer price, List<String> pictures,
            List<BasicTechnical> technical) {
        this.id = id;
        this.integrationId = integrationId;
        this.name = name;
        this.desc = desc;
        this.category = category;
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
        return new ProductResponse(prod.id, null, prod.name, prod.desc, prod.categorie.name, prod.categorie.id,
                prod.brand, prod.weight,
                prod.quantity, prod.price, pics, techs);
    }

    static public ProductResponse fromProductBasic(Product prod) {
        List<String> pics = Lists.newArrayList();
        List<BasicTechnical> techs = Lists.newArrayList();
        return new ProductResponse(prod.id, null, prod.name, prod.desc, prod.categorie.name, prod.categorie.id,
                prod.brand, prod.weight,
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
    private JsonLogRepo jsonLogRepo;
    private ErrorLogRepo errorLogRepo;
    private UserRepo userRepo;

    public ProductController(ProductRepo productRepo, PictureRepo pictureRepo, CategorieRepo categorieRepo,
            TechnicalRepo technicalRepo, SearchManager searchManager, IntegrationRepo integrationRepo,
            IntegOrderProductRepo integOrderProductRepo, JsonLogRepo jsonLogRepo, UserRepo userRepo,
            ErrorLogRepo errorLogRepo) {
        this.productRepo = productRepo;
        this.pictureRepo = pictureRepo;
        this.categorieRepo = categorieRepo;
        this.technicalRepo = technicalRepo;
        this.searchManager = searchManager;
        this.integrationRepo = integrationRepo;
        this.integOrderProductRepo = integOrderProductRepo;
        this.jsonLogRepo = jsonLogRepo;
        this.userRepo = userRepo;
        this.errorLogRepo = errorLogRepo;
    }

    class EstimadoBody {
        public Integer categoryId;
        public Double salePrice;
        public Double boughtPrice;
        public Double porcentage;
        public Integer quantity;
        public Double weight;
        public Boolean international;
    }

    @Get("/")
    public List<ProductResponse> list() {
        return ProductResponse.fromProductList(productRepo.findAll());
    }

    @Get("/{id}")
    public ProductResponse get(int id) {
        var response = (productRepo.findById(id).get());
        // List<EstimadoBody> temp = Lists.newArrayList();

        // var t = new EstimadoBody();

        // t.categoryId = response.categorie.id;
        // t.salePrice = Double.valueOf(response.price);
        // t.boughtPrice = Double.valueOf(response.price);
        // t.porcentage = 0.3;
        // t.quantity = 1;
        // t.weight = Double.valueOf(response.weight);
        // t.international = false;

        // temp.add(t);

        // var r =
        // client.toBlocking().retrieve(HttpRequest.POST("http://mycelium-taxes/api/tax/estimate",
        // temp),
        // List.class);

        // response.price = ((Double) ((Map<?, ?>) r.get(0)).get("tax")).intValue();

        return ProductResponse.fromProduct(response);
    }

    @Get("/byCategory/{categorieId}")
    public List<ProductResponse> listPCategorie(String categorieId) {
        return search("", "", "", Lists.newArrayList(categorieId));
    }

    @Get("/topSales")
    public List<ProductResponse> listSales() {
        List<ProductResponse> res = new ArrayList<ProductResponse>();
        for (Product product : productRepo.findTop3Sales()) {
            List<String> urls = new ArrayList<String>();
            for (Picture picture : pictureRepo.findByProductId(product.id)) {
                urls.add(picture.url);
            }

            // List<EstimadoBody> temp = Lists.newArrayList();
            // var t = new EstimadoBody();

            // t.categoryId = product.categorie.id;
            // t.salePrice = Double.valueOf(product.price);
            // t.boughtPrice = Double.valueOf(product.price);
            // t.porcentage = 0.3;
            // t.quantity = 1;
            // t.weight = Double.valueOf(product.weight);
            // t.international = false;

            // temp.add(t);

            // var r =
            // client.toBlocking().retrieve(HttpRequest.POST("http://mycelium-taxes/api/tax/estimate",
            // temp),
            // List.class);

            Integer priceInt = product.price;

            // if (!r.isEmpty()) {
            // priceInt = ((Double) ((Map<?, ?>) r.get(0)).get("tax")).intValue();
            // }

            res.add(new ProductResponse(product.id, null, product.name, product.desc, product.categorie.name,
                    product.categorie.id,
                    product.brand, product.weight, product.quantity, priceInt, urls, Lists.newArrayList()));
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

            // List<EstimadoBody> temp = Lists.newArrayList();
            // var t = new EstimadoBody();

            // t.categoryId = product.categorie.id;
            // t.salePrice = Double.valueOf(product.price);
            // t.boughtPrice = Double.valueOf(product.price);
            // t.porcentage = 0.3;
            // t.quantity = 1;
            // t.weight = Double.valueOf(product.weight);
            // t.international = false;

            // temp.add(t);

            // var r =
            // client.toBlocking().retrieve(HttpRequest.POST("http://mycelium-taxes/api/tax/estimate",
            // temp),
            // List.class);

            Integer priceInt = product.price;

            // if (!r.isEmpty()) {
            // priceInt = ((Double) ((Map<?, ?>) r.get(0)).get("tax")).intValue();
            // }

            res.add(new ProductResponse(product.id, null, product.name, product.desc, product.categorie.name,
                    product.categorie.id,
                    product.brand, product.weight, product.quantity, priceInt, urls, Lists.newArrayList()));
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
            @Nullable @QueryValue(value = "categories", defaultValue = "") List<String> categories) {
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

        List<Integer> ids = Lists.newArrayList();
        for (var categId : categories) {
            try {
                ids.add(Integer.parseInt(categId.trim()));
            } catch (NumberFormatException e) {
                // Do nothing
            }
        }
        if (categories.size() > 0 && categories.get(0).trim() != "") {
            criteria.add(new SearchCriteria.CategoryIn(ids));
        }

        var products = ProductResponse.fromProductList(searchManager.search(criteria));

        for (var integ : integrationRepo.findAll()) {
            var url = "/api/search?";

            List<String> queryVals = Lists.newArrayList();

            if (query.trim() != "") {
                queryVals.add("q=" + URLEncoder.encode(query.trim(), StandardCharsets.UTF_8));
            }

            if (priceMaxStr.trim() != "") {
                queryVals.add("pricemax=" + URLEncoder.encode(priceMaxStr.trim(), StandardCharsets.UTF_8));
            }

            if (priceMinStr.trim() != "") {
                queryVals.add("pricemin=" + URLEncoder.encode(priceMinStr.trim(), StandardCharsets.UTF_8));
            }

            for (var categId : categories) {
                if (categId.trim() != "") {
                    queryVals.add("categories=" + URLEncoder.encode(categId.trim(), StandardCharsets.UTF_8));
                }
            }

            url += String.join("&", queryVals);

            List<?> response = client.toBlocking().retrieve(
                    HttpRequest.GET(integ.request + url),
                    List.class);

            for (var prod : response) {
                if (prod instanceof Map<?, ?> m) {
                    var res = new ProductResponse((String) m.get("_id"), integ.id, (String) m.get("name"),
                            (String) m.get("desc"), "", (String) m.get("categorie"), (String) m.get("brand"),
                            (Integer) m.get("weight"), (Integer) m.get("stock"), (Integer) m.get("price"), List.of(),
                            List.of());

                    client.toBlocking().retrieve(
                            HttpRequest.PUT(integ.request + "/api/products/search/" + (String) m.get("_id"), null),
                            String.class);

                    products.add(res);
                }
            }
        }

        products.sort((ProductResponse a, ProductResponse b) -> {
            return a.name.compareToIgnoreCase(b.name);
        });

        // for (var p : products) {
        // List<EstimadoBody> temp = Lists.newArrayList();
        // var t = new EstimadoBody();

        // if (p.integrationId == null) {
        // t.categoryId = (Integer) p.categorieId;
        // } else {
        // t.categoryId = 1;
        // }
        // t.salePrice = Double.valueOf(p.price);
        // t.boughtPrice = Double.valueOf(p.price);
        // t.porcentage = 0.30;
        // t.quantity = 1;
        // t.weight = Double.valueOf(p.weight);

        // if (p.integrationId == null) {
        // t.international = false;
        // } else {
        // t.international = true;
        // }

        // temp.add(t);

        // var r =
        // client.toBlocking().retrieve(HttpRequest.POST("http://mycelium-taxes/api/tax/estimate",
        // temp),
        // List.class);

        // p.price = ((Double) ((Map<?, ?>) r.get(0)).get("tax")).intValue();
        // }

        return products;
    }

    @Get("/{id_integration}/{id_producto}")
    public Map<?, ?> integrationProds(int id_integration, String id_producto) {
        var integ = integrationRepo.findById(id_integration).get();
        HttpRequest<?> request = HttpRequest.GET(integ.request + "/api/products/" + id_producto);
        Map<String, Object> map = client.toBlocking().retrieve(request, Map.class);
        map.put("id", (String) ((Map<?, ?>) map.get("_id")).get("$oid"));
        map.put("quantity", map.get("stock"));
        map.put("categorie", (String) ((Map<?, ?>) map.get("categorie")).get("$oid"));
        return map;
    }

    @Put("/{id_integration}/{id_producto}")
    public void addCountProd(int id_integration, String id_producto) {
        for (var integ : integrationRepo.findAll()) {
            if (integ.id == (id_integration)) {
                client.toBlocking().retrieve(
                        HttpRequest.PUT(integ.request + "/api/products/view/" + id_producto, null), String.class);
            }
        }
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Get("/export")
    public HttpResponse<List<ProductExportResponse>> exportProduct(Authentication authentication) {
        var userMap = authentication.getAttributes();
        var userId = (int) (long) userMap.get("id");

        List<ProductExportResponse> exportedProducts = Lists.newArrayList();

        var log = new JsonLog();

        var currDate = new Date();
        log.archiveName = "export_" + (currDate.getTime()) + ".json";
        log.error = 0;
        log.operation = false;
        log.success = 0;
        log.user = userRepo.findById(userId).get();
        log.when = currDate;
        log.errorLogs = Lists.newArrayList();

        for (var prod : productRepo.findAll()) {
            try {
                List<String> pics = Lists.newArrayList();
                for (var pic : prod.pictures) {
                    pics.add(pic.url);
                }

                List<BasicTechnical> techs = Lists.newArrayList();
                for (var tech : prod.technical) {
                    techs.add(new BasicTechnical(tech.type, tech.value));
                }

                exportedProducts.add(new ProductExportResponse(prod.name, prod.desc, prod.categorie.id, prod.brand,
                        prod.weight, prod.quantity, prod.price, pics, techs, prod.id));
                log.success += 1;
            } catch (Exception e) {
                var errorLog = new ErrorLog();
                errorLog.jsonLog = log;
                errorLog.message = e.toString();
                e.printStackTrace();

                log.errorLogs.add(errorLog);

                log.error += 1;
            }
        }

        jsonLogRepo.save(log);
        errorLogRepo.saveAll(log.errorLogs);

        var response = HttpResponse.ok(exportedProducts);
        response.header("Content-Disposition", "attachment; filename=\"" + log.archiveName + "\"");
        return response;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post("/import")
    public void importProduct(@Body List<ProductImportRequest> body, Authentication authentication) {
        var userMap = authentication.getAttributes();
        var userId = (int) (long) userMap.get("id");

        var log = new JsonLog();

        var currDate = new Date();
        log.archiveName = "-";
        log.error = 0;
        log.operation = false;
        log.success = 0;
        log.user = userRepo.findById(userId).get();
        log.when = currDate;
        log.errorLogs = Lists.newArrayList();

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
            try {
                Product prod = null;
                if (newProd.id != null) {
                    prod = prods.get(newProd.id);
                }

                boolean isNew;
                if (prod == null) {
                    prod = new Product();
                    isNew = true;
                } else {
                    isNew = false;
                }

                prod.brand = newProd.brand;
                prod.categorie = categorieRepo.findById(newProd.categorieId).get();
                prod.desc = newProd.desc;
                prod.name = newProd.name;

                pictureRepo.deleteAll(prod.pictures);
                prod.pictures.clear();
                for (var url : newProd.pictures) {
                    var pic = new Picture();
                    pic.product = prod;
                    pic.url = url;
                    prod.pictures.add(pic);
                }

                prod.price = newProd.price;
                prod.quantity = newProd.quantity;

                technicalRepo.deleteAll(prod.technical);
                prod.technical.clear();
                for (var newTech : newProd.technical) {
                    var tech = new Technical();
                    tech.product = prod;
                    tech.type = newTech.type;
                    tech.value = newTech.value;
                    prod.technical.add(tech);
                }

                prod.weight = newProd.weight;

                if (isNew) {
                    productRepo.save(prod);
                } else {
                    productRepo.update(prod);
                }
                pictureRepo.saveAll(prod.pictures);
                technicalRepo.saveAll(prod.technical);

                log.success += 1;
            } catch (Exception e) {
                var errorLog = new ErrorLog();
                errorLog.jsonLog = log;
                errorLog.message = e.toString();
                e.printStackTrace();

                log.errorLogs.add(errorLog);

                log.error += 1;
            }
        }

        jsonLogRepo.save(log);
        errorLogRepo.saveAll(log.errorLogs);
    }
}
