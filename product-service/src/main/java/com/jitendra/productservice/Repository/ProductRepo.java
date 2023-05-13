package com.jitendra.productservice.Repository;

import com.jitendra.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepo extends MongoRepository<Product,String> {
}
