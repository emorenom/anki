package com.weekendesk.anki;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Util {

	private static final int MAX_INPUT_ATTEMPS = 10;

	public static LocalDate getToday() {

		LocalDate date = LocalDate.now();
		date.atStartOfDay();

		return date;

	}

	public static char readValidInput(String message, List<Short> validAnswers, Scanner scanner) {

		int attempts = 0;
		short input;
		do {

			System.out.print(message);
			try {
				input = (short) scanner.nextLine().charAt(0);
			} catch (StringIndexOutOfBoundsException e) {
				input = 0;
			}
			if (validAnswers.contains(input))
				return (char) input;
			attempts++;
		} while (attempts <= MAX_INPUT_ATTEMPS);

		System.out.println("You have reached the max number of attemps");

		return 0;
	}

}
