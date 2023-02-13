package com.cursojava.curso.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name="webpage")
public class WebPage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idwebpage")
    private Integer idWebPage;
    private String url;
    private String title;
    private String description;

    public WebPage(){


    }

    public WebPage(String url){

        this.url = url;
    }
}
