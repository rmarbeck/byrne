package utils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import play.Logger;
import models.Invoice;

public class InvoiceKey {
	private final static String SEPARATOR = "/";
	private Integer year;
	private Integer serial;
	
	private InvoiceKey(Integer year, Integer serial) {
		this.year = year;
		this.serial = serial;
	}
	
	public static Optional<InvoiceKey> retrieveNextInvoiceKey() {
		Optional<String> lastOneFound = readLastInvoiceKeyFromDB();
		Optional<InvoiceKey> guessedKey = tryToGuessNextKey(lastOneFound);
		return guessedKey;
	}
	
	public String toString() {
		return year+SEPARATOR+serial;
	}
	
	public static Optional<InvoiceKey> fromString(String value) {
		if (value != null) {
			Matcher matcher = Pattern.compile("^([0-9]{4})"+SEPARATOR+"([0-9]*)$").matcher(value);
			if (matcher.matches())
			    return Optional.of(new InvoiceKey(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))));
		}
		
		return Optional.empty();
	}
	
	private static Optional<String> readLastInvoiceKeyFromDB() {
		Optional<Invoice> lastOne = Invoice.findLastByKey();
		if (lastOne.isPresent())
			return Optional.of(lastOne.get().getUniqueSerialKey());
		return Optional.empty();
	}
	
	private static Optional<InvoiceKey> tryToGuessNextKey(Optional<String> keyValue) {
		if (keyValue.isPresent()) {
			Optional<InvoiceKey> foundKey = fromString(keyValue.get());
			if (foundKey.isPresent()) {
				int currentYear = java.time.Year.now().getValue();
				if (foundKey.get().year == currentYear) {
					return Optional.of(new InvoiceKey(foundKey.get().year, foundKey.get().serial+1));
				} else {
					return Optional.of(new InvoiceKey(currentYear, 1));
				}
			}
		}
		return Optional.empty();
	}

	public static int getSerial(String keyAsString) {
		return fromString(keyAsString).orElse(new InvoiceKey(java.time.Year.now().getValue(), 0)).serial;
	}
}
