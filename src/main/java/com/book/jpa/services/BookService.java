package com.book.jpa.services;

import com.book.jpa.dtos.BookRecordDto;
import com.book.jpa.models.BookModel;
import com.book.jpa.models.ReviewModel;
import com.book.jpa.repositorys.AuthorRepository;
import com.book.jpa.repositorys.BookRepository;
import com.book.jpa.repositorys.PublisherRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;


    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
    }
    //metodo pra lista os livros
    public List<BookModel> getAllBooks(){
        return bookRepository.findAll();
    }

    //médoto deletar o book
    @Transactional
    public void deleteBook(UUID id){
        bookRepository.deleteById(id);
    }

    @Transactional
    public BookModel seveBook(BookRecordDto bookRecordDto){
        BookModel book = new BookModel();
        book.setTitle(bookRecordDto.title());
        book.setPublisher(publisherRepository.findById(bookRecordDto.publisherId()).get());
        book.setAuthors(authorRepository.findAllById(bookRecordDto.authorIds()).stream().collect(Collectors.toSet()));

        ReviewModel reviewModel = new ReviewModel();
        reviewModel.setComment(bookRecordDto.reviewComment());
        reviewModel.setBook(book);
        book.setReview(reviewModel);

        return bookRepository.save(book);
    }
}
