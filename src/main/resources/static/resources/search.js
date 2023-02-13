
function search(){


let textSearch = document.getElementById("campo").value;

document.location.href="results.html?query=" + textSearch;
}



let url = document.location.href.split("?query=");

let query = url[1];
document.getElementById("campoBusqueda").value = query;

fetch("http://localhost:8080/api/search?query=" + query)
  .then((response) => response.json())
  .then((json) => {
    console.log(json);

    let htmlCampos = '';
    for (searchElement of json) {
        
        htmlCampos += getHTMLResultSearch(searchElement);
    }
    document.getElementById("camposBusqueda").innerHTML = htmlCampos;
  });

function getHTMLResultSearch(searchElement) {

 let title = searchElement.title;

 let description = searchElement.description;

 let url = searchElement.url;
 
  return  `<div class="resultado" style="margin-bottom: 1.5em">
           <span class="URL"><a href="${url}" target="_blank">${url}</a></span>
           <h3 class="titulo"><a href="${url}" target="_blank">${title}</a></h3>
           <span class="texto">${description}</span>
           </div>`;

}
