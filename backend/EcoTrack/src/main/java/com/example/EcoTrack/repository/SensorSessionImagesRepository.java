package com.example.dreamshops.repo;

import com.example.dreamshops.model.Images;
import com.example.dreamshops.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;
import java.util.List;

public interface ImagesRepository extends JpaRepository<Images,Long> {
    @Override
    List<Images> findAll();
    List<Images> findByProductId(Long id);
    //hiçbişe yok bunlarda normalde db ile ilgili fonksiyonlar olması lazımdı db ile ilgili fonksiyonların hepsi service tarafındaki
    //interfacelerde.



}
