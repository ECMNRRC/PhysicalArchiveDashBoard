package com.dataserve.pad.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahDate;
import java.time.chrono.HijrahEra;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

public class DateUtil {
	public static LocalDate toMuslim(LocalDateTime gregorianDate) {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		final String hijrahDateString = HijrahChronology.INSTANCE.date(gregorianDate).format(formatter);

		return LocalDate.parse(hijrahDateString, formatter);
	}

	public static LocalDate toMuslim(LocalDate gregorianDate) {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		final String hijrahDateString = HijrahChronology.INSTANCE.date(gregorianDate).format(formatter);

		return LocalDate.parse(hijrahDateString, formatter);
	}
	public static LocalDate toGregorian(LocalDate muslimDate) {
		final HijrahDate hijrahDate = HijrahChronology.INSTANCE.date(HijrahEra.AH,
				muslimDate.get(ChronoField.YEAR_OF_ERA), muslimDate.get(ChronoField.MONTH_OF_YEAR),
				muslimDate.get(ChronoField.DAY_OF_MONTH));

		return IsoChronology.INSTANCE.date(hijrahDate);
	}
	
	public static int getCurrentYear() {
		return  Year.now().getValue();
	}
}
