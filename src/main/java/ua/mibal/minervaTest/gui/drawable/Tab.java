package ua.mibal.minervaTest.gui.drawable;

import ua.mibal.minervaTest.model.window.TabType;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public interface Tab {

    void draw();

    TabType getTabType();
}
