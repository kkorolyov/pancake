package dev.kkorolyov.pancake.platform.serialization.string;

import java.math.BigDecimal;

/**
 * Parses numerical values.
 */
public class NumberStringSerializer extends StringSerializer<BigDecimal> {
	public NumberStringSerializer() {
		super("[+-]?(\\d*\\.)?\\d+(E[+-]?\\d+)?");
	}

	@Override
	public BigDecimal read(String s) {
			return new BigDecimal(s);
	}
	@Override
	public String write(BigDecimal number) {
		return number.toString();
	}
}
