

package com.corvid.genericdto.shared.time;

interface TimeUnitConversionFactors {
    int millisecondsPerSecond = 1000;
    int millisecondsPerMinute = 60 * millisecondsPerSecond;
    int millisecondsPerHour = 60 * millisecondsPerMinute;
    int millisecondsPerDay = 24 * millisecondsPerHour;
    int millisecondsPerWeek = 7 * millisecondsPerDay;
    int monthsPerQuarter = 3;
    int monthsPerYear = 12;
}