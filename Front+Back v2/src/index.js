var isLoading = false;
var loadingIcon = document.getElementById("loading");
var loadingIconDes = document.getElementById("loading_des");
var inputArtistName = document.getElementById("input_artist_name");
var clearButton = document.getElementById("clear_button");
const scrollBasket = document.getElementById("scroll");
const wrapper = document.getElementById("wrapper");
const descriptionBox = document.getElementById("description");
const Header = document.getElementById("header");
const Nat = document.getElementById("nat");
const Bio = document.getElementById("bio");
const Errr = document.getElementById("error");
const searchForm = document.getElementById("search_form");
var textNode = document.createElement("p");
const cardsArray = [];
var jsonObject;
var resCount = 0;
var errr = false;


function toggleLoadingIcon(){
    if (loadingIcon.style.visibility === "hidden") {
        loadingIcon.style.visibility = "visible";
    } else {
        loadingIcon.style.visibility = "hidden";
    }
}




function startSearching() {
    descriptionBox.style.visibility = "hidden";
    if (!isLoading && !inputArtistName.value==""){
        deleteCouldntFind();
        sendQuery();
        isLoading = true;
        toggleLoadingIcon();
        stopOperations();
        
    }
}

function stopSearching() {
    if (isLoading){
        isLoading = false;
        toggleLoadingIcon();
        startOperations();
    }
}

function startOperations() {
    inputArtistName.readOnly = false;
    clearButton.disabled = false;
}

function stopOperations() {
    inputArtistName.readOnly = true;
    clearButton.disabled = true;
}

function precessData(data) {
    dataObject = JSON.parse(data);
    resType = dataObject.type;
    if (resType=='cleaned_results'){
        const data_count = JSON.stringify(dataObject.content.length);
        var i = 0;
        while (i<data_count){
            var item = dataObject.content[i]
            var image = JSON.stringify(item.thum_link).replace(/['"]+/g, '');
            var name = JSON.stringify(item.name).replace(/['"]+/g, '');
            var id = JSON.stringify(item.id).replace(/['"]+/g, '');
            createCard(image,name,id)
            i++;
        }
    }
}

function showCouldntFind(){
    if (!errr){
        errr = true;
        textNode = document.createElement("p");
        textNode.innerHTML = "No Results Found."
        textNode.style.padding= "15px 100px 15px 100px";
        Errr.appendChild(textNode);
        
    }
}

function deleteCouldntFind(){
    if (errr){
        errr = false;
        textNode.remove();
    }
}


function deleteCards(){
    c = cardsArray.length;
    i = 0;
    while (i<c){
        cardsArray.pop().remove();
        i++;
    }
}

function setDefaultColors(){
    c = cardsArray.length;
    i = 0;
    while (i<c){
        cardsArray[i].name=false;
        cardsArray[i].style.backgroundColor =  ' #205375'
        i++;
    }
}

function newCardStyling(newCard){
    newCard.classList.add('card');
    newCard.style.marginLeft = '4px'
    newCard.style.marginRight = '4px'
    newCard.style.marginTop = '4px'
    newCard.style.marginBottom = '3px'
    newCard.style.backgroundColor = " #205375";
    return newCard
}

function newCardFunctionality(newCard,id){
    newCard.addEventListener("click", function() {getDescriptionQuery(id);});
    newCard.addEventListener("click", function() {
        setDefaultColors();
        newCard.style.backgroundColor =  ' #112B3C'; newCard.name=true});
    newCard.addEventListener("click", function() {
        descriptionBox.style.visibility = "hidden";
        loadingIconDes.style.height = "60px";
        loadingIconDes.style.visibility = "visible";});
    newCard.addEventListener('mouseover', () => {newCard.style.backgroundColor = ' #112B3C';});
    newCard.addEventListener('mouseout', () => {if (!newCard.name){ newCard.style.backgroundColor =  ' #205375';}});
    return newCard
}

function createCard(image, name,id){
    var newCard = document.createElement("div");
    newCard = addImage(image,newCard,name);
    newCard = addName(name,newCard);
    scrollBasket.appendChild(newCard);
    newCard = newCardStyling(newCard);
    newCard = newCardFunctionality(newCard,id);
    newCard.name=false;
    cardsArray.push(newCard);
}

function addImage(image,card,name){
    var newImg = document.createElement("img");
    newImg.src = image;
    newImg.alt=name;
    card.appendChild(newImg);
    return card;
}

function addName(name,card){
    var newName = document.createElement("p");
    newName.classList.add('card-text');
    newName.innerHTML= name;
    card.appendChild(newName);
    return card;
}


function clearInputArtistName() {
    deleteCouldntFind();
    wrapper.style.visibility = "hidden";
    descriptionBox.style.visibility = "hidden";
    deleteCards();
    if (!isLoading) inputArtistName.value="";
}

function sendQuery() {
    $.ajax({
        url: '/api/search/'+inputArtistName.value.replace(/\s/g, '+'),
        type: 'GET',
        success: function(response) {
            if (response=="NF"){
                wrapper.style.visibility = "hidden";
                showCouldntFind();
            }else{
                deleteCards()
                wrapper.style.visibility = "visible";
                precessData(response);
            }
            stopSearching();
        },
        error: function(error) {
            console.log(error);
        }
    });
}

function getDescriptionQuery(id) {

    $.ajax({
        url: '/api/description/'+id,
        type: 'GET',
        tryCount : 0,
        retryLimit : 8,
            success: function(response) {
                processDesc(response);
                loadingIconDes.style.visibility = "hidden"
                loadingIconDes.style.height = "0px";
                descriptionBox.style.visibility = "visible";
            },
            error : function(xhr, textStatus, errorThrown ) {
                if (textStatus == 'timeout') {
                    this.tryCount++;
                    if (this.tryCount <= this.retryLimit) {
                        $.ajax(this);
                        return;
                    }            
                    return;
                }
               console.log(error);
            }
    });
}

function processDesc(data){ 
    Header.innerHTML= JSON.stringify(data.header).replace(/['"]+/g, '');
    Nat.innerHTML= JSON.stringify(data.nationality).replace(/['"]+/g, '');
    Bio.innerHTML= JSON.stringify(data.bio).replace(/['"]+/g, '');
}


inputArtistName.addEventListener('keydown', (event) => {
    if (event.key === 'Enter') {
        searchForm.submit()
        startSearching();
    }
});

scrollBasket.addEventListener('wheel', (event) => {
    event.preventDefault();
    scrollBasket.scrollLeft += event.deltaY;
  });


