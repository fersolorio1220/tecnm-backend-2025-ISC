package mx.tecnm.backend.api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
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
        p2.nombre = "coca cola";
        p2.precio = 18.5;
        p2.codigoBarras = "1234567890987";
        
        Producto p3 = new Producto();
        p3.nombre = "coca cola";
        p3.precio = 18.5;
        p3.codigoBarras = "1234567890987";
        
        return new Producto[]{p1, p2, p3};
    }

    

}


