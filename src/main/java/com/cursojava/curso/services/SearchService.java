package com.cursojava.curso.services;

import com.cursojava.curso.entities.WebPage;
import com.cursojava.curso.repositories.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    private SearchRepository searchRepository;

    public List<WebPage> BuscarPaginas(){

        return (List<WebPage>) searchRepository.findAll();
    }

    public List<WebPage> search(String textSearch){

        return searchRepository.findByDescriptionContaining(textSearch);
    }

    public void save(WebPage wp){

        searchRepository.save(wp);
    }

    public boolean findByURL(String url) {
        WebPage wpSearched = searchRepository.findByUrl(url);

        return wpSearched != null;
    }

    public List<WebPage> findWebPagesToIndex(){

        return searchRepository.findByDescriptionAndTitleIsNull();
    }

    public void removeLink(WebPage webPage) {

        searchRepository.delete(webPage);
    }
}
