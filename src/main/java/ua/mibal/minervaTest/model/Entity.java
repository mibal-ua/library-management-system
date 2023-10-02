package ua.mibal.minervaTest.model;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public interface Entity {

    boolean isReadyToDelete();

    String getNotDeleteReason();

    String getName();

    Long getId();
}
