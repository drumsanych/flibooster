package org.flibooster.controller;

import lombok.AllArgsConstructor;
import org.flibooster.exceptions.EmptyBooksListException;
import org.flibooster.model.Book;
import org.flibooster.services.interfaces.FlibustaService;
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
import java.io.IOException;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@AllArgsConstructor
@RequestMapping("/api/")
public class RestController {

    private FlibustaService flibustaService;

    @GetMapping("/search")
    public ResponseEntity<List<Book>> getBook(@RequestParam("search") String search) throws EmptyBooksListException, IOException {
        List<Book> booksList = flibustaService.getBooksList(search);
        if (booksList.isEmpty()) {
            throw new EmptyBooksListException();
        }
        return new ResponseEntity<>(booksList, HttpStatus.OK);
    }


    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("href") String href) throws FileNotFoundException {
        File file = flibustaService.getFileByUrl(href);
        Resource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName() + "")
                .body(resource);
    }
}
