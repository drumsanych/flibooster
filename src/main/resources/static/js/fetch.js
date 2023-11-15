let index = 0;
let cardsRendered = 0;
let responseJson;
function sendGetRequest() {
    const searchQuery = document.getElementById("searchInput").value;
    hideStuff('booksTab');
    index = 0;
    cardsRendered = 0;

    const xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.status === 200) {



            responseJson = JSON.parse(xhr.responseText);
            console.log(responseJson)
            showStuff('booksTab');

            drawCard(document.getElementById("container1"), responseJson[index].author, responseJson[index].title, responseJson[index].pdf, responseJson[index].fb2, responseJson[index].epub, responseJson[index].mobi);
            if (responseJson.length > index) {
                drawCard(document.getElementById("container2"), responseJson[index].author, responseJson[index].title, responseJson[index].pdf, responseJson[index].fb2, responseJson[index].epub, responseJson[index].mobi);
            } else {
                document.getElementById("container2").innerHTML = '';
            }
            if (responseJson.length > index) {
                drawCard(document.getElementById("container3"), responseJson[index].author, responseJson[index].title, responseJson[index].pdf, responseJson[index].fb2, responseJson[index].epub, responseJson[index].mobi);
            } else {
                document.getElementById("container3").innerHTML = '';
            }
            console.log('rendered: ' + cardsRendered);
            console.log('current index: ' + index);

        } else {
            alert("Ничего не найдено");
        }

        console.log('hide spinner')
        hideSpinner();
    };

    xhr.open("GET", "/api/search?search=" + searchQuery, true);
    showSpinner();
    xhr.send();
}

document.querySelector('form').addEventListener('submit', function (event) {
    event.preventDefault(); // Отменить базовое поведение формы (отправку)
    // Добавьте свою логику обработки данных формы
});

document.getElementById('searchInput').addEventListener('keydown', function (event) {
    if (event.keyCode === 13) {
        event.preventDefault(); // Отменить базовое поведение нажатия Enter (отправку формы)
        sendGetRequest();
    }
});

function showStuff(id) {
    document.getElementById(id).style.visibility = 'visible';
}
function hideStuff(id) {
    // document.getElementById(id).innerHTML = '';
}

function forward(event) {
    event.preventDefault();
    if (cardsRendered == 3) {
        cardsRendered = 0;
        if (index <= responseJson.length) {
            drawCard(document.getElementById("container1"), responseJson[index].author, responseJson[index].title, responseJson[index].pdf, responseJson[index].fb2, responseJson[index].epub, responseJson[index].mobi);

        }
        if (index < responseJson.length) {
            drawCard(document.getElementById("container2"), responseJson[index].author, responseJson[index].title, responseJson[index].pdf, responseJson[index].fb2, responseJson[index].epub, responseJson[index].mobi);

        } else {
            document.getElementById("container2").innerHTML = '';
        }
        if (index < responseJson.length) {
            drawCard(document.getElementById("container3"), responseJson[index].author, responseJson[index].title, responseJson[index].pdf, responseJson[index].fb2, responseJson[index].epub, responseJson[index].mobi);

        } else {
            document.getElementById("container3").innerHTML = '';
        }
        console.log('rendered: ' + cardsRendered);
        console.log('current index: ' + index);
    }
}

function back(event) {
    event.preventDefault();
    if (index > 3) {
        index = index - 3 - cardsRendered;
        console.log('index before rendering: ' + index);

        cardsRendered = 0;
        drawCard(document.getElementById("container1"), responseJson[index].author, responseJson[index].title, responseJson[index].pdf, responseJson[index].fb2, responseJson[index].epub, responseJson[index].mobi);

        drawCard(document.getElementById("container2"), responseJson[index].author, responseJson[index].title, responseJson[index].pdf, responseJson[index].fb2, responseJson[index].epub, responseJson[index].mobi);

        drawCard(document.getElementById("container3"), responseJson[index].author, responseJson[index].title, responseJson[index].pdf, responseJson[index].fb2, responseJson[index].epub, responseJson[index].mobi);

        console.log('rendered: ' + cardsRendered);
        console.log('current index: ' + index);

    }
}


function drawCard(element, author, title, pdf, fb2, epub, mobi) {
    element.innerHTML = '';

    element.innerHTML +=
        '<div class="u-container-layout u-similar-container u-container-layout-1">\n' +
        '<img alt="" class="custom-expanded u-image u-image-contain u-image-default u-image-1" data-image-width="512" data-image-height="512" src="/images/free-icon-spellbook-7778743.png">\n' +
        '<img id="pdfImg' + index + '" style="visibility: hidden" class="u-hover-feature u-image u-image-contain u-image-default u-preserve-proportions u-image-7" src="/images/icons8-pdf-2-50-2.png" alt="" data-image-width="50" data-image-height="50" onclick="openLink(\'' + pdf + '\')">\n' +
        '<img id="epubImg' + index + '" style="visibility: hidden" class="u-hover-feature u-image u-image-contain u-image-default u-preserve-proportions u-image-3" src="/images/icons8-epub-50-2.png" alt="" data-image-width="50" data-image-height="50" onclick="openLink(\'' + epub + '\')">\n' +
        '<img id="fb2Img' + index + '" style="visibility: hidden" class="u-hover-feature u-image u-image-contain u-image-default u-preserve-proportions u-image-4" src="/images/icons8-fb-2-50-2.png" alt="" data-image-width="50" data-image-height="50" onclick="openLink(\'' + fb2 + '\')">\n' +
        '<img id="mobiImg' + index + '" style="visibility: hidden" class="u-hover-feature u-image u-image-contain u-image-default u-preserve-proportions u-image-5" src="/images/icons8-mobi-50-2.png" alt="" data-image-width="50" data-image-height="50" onclick="openLink(\'' + mobi + '\')">\n' +
        '<h4 class="u-custom-font u-text u-text-default u-text-1">' + author + '</h4>\n' +
        '  <p class="u-custom-font u-text u-text-default u-text-2">' + title + '</p>\n' +
        '</div>'

    if (pdf != null) {
        document.getElementById("pdfImg" + index).style.visibility = 'visible';
        document.getElementById("pdfImg" + index).setAttribute('href', pdf);
    }
    if (epub != null) {
        document.getElementById("epubImg" + index).style.visibility = 'visible';
        document.getElementById("epubImg" + index).setAttribute('href', epub);
    }
    if (fb2 != null) {
        document.getElementById("fb2Img" + index).style.visibility = 'visible';
        document.getElementById("fb2Img" + index).setAttribute('href', fb2);
    }
    if (mobi != null) {
        document.getElementById("mobiImg" + index).style.visibility = 'visible';
        document.getElementById("mobiImg" + index).setAttribute('href', mobi);
    }
    index++;
    cardsRendered++;
}

function openLink(link) {
    showSpinner()
    fetch("/api/download?href=" + link, {
        method: "GET",
        headers: {
            "Content-Type": "application/octet-stream"
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Ошибка при скачивании файла");
            }
            hideSpinner();
            const contentDisposition = response.headers.get("content-disposition");
            const fileName = contentDisposition.split("filename=")[1];

            return response.blob().then(blob => {
                const downloadLink = document.createElement("a");
                downloadLink.href = URL.createObjectURL(blob);
                downloadLink.download = fileName;
                downloadLink.click();

                URL.revokeObjectURL(downloadLink.href);
            });
        })
        .catch(error => {
            console.error("Произошла ошибка:", error);
        });
}

function showSpinner() {
    document.getElementById('spinner').style.visibility = 'visible';
}
function hideSpinner() {
    document.getElementById('spinner').style.visibility = 'hidden';
}