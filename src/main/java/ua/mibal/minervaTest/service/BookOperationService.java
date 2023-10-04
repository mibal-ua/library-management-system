package ua.mibal.minervaTest.service;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public interface BookOperationService {

    void takeBook(Long clientId, Long bookId);

    void returnBook(Long clientId, Long bookId);
}
