/* eMafiaClient - Comparator_PlayerLobby.java
   Copyright (C) 2012  Matthew 'Apocist' Davis */
package com.inverseinnovations.eMafiaClient.classes;

import java.text.Collator;
import java.util.Comparator;

import com.inverseinnovations.eMafiaClient.classes.data.List_Character;

/**Sorts Character list by alphabet*/
public class Comparator_PlayerLobby implements Comparator<List_Character> {

	private Collator collator = Collator.getInstance();

	@Override
	public int compare(List_Character o1, List_Character o2) {
		return compareString(o1.name, o2.name);
	}
	private int compareString(String o1, String o2) {
		return collator.compare(o1, o2);
	}

}
