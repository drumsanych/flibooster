package org.flibooster.services;

import lombok.AllArgsConstructor;
import org.flibooster.model.Book;
import org.flibooster.services.interfaces.FlibustaService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class FlibustaServiceImpl implements FlibustaService {
    private FlibustaRepository htmlParser;
    @Override
    public File getFileByUrl(String url) throws FileNotFoundException {
        File file = new File("/sd");
        if (!file.exists()) {
            throw new FileNotFoundException("Файл не найден");
        }
        return null;
    }

    @Override
    public List<Book> getBooksList(String request) throws IOException {
        return htmlParser.searchBooks(request);
    }
}
