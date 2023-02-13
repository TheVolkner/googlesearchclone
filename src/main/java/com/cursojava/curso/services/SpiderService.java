package com.cursojava.curso.services;

import com.cursojava.curso.entities.WebPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.hibernate.internal.util.StringHelper.isBlank;

@Service
public class SpiderService {

    @Autowired
    private SearchService searchService;

    public void startIndexing() {

        List<WebPage> linksToIndex = searchService.findWebPagesToIndex();
        linksToIndex.stream().parallel().forEach(webPage -> {
            try {
                indexWebPage(webPage);
            } catch (Exception e) {
                System.out.println("EXCEPTION IN THE PROCESS = ");
                System.out.println(e.getMessage());
                searchService.removeLink(webPage);
                System.out.println("LINK DELETED...");
            }
        });

    }

    private void indexWebPage(WebPage webPage) throws Exception{
        System.out.println("Indexing " + webPage.getUrl());
        String url = webPage.getUrl();
        String content = getWebContent(url);
        if(isBlank(content)){
            return;
        }
        IndexAndSaveWebPage(webPage, content);
        saveLinks(getDomain(url),content);
    }

    private synchronized String getDomain(String url) {

        System.out.println("Obteniendo Dominio...");
        String[] cont1 = url.split("/");
        return cont1[0] + "//" + cont1[2];
    }

    private synchronized void saveLinks(String domain,String content) {

        System.out.println("Saving Links...");
        List<String> links = getLinks(domain,content);

        links.stream().
                filter(link -> !searchService.findByURL(link)).
                forEach(link-> searchService.save(new WebPage(link)));
    }

    private synchronized void IndexAndSaveWebPage(WebPage wp, String content) {
        String title = getTitle(content);
        String description = getDescription(content);

        wp.setTitle(title);
        wp.setDescription(description);
        System.out.println("Saving Web Page...");
        searchService.save(wp);
    }

    public synchronized List<String> getLinks(String domain,String content){

        System.out.println("Getting links from the content...");
        List<String> links = new ArrayList<>();
        String[] cont1 = content.split("href=\"");

        List<String> linksHTML = Arrays.asList(cont1);

        linksHTML.forEach(link -> {

            String[] cont2 = link.split("\"");
            links.add(cont2[0]);
        });
        System.out.println("Returning clean links...");
        return  cleanLinks(domain,links);
    }

    private synchronized List<String> cleanLinks(String domain,List<String> links) {

        System.out.println("Filtering links...");
        String[] excludedExtensions = {"css","js","png","jpg","gif","woff2","json"};

        List<String> cleanList = links.stream().filter(link -> Arrays.stream(excludedExtensions).
                noneMatch(extension -> link.endsWith(extension))).
                map(link -> link.startsWith("/") ? domain + link : link).
                filter(link -> !link.startsWith("#")).
                collect(Collectors.toList());

        List<String> filteredList = new ArrayList<>(new HashSet<String>(cleanList));
        filteredList.forEach(System.out::println);
        System.out.println("Returning filtered links...");
        return filteredList;
    }

    public synchronized String getTitle(String content){

        System.out.println("Getting Title...");
        String[] cont1 = content.split("<title>");
        String[] cont2 = cont1[1].split("</title>");

        System.out.println("Returning Title...");
        return cont2[0];
    }

    public synchronized String getDescription(String content){

        System.out.println("Getting Description...");
        String[] cont1 = content.split(" <meta name=\"description\" content=\"");
        String[] cont2 = cont1[1].split("\" />");

        System.out.println("Returning Description..");
        return cont2[0];
    }

    public synchronized String getWebContent(String link) {

        System.out.println("INIT WEB SCRAPPER...");
        System.out.println(link);

        try {


            //PROCESO DE CONSTRUCCIÓN DEL WEB SCRAPPER, EN ESTE MÉTODO SE CREA UNO PARA CADA LINK.
            URL url = new URL(link);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            String encode = conn.getContentEncoding();

            InputStream inputStream = conn.getInputStream();

            System.out.println("END OF WEB SCRAPPER. RETURNING CONTENT...");
            return new BufferedReader(new InputStreamReader(inputStream)).
                    lines().collect(Collectors.joining());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
}
