package mx.tecnm.backend.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.tecnm.backend.api.models.Producto;

@RequestMapping("/test")
@RestController
public class Test {

    @GetMapping("/hello")
    public String helloworld(){
        return "Hello API Rest";
    }

    @GetMapping("/producto")
    public Producto getProducto(){
        Producto p = new Producto();
        p.nombre = "coca cola";
        p.precio = 18.5;
        p.codigoBarras = "1234567890987";
        return p;
    }

    @GetMapping("/productos")
    public Producto [] getProductos(){
        Producto p1 = new Producto();
        p1.nombre = "coca cola";
        p1.precio = 18.5;
        p1.codigoBarras = "1234567890987";

        Producto p2 = new Producto();
        p2.nombre = "Pepsi";
        p2.precio = 17.5;
        p2.codigoBarras = "0973567890987";
        
        Producto p3 = new Producto();
        p3.nombre = "Fanta";
        p3.precio = 16.5;
        p3.codigoBarras = "0283764246197";
        
        return new Producto[]{p1, p2, p3};
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable int id) {

        if (id<0||id >2) {
            return ResponseEntity.notFound().build();
        }

        Producto[] productos = new Producto[3];
        Producto p1 = new Producto();
        p1.nombre = "coca cola";
        p1.precio = 18.5;
        p1.codigoBarras = "1234567890987";
        productos[0] = p1;


        Producto p2 = new Producto();
        p2.nombre = "pepsi";
        p2.precio = 17.5;
        p2.codigoBarras = "0234567890987";
        productos[1] = p2;
        
        Producto p3 = new Producto();
        p3.nombre = "Fanta";
        p3.precio = 16.5;
        p3.codigoBarras = "47208371271237";
        productos[2] = p3;
        
        Producto resultado = productos[id];
        return ResponseEntity.ok(resultado);
    }

    

}


