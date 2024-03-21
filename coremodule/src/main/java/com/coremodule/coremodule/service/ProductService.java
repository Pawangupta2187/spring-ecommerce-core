package com.coremodule.coremodule.service;

import com.coremodule.coremodule.entities.categories.Category;
import com.coremodule.coremodule.entities.categories.CategoryMetaDataFieldValues;
import com.coremodule.coremodule.entities.products.DTO.*;
import com.coremodule.coremodule.entities.products.Product;
import com.coremodule.coremodule.entities.products.ProductVariation;
import com.coremodule.coremodule.entities.users.PagingDefinationDTO;
import com.coremodule.coremodule.entities.users.Seller;
import com.coremodule.coremodule.exception.BadRequestException;
import com.coremodule.coremodule.exception.ConflictException;
import com.coremodule.coremodule.exception.NotFoundException;
import com.coremodule.coremodule.exception.SuccessResponse;
import com.coremodule.coremodule.repository.*;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    RegisterSellerRepository registerSellerRepository;

    @Autowired
    ModelMapper mm;

    @Autowired
    ProductVariationRepository productVariationRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryMetaDataFieldValuesRepository categoryMetaDataFieldValuesRepository;


    public ResponseEntity<SuccessResponse> addProduct(Product product, Long catId, String sellerEmail) {
        Seller seller = registerSellerRepository.findSellerByemailId(sellerEmail);
        Optional<Category> category = categoryRepository.findById(catId);
        if (category.isEmpty() || category.get().getChildCategory().size() != 0) {
            System.out.println(category.get().getChildCategory());
            throw new NotFoundException("Invalid Category id");
        }
        List<Product> existedProduct = productRepository.checkifProductExist(product.getName(), product.getBrand(), catId, seller.getId());
        if (existedProduct.size() != 0)
            throw new ConflictException("Product Already Exist");

        product.setCategory(category.get());
        product.setSeller(seller);
        productRepository.save(product);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("pg445406@gmail.com");//admin mail.
        mailMessage.setSubject("A New Product is Add");
        mailMessage.setFrom("pawan.gupta@tothenew.com");
        mailMessage.setText("Please check details and activate product "
                + "http://localhost:8080/viewcategory?catId=" + product.getId());//need to change cat url
        //  emailSenderService.sendEmail(mailMessage);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Product saved Succefully wait for activation"));
    }


    public Boolean CompareVariationmetadata(Map<Object, Object> metadata, Map<Object, Object> existedmetafieldvalues) {
        for (Map.Entry<Object, Object> entry : metadata.entrySet()) {
            if (existedmetafieldvalues.containsKey(entry.getKey())) {
                List<String> existedMetaValues = new ArrayList<>();
                Collections.addAll(existedMetaValues, existedmetafieldvalues.get(entry.getKey()).toString().split(","));
                if (!existedMetaValues.contains(entry.getValue()))
                    return false;
            } else {
                return false;
            }
        }
        return true;
    }

    public ResponseEntity<SuccessResponse> addVariation(Long productId, AddProductVariationDTO productVariationDTO) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty())
            throw new NotFoundException("Product id is not Valid");
        if (!product.get().getIsActive() || product.get().getIsDelete())
            throw new NotFoundException("Product id is Not Active or deleted");

        Map<Object, Object> metadata = productVariationDTO.getMetadata();
        Category category = product.get().getCategory();
        List<CategoryMetaDataFieldValues> categoryMetaDataFieldValues = categoryMetaDataFieldValuesRepository.findByCategory(category);
        Map<Object, Object> existedmetafieldvalues = new HashMap<>();
        for (CategoryMetaDataFieldValues categoryMetaDataFieldValue : categoryMetaDataFieldValues) {
            existedmetafieldvalues.put(categoryMetaDataFieldValue.getCategoryMetaDataField().getMetaName(), categoryMetaDataFieldValue.getValue());
        }
        ///compare that input metadata variation is exist or not like RAM 16(input ) then RAm and 16 GBmust exist
        if (!CompareVariationmetadata(metadata, existedmetafieldvalues))
            throw new BadRequestException("value or key is not exist in variation ");

        ProductVariation productVariation = new ProductVariation();
        mm.map(productVariationDTO, productVariation);
        JSONObject crunchifyObject = new JSONObject(metadata);
        productVariation.setMetadata(crunchifyObject);
        product.get().addProductVariation(productVariation);
        try {
            productRepository.save(product.get());
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Product variation added succesfully"));
    }


    public ViewProductDTO viewProduct(Long productId, String email) {
        Seller seller = registerSellerRepository.findSellerByemailId(email);
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty())
            throw new NotFoundException("invalid category");
        if (!product.get().getSeller().equals(seller))
            throw new NotFoundException("invalid seller");
        ViewProductDTO viewProductDTO = new ViewProductDTO();
        mm.map(product.get(), viewProductDTO);
        HashMap<Object, Object> categoryDetails = new HashMap<>();
        categoryDetails.put("id", product.get().getCategory().getId());
        categoryDetails.put("categoryName", product.get().getCategory().getCategoryName());
        viewProductDTO.setCategoryDetails(categoryDetails);
        return viewProductDTO;
    }

    public ViewVariationDTO viewProductVariation(Long variationId, String email) {
        Seller seller = registerSellerRepository.findSellerByemailId(email);
        Optional<ProductVariation> productVariation = productVariationRepository.findById(variationId);
        if (productVariation.isEmpty())
            throw new NotFoundException("invalid varitation");
        if (!productVariation.get().getProduct().getSeller().equals(seller))
            throw new NotFoundException("invalid user");
        if (productVariation.get().getProduct().getIsDelete())
            throw new NotFoundException("product is deleted");
        ViewVariationDTO viewVariationDTO = new ViewVariationDTO();
        mm.map(productVariation.get(), viewVariationDTO);
        viewVariationDTO.setProductdetail(viewProduct(productVariation.get().getProduct().getId(), email));
        Map<String, Object> metadata = productVariation.get().getMetadata().toMap();
        viewVariationDTO.setVariation(metadata);
        return viewVariationDTO;
    }

    public Page<ViewProductDTO> getALlSellerProduct(String email, PagingDefinationDTO pagingDefinationDTO) {
        Pageable pageable = PageRequest.of(pagingDefinationDTO.getOffSet(), pagingDefinationDTO.getPageSize(), (Sort.by(new Sort.Order(null, pagingDefinationDTO.getSortBY()))));
        Seller seller = registerSellerRepository.findSellerByemailId(email);
        List<Product> products = productRepository.findBySeller(seller, pageable);
        List<ViewProductDTO> viewProductDTOList = new ArrayList<>();
        for (Product product : products) {
            ViewProductDTO viewProductDTO = new ViewProductDTO();
            mm.map(product, viewProductDTO);
            HashMap<Object, Object> categoryDetails = new HashMap<>();
            categoryDetails.put("id", product.getCategory().getId());
            categoryDetails.put("categoryName", product.getCategory().getCategoryName());
            viewProductDTO.setCategoryDetails(categoryDetails);
            viewProductDTOList.add(viewProductDTO);
        }

        return new PageImpl<>(viewProductDTOList, pageable, viewProductDTOList.size());
    }

    public Page<ViewVariationDTO> viewProductAllVariation(Long productId, String email, PagingDefinationDTO pagingDefinationDTO) {
        Seller seller = registerSellerRepository.findSellerByemailId(email);
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty())
            throw new NotFoundException("product is not valid");
        if (!product.get().getSeller().getId().equals(seller.getId()))
            throw new NotFoundException("user is not valid");
        Pageable pageable = PageRequest.of(pagingDefinationDTO.getOffSet(), pagingDefinationDTO.getPageSize(), (Sort.by(new Sort.Order(null, pagingDefinationDTO.getSortBY()))));
        List<ProductVariation> productVariations = productVariationRepository.findByProduct(product.get(), pageable);
        List<ViewVariationDTO> variationList = new ArrayList<>();
        for (ProductVariation productVariation : productVariations) {
            if (productVariation.getProduct().getIsDelete())
                continue;
            ViewVariationDTO viewVariationDTO = new ViewVariationDTO();
            mm.map(productVariation, viewVariationDTO);
            Map<String, Object> metadata = productVariation.getMetadata().toMap();
            viewVariationDTO.setVariation(metadata);
            System.out.println(metadata);
            variationList.add(viewVariationDTO);
        }
        return new PageImpl<>(variationList, pageable, variationList.size());
    }

    public ResponseEntity<SuccessResponse> deleteProduct(Long productId, String email) {
        Seller seller = registerSellerRepository.findSellerByemailId(email);
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty())
            throw new NotFoundException("product is not valid");
        if (!product.get().getSeller().getId().equals(seller.getId()))
            throw new NotFoundException("user is not valid");
        product.get().setIsDelete(true);
        try {
            productRepository.save(product.get());
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Product is Deleted"));
    }

    public ResponseEntity<SuccessResponse> updateProduct(UpdateProductDTO productDTO, Long productId, String email) {
        Seller seller = registerSellerRepository.findSellerByemailId(email);
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty() || product.get().getIsDelete())
            throw new NotFoundException("product is not valid");
        if (!product.get().getSeller().getId().equals(seller.getId()))
            throw new NotFoundException("user is not valid");
        List<Product> existedProduct = productRepository.checkifProductExist(productDTO.getName(), product.get().getBrand(), product.get().getCategory().getId(), seller.getId());
        if (existedProduct.size() != 0)
            throw new ConflictException("Product Already Exist");
        mm.map(productDTO, product.get());
        try {
            productRepository.save(product.get());
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Product is Updated"));
    }

    public ResponseEntity<SuccessResponse> updateVariation(AddProductVariationDTO productVariationDTO, Long variationId, String email) {
        Optional<ProductVariation> productVariation = productVariationRepository.findById(variationId);
        if (productVariation.isEmpty())
            throw new NotFoundException("ProductVariation id is not Valid");
        if (!productVariation.get().getProduct().getIsActive() || productVariation.get().getProduct().getIsDelete())
            throw new NotFoundException("Product id is Not Active or deleted");
        Seller seller = registerSellerRepository.findSellerByemailId(email);
        if (!productVariation.get().getProduct().getSeller().getId().equals(seller.getId()))
            throw new NotFoundException("user is not valid");
        List<CategoryMetaDataFieldValues> categoryMetaDataFieldValues = categoryMetaDataFieldValuesRepository.findByCategory(productVariation.get().getProduct().getCategory());
        Map<Object, Object> existedmetafieldvalues = new HashMap<>();
        for (CategoryMetaDataFieldValues categoryMetaDataFieldValue : categoryMetaDataFieldValues) {
            existedmetafieldvalues.put(categoryMetaDataFieldValue.getCategoryMetaDataField().getMetaName(), categoryMetaDataFieldValue.getValue());
        }
        ///compare that input metadata variation is exist or not like RAM 16(input ) then RAm and 16 GBmust exist
        Map<Object, Object> metadata = productVariationDTO.getMetadata();
        if (!CompareVariationmetadata(metadata, existedmetafieldvalues))
            throw new BadRequestException("value or key is not exist in variation ");
        Product product = productVariation.get().getProduct();
        mm.map(productVariationDTO, productVariation.get());
        productVariation.get().setProduct(product);
        try {
            productVariationRepository.save(productVariation.get());
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Product variation updated"));
    }

    public List<ViewVariationDTO> makeListOfVairation(Set<ProductVariation> productVariations) {
        List<ViewVariationDTO> viewVariationList = new ArrayList<>();
        for (ProductVariation variation : productVariations) {
            ViewVariationDTO viewVariationDTO = new ViewVariationDTO();
            mm.map(variation, viewVariationDTO);
            Map<String, Object> metadata = variation.getMetadata().toMap();
            viewVariationDTO.setVariation(metadata);
            viewVariationList.add(viewVariationDTO);
        }
        return viewVariationList;
    }

    //customer
    public CustomerViewProductDTO customerViewProduct(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty() || product.get().getIsDelete() || !product.get().getIsActive())
            throw new NotFoundException("invalid category Id");
        if (product.get().getProductVariations().size() == 0)
            throw new NotFoundException("Not Found any variation");

        Set<ProductVariation> productVariations = product.get().getProductVariations();
        List<ViewVariationDTO> viewVariationList = makeListOfVairation(productVariations);
        CustomerViewProductDTO customerViewProductDTO = new CustomerViewProductDTO();
        mm.map(product.get(), customerViewProductDTO);
        HashMap<Object, Object> categoryDetails = new HashMap<>();
        categoryDetails.put("id", product.get().getCategory().getId());
        categoryDetails.put("categoryName", product.get().getCategory().getCategoryName());
        customerViewProductDTO.setCategoryDetails(categoryDetails);
        customerViewProductDTO.setVariations(viewVariationList);
        return customerViewProductDTO;
    }

    public List<Category> findAllChildProductRecursive(Category category) {
        if (category.getProducts().size() != 0) {
            List<Category> categories = new ArrayList<>();
            categories.add(category);
            return categories;
        }
        if (category.getChildCategory().size() != 0) {
            List<Category> categories = new ArrayList<>();
            for (Category cat : category.getChildCategory()) {
                List<Category> categories1 = findAllChildProductRecursive(cat);
                if (categories1 != null)
                    categories.addAll(categories1);
            }
            return categories;
        }
        return null;
    }

    public Page<CustomerViewProductDTO> customerViewAllProduct(Long categoryId) {
        PagingDefinationDTO pagingDefinationDTO = new PagingDefinationDTO();
        Pageable pageable = PageRequest.of(pagingDefinationDTO.getOffSet(), pagingDefinationDTO.getPageSize(), (Sort.by(new Sort.Order(null, pagingDefinationDTO.getSortBY()))));

        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty())
            throw new NotFoundException("category id is not valid");
        Set<Product> products = new HashSet<>();//category.get().getProducts();
        //id is not a leaf category
        if (category.get().getChildCategory() != null) {
            List<Category> categories = findAllChildProductRecursive(category.get());
            for (Category cat : categories) {
                System.out.println("cat=" + cat.getCategoryName());
                products.addAll(cat.getProducts());
            }
        } else {
            products.addAll(category.get().getProducts());
        }
        if (products.size() == 0)
            throw new NotFoundException("NO product exist of category");
        List<CustomerViewProductDTO> customerViewProductDTOList = new ArrayList<>();
        for (Product product : products) {
            if (product.getProductVariations().size() == 0)
                continue;
            Set<ProductVariation> productVariations = product.getProductVariations();
            List<ViewVariationDTO> viewVariationList = makeListOfVairation(productVariations);
            CustomerViewProductDTO customerViewProductDTO = new CustomerViewProductDTO();
            mm.map(product, customerViewProductDTO);
            HashMap<Object, Object> categoryDetails = new HashMap<>();
            categoryDetails.put("id", product.getCategory().getId());
            categoryDetails.put("categoryName", product.getCategory().getCategoryName());
            customerViewProductDTO.setCategoryDetails(categoryDetails);
            customerViewProductDTO.setVariations(viewVariationList);
            customerViewProductDTOList.add(customerViewProductDTO);
        }

        return new PageImpl<>(customerViewProductDTOList, pageable, customerViewProductDTOList.size());
    }

    public Page<CustomerViewProductDTO> customerAllsimilarProduct(Long productId, PagingDefinationDTO pagingDefinationDTO) {
        Optional<Product> existproduct = productRepository.findById(productId);
        if (existproduct.isEmpty())
            throw new NotFoundException("product id is not valid");
        Pageable pageable = PageRequest.of(pagingDefinationDTO.getOffSet(), pagingDefinationDTO.getPageSize(), (Sort.by(new Sort.Order(null, pagingDefinationDTO.getSortBY()))));
        List<Product> products = productRepository.findSimilarProduct(existproduct.get().getName(), existproduct.get().getBrand(), existproduct.get().getCategory().getId(), pageable);
        if (products.size() == 0)
            throw new NotFoundException("NO similar Product exist");
        List<CustomerViewProductDTO> customerViewProductDTOList = new ArrayList<>();
        for (Product product : products) {
            if (product.getProductVariations().size() == 0)
                continue;
            Set<ProductVariation> productVariations = product.getProductVariations();
            List<ViewVariationDTO> viewVariationList = makeListOfVairation(productVariations);
//
            CustomerViewProductDTO customerViewProductDTO = new CustomerViewProductDTO();
            mm.map(product, customerViewProductDTO);
            HashMap<Object, Object> categoryDetails = new HashMap<>();
            categoryDetails.put("id", product.getCategory().getId());
            categoryDetails.put("categoryName", product.getCategory().getCategoryName());
            customerViewProductDTO.setCategoryDetails(categoryDetails);
            customerViewProductDTO.setVariations(viewVariationList);
            customerViewProductDTOList.add(customerViewProductDTO);
        }

        return new PageImpl<>(customerViewProductDTOList, pageable, customerViewProductDTOList.size());
    }


    //admin
    public AdminViewProductDTO adminViewProduct(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty() || product.get().getIsDelete() || !product.get().getIsActive())
            throw new NotFoundException("invalid category Id");
        List<String> primaryimageofVariation = new ArrayList<>();
        Set<ProductVariation> productVariations = product.get().getProductVariations();
        for (ProductVariation variation : productVariations)
            primaryimageofVariation.add(variation.getPrimaryImage());

        AdminViewProductDTO viewVariation = new AdminViewProductDTO();
        mm.map(product.get(), viewVariation);
        HashMap<Object, Object> categoryDetails = new HashMap<>();
        categoryDetails.put("id", product.get().getCategory().getId());
        categoryDetails.put("categoryName", product.get().getCategory().getCategoryName());
        viewVariation.setCategoryDetails(categoryDetails);
        viewVariation.setImages(primaryimageofVariation);
        return viewVariation;
    }


    public Page<AdminViewProductDTO> adminViewAllProduct(PagingDefinationDTO pagingDefinationDTO) {
        Pageable pageable = PageRequest.of(pagingDefinationDTO.getOffSet(), pagingDefinationDTO.getPageSize(), (Sort.by(new Sort.Order(null, pagingDefinationDTO.getSortBY()))));
        List<AdminViewProductDTO> adminViewProductList = new ArrayList<>();
        Page<Product> products = productRepository.findAll(pageable);
        for (Product product : products) {
            if (!product.getIsActive() || product.getIsDelete())
                continue;
            List<String> primaryimageofVariation = new ArrayList<>();
            Set<ProductVariation> productVariations = product.getProductVariations();
            for (ProductVariation variation : productVariations)
                primaryimageofVariation.add(variation.getPrimaryImage());

            AdminViewProductDTO viewVariation = new AdminViewProductDTO();
            mm.map(product, viewVariation);
            HashMap<Object, Object> categoryDetails = new HashMap<>();
            categoryDetails.put("id", product.getCategory().getId());
            categoryDetails.put("categoryName", product.getCategory().getCategoryName());
            viewVariation.setCategoryDetails(categoryDetails);
            viewVariation.setImages(primaryimageofVariation);
            adminViewProductList.add(viewVariation);
        }

        return new PageImpl<>(adminViewProductList, pageable, adminViewProductList.size());
    }


    public ResponseEntity<SuccessResponse> deActivateProduct(Long productd) {
        Optional<Product> product = productRepository.findById(productd);
        if (product.isEmpty())
            throw new NotFoundException("Product id is invalid");
        if (product.get().getIsDelete() || !product.get().getIsActive())
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Product is already deactivate or deleted"));

        product.get().setIsActive(false);
        try {
            productRepository.save(product.get());
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
        SimpleMailMessage mailMessage = emailSenderService.CreateBodyForMail(
                product.get().getSeller().getEmailId(),
                "Your Product is DeActivated",
                "Please check details below \n"
                        + product.get().getName() + "\n" + product.get().getDescription());
          emailSenderService.sendEmail(mailMessage);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Product Deactivated Successfully"));

    }

    public ResponseEntity<SuccessResponse> activateProduct(Long productd) {
        Optional<Product> product = productRepository.findById(productd);
        if (product.isEmpty() || product.get().getIsDelete())
            throw new NotFoundException("Product id is invalid");
        if (product.get().getIsActive())
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Product is already Active"));
        product.get().setIsActive(true);
        try {
            productRepository.save(product.get());
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
        SimpleMailMessage mailMessage = emailSenderService.CreateBodyForMail(
                product.get().getSeller().getEmailId(),
                "Your Product is Activated",
                "Please check details below \n"
                        + product.get().getName() + "\n" + product.get().getDescription()
        );
          emailSenderService.sendEmail(mailMessage);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Product Activated Successfully"));

    }
}
