package org.flibooster.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book {
    private String title;
    private String author;
    private String pdf;
    private String fb2;
    private String epub;
    private String mobi;

    public boolean hasAnyLink() {
        return pdf != null || fb2 != null || epub != null || mobi != null;
    }
}
