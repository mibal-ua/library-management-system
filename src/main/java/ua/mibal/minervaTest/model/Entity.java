package ua.mibal.minervaTest.model;

import ua.mibal.minervaTest.gui.console.DataBundle;
import ua.mibal.minervaTest.model.window.DataType;

import java.util.List;
import java.util.Map;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public abstract class Entity {

    private final static Map<DataType, DataBundle<? extends Entity>> listMap = Map.of(
            DataType.BOOK, new DataBundle<Book>(
                    List.of(" ID ", "Title", "Author", "Free"),
                    List.of(4, 36, 23, 4),
                    List.of(
                            Book::getId,
                            Book::getTitle,
                            Book::getAuthor,
                            book -> book.isFree() ? "Yes" : "No")),
            DataType.CLIENT, new DataBundle<Client>(
                    List.of(" ID ", "Name", "Books"),
                    List.of(4, 35, 31),
                    List.of(
                            Client::getId,
                            Client::getName,
                            client -> client.getBooks().stream()
                                    .map(book -> book.getId().toString())
                                    .reduce("", (str1, str2) -> str1 + str2 + " "))),
            DataType.HISTORY, new DataBundle<Operation>(
                    List.of(" ID ", "Date", "Client name", "Operation", "Books"),
                    List.of(4, 10, 26, 9, 15),
                    List.of(
                            Operation::getId,
                            operation -> operation.getDate().toLocalDate(),
                            operation -> operation.getClient().getName(),
                            Operation::getOperationType,
                            operation -> operation.getBook().getTitle()))
    );

    private final static Map<DataType, DataBundle<? extends Entity>> detailsMap = Map.of(
            DataType.BOOK, new DataBundle<Book>(
                    List.of(" ID ", "Title", "Subtitle", "Author", "Publisher", "Publish date", "Free", "Client"),
                    List.of(
                            Book::getId,
                            Book::getTitle,
                            Book::getSubtitle,
                            Book::getAuthor,
                            Book::getPublisher,
                            Book::getPublishedDate,
                            book -> book.isFree() ? "Yes" : "No",
                            book -> book.getClient()
                                    .map(Client::getName)
                                    .orElse("NONE")
                    )),
            DataType.CLIENT, new DataBundle<Client>(
                    List.of("ID", "Name"),
                    List.of(
                            Client::getId,
                            Client::getName
                    ),
                    Client::getBooks),
            DataType.HISTORY, new DataBundle<Operation>(
                    List.of("ID", "Date", "Client", "Operation", "Book"),
                    List.of(
                            Operation::getId,
                            operation -> operation.getDate().toLocalDate(),
                            operation -> operation.getClient().getName(),
                            Operation::getOperationType,
                            operation -> operation.getBook().getTitle()))
    );

    public static <T extends Entity> DataBundle<T> getConsoleListBundle(DataType dataType) {
        return (DataBundle<T>) listMap.get(dataType);
    }

    public static <T extends Entity> DataBundle<T> getConsoleDetailsBundle(DataType dataType) {
        return (DataBundle<T>) detailsMap.get(dataType);
    }

    abstract public boolean isReadyToDelete();

    abstract public String getNotDeleteReason();

    abstract public String getName();

    abstract public Long getId();

}
