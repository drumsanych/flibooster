package org.flibooster.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Book {
    private String title;
    private String author;
    private String pdf;
    private String fb2;
    private String epub;
    private String mobi;
}
