package com.example.catalogservice;

import com.example.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookJsonTests {

    @Autowired
    private JacksonTester<Book> json;

    @Test
    void testSerialize() throws Exception {

        var book = new Book(
                null,
                "12345677890",
                "Title",
                "Author",
                9.90,
                null,
                Instant.now().minusSeconds(15 * 60),
                Instant.now(),
                0
        );
        var jsonContent = json.write(book);

        assertThat(jsonContent)
                .extractingJsonPathStringValue("@.isbn")
                .isEqualTo(book.isbn());

        assertThat(jsonContent)
                .extractingJsonPathStringValue("@.title")
                .isEqualTo(book.title());

        assertThat(jsonContent)
                .extractingJsonPathStringValue("@.author")
                .isEqualTo(book.author());

        assertThat(jsonContent)
                .extractingJsonPathNumberValue("@.price")
                .isEqualTo(book.price());

        assertThat(jsonContent)
                .extractingJsonPathStringValue("@.createdDate")
                .isEqualTo(book.createdDate().toString());
    }

    @Test
    void testDeserialize() throws Exception {

        var content = """
                {
                  "isbn": "1234567890",
                  "title": "Title",
                  "author": "Author",
                  "price": 9.90
                }
                """;

        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(Book.of("1234567890", "Title", "Author", 9.90, null));
    }
}
