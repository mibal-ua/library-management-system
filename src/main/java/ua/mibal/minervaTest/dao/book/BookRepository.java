package ua.mibal.minervaTest.dao.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ua.mibal.minervaTest.model.Book;

import java.util.List;
import java.util.Optional;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public interface BookRepository extends JpaRepository<Book, Long>, CustomBookRepository {

    @Transactional(readOnly = true)
    @Query("select b from Book b left join fetch b.client where b.id = :id")
    Optional<Book> findByIdFetchClient(@Param("id") Long id);

    @Transactional(readOnly = true)
    @Query("select b from Book b left join fetch b.client")
    List<Book> findAllFetchClient();
}
