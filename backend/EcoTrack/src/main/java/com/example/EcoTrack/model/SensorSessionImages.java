package com.example.dreamshops.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
@Getter
@Setter
public class Images {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String fileType; // bu filename
    private String filePath; //bu filetype normalde

    @Lob
    private Blob image;
    //burda ilk kez karşılaştığımız mevzu ise @lob ve blob mevzuları.
    private  String downloadUrl;
    @ManyToOne
    @JoinColumn(name = "product_ıd")
    private Product product;



}
