package com.yml.deckofcards;

import com.yml.linkedlist.Node;
import com.yml.queue.Queue;

public class Player {
    private Queue<Card> deckOfCards;

    Player() {
        deckOfCards = new Queue<Card>();
    }

    public Queue<Card> getDeckOfCards() {
        return deckOfCards;
    }

    public void setDeckOfCards(Queue<Card> deckOfCards) {
        this.deckOfCards = deckOfCards;
    }

    public void addCard(Card card) {
        deckOfCards.enqueue(card);
    }

    public int getActualValue(String value) {
        int actualValue = 0;

        try{
            actualValue = Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            switch (value) {
                case "Jack":
                    actualValue = 11;
                    break;
                case "Queen":
                    actualValue = 12;
                    break;
                case "King":
                    actualValue = 13;
                    break;
                case "Ace":
                    actualValue = 14;
                    break;
            }
        }

        return actualValue;
    }

    public void sortByRank() {
        int size = deckOfCards.size();
        while (size-- >0) {
            Node<Card> current = deckOfCards.getFront();
            Node<Card> temp = current;
            temp = temp.getNext();
            while (temp != null) {
                int actualValue = getActualValue(current.getData().getRank());
                int tempActualValue = getActualValue(temp.getData().getRank());
                if (actualValue > tempActualValue) {
                    Card tempor = current.getData();
                    current.setData(temp.getData());
                    temp.setData(tempor);
                }
                current = temp;
                temp = temp.getNext();
            }
        }
    }
}
