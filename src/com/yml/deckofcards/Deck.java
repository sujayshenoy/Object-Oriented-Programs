package com.yml.deckofcards;

import java.util.*;

import com.yml.linkedlist.Node;
import com.yml.queue.Queue;

public class Deck {
    Random random = new Random();
	Set<Integer> cardNumber = new HashSet<Integer>();
	String suit[] = {"Clubs", "Diamonds", "Hearts", "Spades"};
	String rank[] = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
    Card cards[] = new Card[52];
    
	Deck() {

	}
	
	public void init() {
		for (int i = 0; i < 52; i++) {
			cards[i] = new Card();
		}
		int count =0;
		for(int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				cards[count].setSuit(suit[i]);
				cards[count].setRank(rank[j]);
				count ++;
			}
		}
	}
	
	public int getRandom() {
		int randomNumber = random.nextInt(52);
		while(cardNumber.contains(randomNumber)) {
			randomNumber = random.nextInt(52);
		}
		cardNumber.add(randomNumber);
		return randomNumber;
	}
	
	public void shuffle(Queue<Player> players) {
		for (Node<Player> player : players) {
			int count = 9;
			while (count-- > 0) {
				int randomNumber = getRandom();
				player.getData().addCard(cards[randomNumber]);
			}
		}
	}
	
	public void printDeck(Queue<Player> players) {
		int count = 1;
		for (Node<Player> player : players) {
			System.out.println("Player : " + count);
			for (Node<Card> card : player.getData().getDeckOfCards()) {
				System.out.println(card.getData() + " ");
			}
			System.out.println();
			count++;
		}
	}
	
}
