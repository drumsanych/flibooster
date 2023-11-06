package org.flibooster.controller;

import org.flibooster.model.Book;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/")
public class RestController {

    @GetMapping("/search")
    public ResponseEntity<List<Book>> getBook(@RequestParam("search") String search) {
        System.out.println("Контроллер сработал: " + search);

        Book book1 = new Book("Горе от ума", "Грибоедов", "/b/1234/pdf", "/b/1234/fb2", "/b/1234/fb2", null);
        Book book2 = new Book("Тарас Бульба", "Гоголь", "/b/1234/pdf", null, null, null);
        Book book3 = new Book("Анна Каренина", "Толстой", "/b/1234/pdf", null, "/b/1234/fb2", "/b/1234/mobi");
        Book book4 = new Book("TITLE1", "AUTHOR1", "/b/1234/pdf", "/b/1234/fb2", "/b/1234/fb2", null);
        Book book5 = new Book("TITLE2", "AUTHOR2", "/b/1234/pdf", null, null, null);
        Book book6 = new Book("TITLE3", "AUTHOR3", "/b/1234/pdf", null, "/b/1234/fb2", "/b/1234/mobi");
        Book book7 = new Book("TITLE4", "AUTHOR4", "/b/1234/pdf", "/b/1234/fb2", "/b/1234/fb2", null);
        List<Book> books = new ArrayList<>();
        if (search.equals("Привет")) {
            books.add(book1);
            books.add(book4);
        }
//        books.add(book1);
//        books.add(book2);
//        books.add(book3);
//        books.add(book4);
//        books.add(book5);
//        books.add(book6);
//        books.add(book7);
//        books.add(book1);
//        books.add(book2);
//        books.add(book3);
//        books.add(book4);
//        books.add(book5);
//        books.add(book6);
        ResponseEntity<List<Book>> responseEntity = new ResponseEntity<>(books, HttpStatus.OK);
        return responseEntity;
    }


    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("href") String href) throws FileNotFoundException {
        // Путь к файлу, который нужно скачать
        File file = new File("C:\\Users\\amoraltest\\Desktop\\поделки\\test.pdf");

        // Проверяем, что файл существует
        if (!file.exists()) {
            throw new FileNotFoundException("Файл не найден");
        }

        // Создаем ресурс из файла
        Path filePath = Paths.get(file.getAbsolutePath());
        Resource resource = new InputStreamResource(new FileInputStream(file));

        // Возвращаем файл для загрузки в HTTP-ответе с указанием имени файла
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName() + "")
                .body(resource);
    }
}
