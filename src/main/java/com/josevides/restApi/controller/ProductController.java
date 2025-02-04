package com.josevides.restApi.controller;

import com.josevides.restApi.dto.product.ProductRequest;
import com.josevides.restApi.dto.product.ProductResponse;
import com.josevides.restApi.model.Product;
import com.josevides.restApi.services.ProductService;
import com.josevides.restApi.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    private final JWTUtil jwtUtil = new JWTUtil();

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody ProductRequest productRequest, @RequestHeader("Authorization") String jwt){
        try {

            if (!jwtUtil.isTokenValid(jwt)){
                return ResponseEntity.badRequest().body("JWT no valido");
            }

            if (!jwtUtil.verifyRole(jwt, "SUPER")){
                return ResponseEntity.badRequest().body("Rol no valido");
            }

            ProductResponse productResponse = productService.create(productRequest);

            return ResponseEntity.ok(productResponse);

        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error al crear: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity listProducts(@RequestHeader("Authorization") String jwt){

        try{
            if (!jwtUtil.isTokenValid(jwt)){
                return ResponseEntity.badRequest().body("JWT no valido");
            }

            if (!jwtUtil.verifyRole(jwt, "TEC")){
                return ResponseEntity.badRequest().body("Rol no valido");
            }

            List<Product> products = this.productService.listProducts();

            return ResponseEntity.ok(products);

        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error al listar: " + e.getMessage());
        }

    }

    @GetMapping("/by-price/{price}")
    public ResponseEntity getproductsByPrice(@RequestHeader("Authorization") String jwt, @PathVariable Float price){
        try{

            if (!jwtUtil.isTokenValid(jwt)){
                return ResponseEntity.badRequest().body("JWT no valido");
            }

            if (!jwtUtil.verifyRole(jwt, "SUPER")){
                return ResponseEntity.badRequest().body("Rol no valido");
            }

            List<Product> products = this.productService.getProductsByPrice(price);

            return ResponseEntity.ok(products);

        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error al listar por precio: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestHeader("Authorization") String jwt, @RequestBody ProductRequest productRequest){
        try{
            if (!jwtUtil.isTokenValid(jwt)){
                return ResponseEntity.badRequest().body("JWT no valido");
            }

            if (!jwtUtil.verifyRole(jwt, "VIS")){
                return ResponseEntity.badRequest().body("Rol no valido");
            }

            ProductResponse productResponse = this.productService.update(id, productRequest);

            return ResponseEntity.ok(productResponse);

        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error al actualizar: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @RequestHeader("Authorization") String jwt){
        try{

            if (!jwtUtil.isTokenValid(jwt)){
                return ResponseEntity.badRequest().body("JWT no valido");
            }

            if (!jwtUtil.verifyRole(jwt, "ADMIN")){
                return ResponseEntity.badRequest().body("Rol no valido");
            }

            String respuesta = this.productService.delete(id);

            return ResponseEntity.ok(respuesta);

        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error al eliminar: " + e.getMessage());
        }
    }

}
