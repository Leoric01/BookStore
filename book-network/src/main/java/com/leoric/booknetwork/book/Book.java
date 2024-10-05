package com.leoric.booknetwork.book;

import com.leoric.booknetwork.common.BaseEntity;
import com.leoric.booknetwork.feedback.Feedback;
import com.leoric.booknetwork.history.BookTransactionHistory;
import com.leoric.booknetwork.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book extends BaseEntity {

    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private boolean shareable;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    // NOT IDEAL TO USE EAGER IN CASE THERE'S A LOT OF FEEDBACKS, IF SHORT LIST - THEN OK
    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    private List<BookTransactionHistory> histories;

    @Transient
    public double getRate() {
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }
        double rate = this.feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);
        //round to whole number
        return Math.round(rate * 100.0) / 100.0;
    }
}
