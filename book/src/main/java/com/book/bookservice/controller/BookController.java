package com.book.bookservice.controller;

import com.book.bookservice.model.Book;
import com.book.bookservice.proxy.CambioProxy;
import com.book.bookservice.repository.BookRepository;
import com.book.bookservice.response.Cambio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("book-service")
public class BookController {

    @Autowired
    private Environment environment;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CambioProxy cambioProxy;

    @GetMapping(value = "/{id}/{currency}")
    public Optional<Book> findBook(@PathVariable("id") Long id,
                                   @PathVariable("currency") String currency) {

        var book = bookRepository.findById(id);
        if (book == null) throw new RuntimeException("Book not Found");


        var cambio = cambioProxy.getCambio(book.get().getPrice(), "USD", currency);

        var port = environment.getProperty("local.server.port");


        book.get().setEnvironment(port);
        book.get().setEnvironment("Book port:" + port + "Cambio port:" + cambio.getEnvironment());
        book.get().setPrice(cambio.getConvertedValue());
        return book;
    }


}

