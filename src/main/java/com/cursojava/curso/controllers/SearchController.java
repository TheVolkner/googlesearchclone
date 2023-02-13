package com.cursojava.curso.controllers;

import com.cursojava.curso.entities.WebPage;
import com.cursojava.curso.services.SearchService;
import com.cursojava.curso.services.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class SearchController {


    @Autowired
    private SearchService searchService;

    @Autowired
    private SpiderService spiderService;

    @RequestMapping(value = "api/prueba")
    public List<WebPage> buscar(){

        return searchService.BuscarPaginas();

    }

    @RequestMapping(value = "api/search")
    public List<WebPage> search(@RequestParam Map<String,String> params){
        String query = params.get("query");
        return searchService.search(query);
    }

    @RequestMapping(value = "api/test")
    public void search()  {
        spiderService.startIndexing();
    }
}
