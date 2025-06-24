package com.book.jpa.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "TB_BOOK")
public class BookModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)//O ID vai se gerado automático.
    private UUID id;

    @Column(nullable = false, unique = true)//o título não pode se nulo e tem que se único.
    private String title;

    //relação entre as  tabela tb_book e publisher
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)//pra não dar erro no carregamento LYZE
    @ManyToOne//(fetch = FetchType.LAZY)//garregamento lento
    @JoinColumn(name = "publisher_id")//chave estrangeira do tb_book
    private PublisherModel publisher;

    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany//(fetch = FetchType.LAZY)
    @JoinTable(name = "tb_book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<AuthorModel> authors = new HashSet<>();

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL) // muda para todos o cascade não é muito legal mas pra esse exemplo de boa
    private ReviewModel review;

    public UUID getId(){
        return id;
    }

    public void setId(UUID id){
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public PublisherModel getPublisher() {
        return publisher;
    }

    public void setPublisher(PublisherModel publisher) {
        this.publisher = publisher;
    }

    public Set<AuthorModel> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorModel> authors) {
        this.authors = authors;
    }

    public ReviewModel getReview() {
        return review;
    }

    public void setReview(ReviewModel review) {
        this.review = review;
    }
}
