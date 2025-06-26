package com.book.jpa.controllers;

import com.book.jpa.dtos.BookRecordDto;
import com.book.jpa.models.BookModel;
import com.book.jpa.repositorys.BookRepository;
import com.book.jpa.services.BookService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/bookstore/books")
public class BookController {

    @Autowired // utilizando para não usar o new e ter acesso aos médotos Jpa.
    BookRepository bookRepository;

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    //Para acessar o médoto de lista dos book no bookservice.
    @GetMapping
    public ResponseEntity<List<BookModel>> getAllBooks(){
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneBook(@PathVariable(value = "id") UUID id){
        Optional<BookModel> book = bookRepository.findById(id);
        if (book.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");//corresponde ao código 404, o body a resposta.
        }
        return ResponseEntity.status(HttpStatus.OK).body(book.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateBook(@PathVariable(value = "id") UUID id, @RequestBody @Validated BookRecordDto bookRecordDto){
        Optional<BookModel> bookO = bookRepository.findById(id);
        if (bookO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        var bookModel = bookO.get();
        BeanUtils.copyProperties(bookRecordDto, bookModel);
        return ResponseEntity.status(HttpStatus.OK).body(bookRepository.save(bookModel));//convenção do dto para model.
    }

    @PostMapping
    public ResponseEntity<BookModel> seveBook(@RequestBody BookRecordDto bookRecordDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.seveBook(bookRecordDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable UUID id){
        Optional<BookModel> bookO = bookRepository.findById(id);
        if (bookO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found.");
        }
        bookRepository.delete(bookO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Book deleted sucessfully.");
    }
}
