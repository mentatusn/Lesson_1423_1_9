package ru.geekbrains.socialnetwork.data;

public interface CardsSource {
    CardData getCardData(int position);
    int size();

    void deleteCardData(int position);
    void updateCardData(int position,CardData newCardData);
    void addCardData(CardData newCardData);
    void clearCardData();
}
