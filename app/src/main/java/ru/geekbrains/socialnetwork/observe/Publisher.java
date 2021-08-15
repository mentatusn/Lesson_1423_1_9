package ru.geekbrains.socialnetwork.observe;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.socialnetwork.data.CardData;

public class Publisher {
    private List<Observer> observers;

    public Publisher() {
        this.observers = new ArrayList<Observer>();
    }

    public void subscribe(Observer observer){
        observers.add(observer);
    }
    public void unsubscribe(Observer observer){
        observers.remove(observer);
    }

    public void notifyTask(CardData cardData){
        for (Observer observer:observers){
            observer.updateState(cardData);
            unsubscribe(observer);
        }
    }

}
