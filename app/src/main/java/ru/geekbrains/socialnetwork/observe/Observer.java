package ru.geekbrains.socialnetwork.observe;

import ru.geekbrains.socialnetwork.data.CardData;

public interface Observer {
    void updateState(CardData cardData);
}
