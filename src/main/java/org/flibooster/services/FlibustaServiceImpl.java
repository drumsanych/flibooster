package org.flibooster.services;

import lombok.AllArgsConstructor;
import org.flibooster.model.Book;
import org.flibooster.repository.FlibustaRepository;
import org.flibooster.services.interfaces.FlibustaService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class FlibustaServiceImpl implements FlibustaService {
    private FlibustaRepository flibustaRepository;
    @Override
    public File getFileByUrl(String url) throws IOException {
        File file = flibustaRepository.getFileByUrl(url);
        return file;
    }

    @Override
    public List<Book> getBooksList(String request) throws IOException {
        return flibustaRepository.searchBooks(request);
    }
}
