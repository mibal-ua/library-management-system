package ua.mibal.minervaTest.utils;

import ua.mibal.minervaTest.model.Book;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class Books {

    public static Book ofMapping(List<String> answers) {
        Iterator<String> answersIterator = answers.iterator();
        return new Book(
                answersIterator.next(),
                answersIterator.next(),
                answersIterator.next(),
                LocalDateTime.of(LocalDate.parse(answersIterator.next()), LocalTime.MIN),
                answersIterator.next(),
                true
        );
    }

    public static Book copyOf(Book originalBook) {
        return new Book(
                originalBook.getId(),
                originalBook.getTitle(),
                originalBook.getSubtitle(),
                originalBook.getAuthor(),
                originalBook.getPublishedDate(),
                originalBook.getPublisher(),
                originalBook.isFree()
        );
    }

    public static Book changeByMapping(Book editedBook, List<String> answers) {
        List<Consumer<String>> consumers = List.of(
                editedBook::setTitle,
                editedBook::setSubtitle,
                editedBook::setAuthor,
                str -> editedBook.setPublishedDate(LocalDateTime.of(LocalDate.parse(str), LocalTime.MIN)),
                editedBook::setPublisher

        );
        for (int i = 0; i < consumers.size(); i++) {
            String answer = answers.get(i);
            if (!answer.isEmpty()) consumers.get(i).accept(answer);
        }
        return editedBook;
    }
}
