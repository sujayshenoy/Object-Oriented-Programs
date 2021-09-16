package com.yml.deckofcards;

import com.yml.linkedlist.Node;
import com.yml.queue.Queue;

public class DeckOfCards {
    public static void run() {
        Deck deck = new Deck();
        deck.init();
        Queue<Player> players = new Queue<Player>();

        int numberOfPlayers = 4;
        while (numberOfPlayers-- > 0) {
            players.enqueue(new Player());
        }

        deck.shuffle(players);
        for (Node<Player> player : players) {
            player.getData().sortByRank();
        }

        deck.printDeck(players);
    }
}
