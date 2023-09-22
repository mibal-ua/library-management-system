package ua.mibal.minervaTest.utils;

import ua.mibal.minervaTest.model.Client;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author Mykhailo Balakhon
 * @link t.me/mibal_ua
 */
public class Clients {

    public static Client ofMapping(List<String> answers) {
        return new Client(answers.iterator().next());
    }

    public static Client copyOf(Client originalClient) {
        return new Client(
                originalClient.getId(),
                originalClient.getName()
        );
    }

    public static Client changeByMapping(Client editedClient, List<String> answers) {
        List<Consumer<String>> consumers = List.of(
                editedClient::setName
        );
        for (int i = 0; i < consumers.size(); i++) {
            String answer = answers.get(i);
            if (!answer.isEmpty()) consumers.get(i).accept(answer);
        }
        return editedClient;
    }
}
