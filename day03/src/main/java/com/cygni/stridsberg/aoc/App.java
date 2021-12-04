/*
 * This Java source file was generated by the Gradle 'init' task.
 */

package com.cygni.stridsberg.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;


public class App {
    private final List<String> input;

    public App(List<String> input) {
        this.input = input;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("java");
        List<String> input = parseInput("input.txt");

        String part = System.getenv("part") == null ? "part1" : System.getenv("part");
        if (part.equals("part2")) {
            System.out.println(new App(input).getSolutionPart2().join());
        } else {
            System.out.println(new App(input).getSolutionPart1());
        }
    }

    private static List<String> parseInput(String filename) throws IOException {
        return Files.lines(Path.of(filename))
            .collect(Collectors.toList());
    }

    public Integer getSolutionPart1() {
        StringBuilder gammaRate = new StringBuilder();
        StringBuilder epsilonRate = new StringBuilder();
        for (int i = 0; i < input.get(0).length(); i++) {
            var currentRow = getFirstBitsAsString(input, i);
            if (getMostCommonBit(currentRow).equals("0")) {
                gammaRate.append("0");
                epsilonRate.append("1");
            } else {
                gammaRate.append("1");
                epsilonRate.append("0");
            }
        }

        return Integer.parseInt(gammaRate.toString(), 2) * Integer.parseInt(epsilonRate.toString(), 2);
    }

    public CompletableFuture<Integer> getSolutionPart2() {
        var oxygenGeneratorFuture = CompletableFuture.supplyAsync(() -> getOxygenGeneratorRating(input));
        var co2ScrubberRatingFuture = CompletableFuture.supplyAsync(() -> getCO2ScrubberRating(input));
        return oxygenGeneratorFuture.thenCombine(co2ScrubberRatingFuture, (oxygenGeneratorBinaryLine, co2ScrubberRatingLine) ->
            Integer.parseInt(oxygenGeneratorBinaryLine, 2) * Integer.parseInt(co2ScrubberRatingLine, 2));
    }

    private String getOxygenGeneratorRating(List<String> input) {
        return filterOutValue(input, 0, this::getMostCommonBit);
    }

    private String getCO2ScrubberRating(List<String> input) {
        return filterOutValue(input, 0, this::getLeastCommonBit);
    }


    private String filterOutValue(List<String> input, int index, Function<String, String> occurrenceFunction) {
        String firstBitsAsString = getFirstBitsAsString(input, index);
        var mostCommonBit = occurrenceFunction.apply(firstBitsAsString);
        var filteredList = getFilteredList(input, index, mostCommonBit);
        if (filteredList.size() > 1) {
            return filterOutValue(filteredList, ++index, occurrenceFunction);
        } else {
            return filteredList.get(0);
        }
    }

    private List<String> getFilteredList(List<String> input, int finalIndex, String bit) {
        var asChar = bit.charAt(0);
        return input
            .stream()
            .filter(line -> line.charAt(finalIndex) == asChar)
            .collect(Collectors.toList());
    }

    private String getLeastCommonBit(String firstBitsAsString) {
        return getMostCommonBit(firstBitsAsString).equals("0") ? "1" : "0";
    }

    private String getMostCommonBit(String bitString) {
        return countOccurrences(bitString, '0') > countOccurrences(bitString, '1') ? "0" : "1";
    }

    private String getFirstBitsAsString(List<String> input, int index) {
        return input.stream()
            .map(line -> line.charAt(index))
            .map(Object::toString)
            .collect(Collectors.joining());
    }

    private long countOccurrences(String firstRow, char c) {
        return firstRow.chars().filter(ch -> ch == c).count();
    }

}
