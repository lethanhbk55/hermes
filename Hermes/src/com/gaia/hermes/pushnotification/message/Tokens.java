package com.gaia.hermes.pushnotification.message;

import java.util.ArrayList;
import java.util.List;

public class Tokens {
	public static List<List<String>> splitTokens(List<String> tokens, int packSize) {
		List<List<String>> result = new ArrayList<>();
		int size = tokens.size();
		int currentIndex = 0;
		while (currentIndex < size) {
			if (size - currentIndex < packSize) {
				result.add(tokens.subList(currentIndex, size));
				currentIndex = size;
			} else {
				result.add(tokens.subList(currentIndex, currentIndex + packSize));
				currentIndex += packSize;
			}
		}
		return result;
	}
}
