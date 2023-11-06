package org.flibooster.services.interfaces;

import org.flibooster.model.Book;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface FlibustaService {
    File getFileByUrl(String url) throws FileNotFoundException;
    List<Book> getBooksList(String request) throws IOException;

}
