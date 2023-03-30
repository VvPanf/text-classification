document.addEventListener("DOMContentLoaded", () => {
    const inputFile = document.querySelectorAll(".upload-file__input");

    /////////// Кнопка «Прикрепить файл» ///////////
    inputFile.forEach(function(el) {
        let textSelector = document.querySelector(".upload-file__text");
        let fileList;

        // Событие выбора файла(ов)
        el.addEventListener("change", function (e) {

            // создаём массив файлов
            fileList = [];
            for (let i = 0; i < el.files.length; i++) {
                fileList.push(el.files[i]);
            }

            // вызов функции для каждого файла
            fileList.forEach(file => {
                uploadFile(file);
            });
        });

        // Проверяем размер файлов и выводим название
        const uploadFile = (file) => {

            // файла <5 Мб
            if (file.size > 5 * 1024 * 1024) {
                alert("Файл должен быть не более 5 МБ.");
                return;
            }

            // Показ загружаемых файлов
            if (file && file.length > 1) {
                if ( file.length <= 4 ) {
                    textSelector.textContent = `Выбрано ${file.length} файла`;
                }
                if ( file.length > 4 ) {
                    textSelector.textContent = `Выбрано ${file.length} файлов`;
                }
            } else {
                textSelector.textContent = file.name;
            }
        }

    });

    /////////// Загрузка файлов при помощи «Drag-and-drop» ///////////
    const dropZone = document.querySelector(".upload-file__label");
    const dropZoneText = document.querySelector(".upload-file__text");
    const maxFileSize = 5000000; // максимальный размер файла - 5 мб.
    let uploadDragFile = "";

    // Проверка поддержки «Drag-and-drop»
    if (typeof (window.FileReader) == "undefined") {
        dropZone.textContent = "Drag&Drop Не поддерживается браузером!";
        dropZone.classList.add("error");
    }

    // Событие - перетаскивания файла
    dropZone.ondragover = function () {
        this.classList.add("hover");
        return false;
    };
    dropZone.ondragleave = function () {
        this.classList.remove("hover");
        return false;
    };

    let uploadDragFiles = "";
    dropZone.ondrop = function (e) { // Событие - файл перетащили
        e.preventDefault();
        this.classList.remove("hover");
        this.classList.add("drop");

        uploadDragFiles = e.dataTransfer.files;

        // Проверка размера файла
        if (uploadDragFiles[0].size > maxFileSize) {
            dropZoneText.textContent = "Размер превышает допустимое значение!";
            this.addClass("error");
            return false;
        }

        // Показ загружаемых файлов
        if (uploadDragFiles.length > 1) {
            if (uploadDragFiles.length <= 4) {
                dropZoneText.textContent = `Выбрано ${uploadDragFiles.length} файла`;
            } else {
                dropZoneText.textContent = `Выбрано ${uploadDragFiles.length} файлов`;
            }
        } else {
            dropZoneText.textContent = e.dataTransfer.files[0].name;
        }
    }

//    // Отправка формы на сервер
//    const postData = async (url, fData) => { // имеет асинхронные операции
//
//        // начало отправки
//        // здесь можно оповестить пользователя о начале отправки
//
//        // ждём ответ, только тогда наш код пойдёт дальше
//        let fetchResponse = await fetch(url, {
//            method: "POST",
//            body: fData
//        });
//
//        // ждём окончания операции
//        return await fetchResponse.text();
//    };
//
//    if (forms) {
//        forms.forEach(el => {
//            el.addEventListener("submit", function (e) {
//                e.preventDefault();
//
//                // создание объекта FormData
//                let fData = new FormData(this);
//
//                // Добавление файлов input type file
//                let file = el.querySelector(".upload-file__input");
//                for (let i = 0; i < (file.files.length); i++) {
//                    fData.append("files[]", file.files[i]); // добавляем файлы в объект FormData()
//                }
//
//                // Отправка на сервер
//                postData("./mail.php", fData)
//                    .then(fetchResponse => {
//                        console.log("Данные успешно отправлены!");
//                        console.log(fetchResponse);
//                    })
//                    .catch(function (error) {
//                        console.log("Ошибка!");
//                        console.log(error);
//                    });
//            });
//        });
//    };

});