package com.yml.deckofcards;

public class DeckOfCards {
    public static void run() {
        Deck deck = new Deck();
        deck.init();
        
		String playersArray[][] = deck.shuffle();
		deck.printDeck(playersArray);
    }
}
