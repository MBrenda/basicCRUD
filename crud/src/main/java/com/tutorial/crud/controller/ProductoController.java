package com.tutorial.crud.controller;

import com.tutorial.crud.dto.Mensaje;
import com.tutorial.crud.dto.ProductoDto;
import com.tutorial.crud.entity.Producto;
import com.tutorial.crud.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/producto")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoController {

    @Autowired
    ProductoService productoService;

    @GetMapping("/lista")
    public ResponseEntity<List<Producto>> list(){
        List<Producto> list = productoService.list();
        return new ResponseEntity<List<Producto>>(list, HttpStatusCode.valueOf(200));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity getById(@PathVariable("id") int id){
        if(!productoService.existsById(id)){
            return new ResponseEntity(new Mensaje("no existe"), HttpStatusCode.valueOf(404));
        }
        Producto producto = productoService.getOne(id).get();
        return new ResponseEntity<Producto>(producto, HttpStatusCode.valueOf(200));
    }

    @GetMapping("/detailname/{nombre}")
    public ResponseEntity getByNombre(@PathVariable("nombre") String nombre){
        if(!productoService.existsByName(nombre)){
            return new ResponseEntity(new Mensaje("no existe"), HttpStatusCode.valueOf(404));
        }
        Producto producto = productoService.getByNombre(nombre).get();
        return new ResponseEntity<Producto>(producto, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProductoDto productoDto){
        if(StringUtils.isBlank(productoDto.getNombre())){
            return new ResponseEntity<>(new Mensaje("El nombre es obligatorio"), HttpStatusCode.valueOf(400));
        }
        if(productoDto.getPrecio()==null || productoDto.getPrecio()<0){
            return new ResponseEntity<>(new Mensaje("El precio es obligatorio y debe ser mayor que 0"), HttpStatusCode.valueOf(400));
        }
        if(productoService.existsByName(productoDto.getNombre())){
            return new ResponseEntity<>(new Mensaje("Ese nombre ya existe"), HttpStatusCode.valueOf(400));
        }

        Producto producto = new Producto(productoDto.getNombre(), productoDto.getPrecio());
        productoService.save(producto);
        return new ResponseEntity<>(new Mensaje("Producto creado con exito"), HttpStatusCode.valueOf(200));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody ProductoDto productoDto){
        if(!productoService.existsById(id)){
            return new ResponseEntity(new Mensaje("no existe"), HttpStatusCode.valueOf(404));
        }
        if(productoService.existsByName(productoDto.getNombre()) && productoService.getByNombre(productoDto.getNombre()).get().getId() != id){
            return new ResponseEntity<>(new Mensaje("Ese nombre ya existe"), HttpStatusCode.valueOf(400));
        }

        if(StringUtils.isBlank(productoDto.getNombre())){
            return new ResponseEntity<>(new Mensaje("El nombre es obligatorio"), HttpStatusCode.valueOf(400));
        }
        if(productoDto.getPrecio()<0){
            return new ResponseEntity<>(new Mensaje("El precio es obligatorio y debe ser mayor que 0"), HttpStatusCode.valueOf(400));
        }

        Producto producto = productoService.getOne(id).get();
        producto.setNombre(productoDto.getNombre());
        producto.setPrecio(productoDto.getPrecio());
        productoService.save(producto);
        return new ResponseEntity<>(new Mensaje("Producto actualizado!!"), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id){
        if(!productoService.existsById(id)){
            return new ResponseEntity(new Mensaje("no existe tal id"), HttpStatusCode.valueOf(404));
        }
        productoService.delete(id);
        return new ResponseEntity<>(new Mensaje("Producto eliminado!!"), HttpStatusCode.valueOf(200));
    }

}
