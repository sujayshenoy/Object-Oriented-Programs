package com.yml.deckofcards;

import java.util.*;

public class Deck {
    Random random = new Random();
	Set<Integer> cardNumber = new HashSet<Integer>();
	String suit[] = {"Clubs", "Diamonds", "Hearts", "Spades"};
	String rank[] = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
    String cards[] = new String[53];
    
	Deck() {

	}
	
	public void init() {
		int count =0;
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j<13; j++) {
				cards[count] = suit[i]+""+rank[j];
				count ++;
			}
		}
	}
	
	public int getRandom() {
		int randomNumber = random.nextInt(53);
		while(cardNumber.contains(randomNumber)) {
			randomNumber = random.nextInt(53);
		}
		cardNumber.add(randomNumber);
		return randomNumber;
	}
	
	public String[][] shuffle() {
		String[][] playersArray = new String[4][9];
		for(int i = 0; i < 4; i++) {
			for(int j = 0 ; j < 9; j++) {
				int randomNumber = getRandom();
				playersArray[i][j] = cards[randomNumber];
			}
		}
		return playersArray;
	}
	
	public void printDeck(String[][] playersArray) {
		for(int i=0; i<4; i++) {
			System.out.print("Player "+(i+1)+" : ");
			for(int j = 0 ; j < 9; j++) {
				System.out.print(playersArray[i][j]+"     ");
			}
			System.out.println();
		}
	}
}
