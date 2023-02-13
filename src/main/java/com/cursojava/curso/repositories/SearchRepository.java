package com.cursojava.curso.repositories;

import com.cursojava.curso.entities.WebPage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SearchRepository extends CrudRepository<WebPage,Integer> {

   List<WebPage> findByDescriptionContaining(String textSearch);

   WebPage findByUrl(String url);

   @Query(value="select * FROM webpage WHERE description IS null AND title IS null",nativeQuery = true)
   List<WebPage> findByDescriptionAndTitleIsNull();
}
