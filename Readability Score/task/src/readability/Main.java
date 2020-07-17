package readability;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String filename = args[0];
        String text = getTextFromFile(filename);

        String[] sentences = text.split("[.?!]");
        String textWithoutPunctuationMarks = text.replaceAll("[,.?!]", "");
        String[] words = textWithoutPunctuationMarks.split("\\s+");

        int numberOfSentences = sentences.length;
        int numberOfWords = words.length;
        int numberOfCharacters = text.replaceAll("\\s+", "").length();
        int[] syllablesAndPolysyllables = countSyllablesAndPolysyllables(words);
        int numberOfSyllables = syllablesAndPolysyllables[0];
        int numberOfPolysyllables = syllablesAndPolysyllables[1];

        System.out.println("java Main " + filename);
        System.out.println("The text is:");
        System.out.println(text + "\n");

        System.out.println("Words: " + numberOfWords);
        System.out.println("Sentences: " + numberOfSentences);
        System.out.println("Characters: " + numberOfCharacters);
        System.out.println("Syllables: " + numberOfSyllables);
        System.out.println("Polysyllables: " + numberOfPolysyllables);

        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        Scanner scanner = new Scanner(System.in);
        String inputWhichScore = scanner.next();
        System.out.println();

        double scoreARI = 4.71 * (numberOfCharacters / (double) numberOfWords) + 0.5 * (numberOfWords / (double) numberOfSentences) - 21.43;
        int ageARI = getAgeFromScore(scoreARI);
        String resultTextForARI = "Automated Readability Index: " + Math.round(scoreARI * 100) / 100.0 + " (about " + ageARI + " year olds).";

        double scoreFK = 0.39 * (numberOfWords / (double) numberOfSentences) + 11.8 * (numberOfSyllables / (double) numberOfWords) - 15.59;
        int ageFK = getAgeFromScore(scoreFK);
        String resultTextForFK = "Flesch–Kincaid readability tests: " + Math.round(scoreFK * 100) / 100.0 + " (about " + ageFK + " year olds).";

        double scoreSMOG = 1.043 * Math.sqrt(numberOfPolysyllables * (30.0 / (double) numberOfSentences)) + 3.1291;
        int ageSMOG = getAgeFromScore(scoreSMOG);
        String resultTextForSMOG = "Simple Measure of Gobbledygook: " + Math.round(scoreSMOG * 100) / 100.0 + " (about " + ageSMOG + " year olds).";

        double scoreCL = 0.0588 * (numberOfCharacters * 100.0 / numberOfWords) - 0.296 * (numberOfSentences * 100.0 / numberOfWords) - 15.8;
        int ageCL = getAgeFromScore(scoreCL);
        String resultTextForCL = "Coleman–Liau index: " + Math.round(scoreCL * 100) / 100.0 + " (about " + ageCL + " year olds).";

        switch (inputWhichScore) {
            case "all":
                System.out.println(resultTextForARI);
                System.out.println(resultTextForFK);
                System.out.println(resultTextForSMOG);
                System.out.println(resultTextForCL);
                System.out.println("\nThis text should be understood in average by " + (ageARI + ageFK + ageSMOG + ageCL) / 4.0 + " year olds.");
            case "ARI":
                System.out.println(resultTextForARI);
            case "FK":
                System.out.println(resultTextForFK);
            case "SMOG":
                System.out.println(resultTextForSMOG);
            case "CL":
                System.out.println(resultTextForCL);
        }

    }

    private static int[] countSyllablesAndPolysyllables(String[] words) {

        int vowelsInAWord = 0;
        int syllables = 0;
        int polySyllables = 0;
        for (String word : words) {
            String prevCharacter = "";
            for (int i = 0; i < word.length(); i++) {
                String character = String.valueOf(word.charAt(i));
                if (i == word.length() - 1 && character.equals("e")) break;
                if (character.matches("[AEIOUYaeiuoy]")) {
                    if (!prevCharacter.matches("[AEIOUYaeiuoy]")) {
                        vowelsInAWord++;
                    }
                }
                prevCharacter = character;
            }
            if (vowelsInAWord == 0) {
                vowelsInAWord = 1;
            }
            syllables += vowelsInAWord;
            if (vowelsInAWord > 2) {
                polySyllables++;
            }
            vowelsInAWord = 0;
        }
        return new int[]{syllables, polySyllables};
    }

    private static String getTextFromFile(String filename) {
        File file = new File(filename);
        StringBuilder text = new StringBuilder();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                text.append(scanner.nextLine());
            }
        } catch (IOException e) {
            System.out.println("No file found: " + filename);
        }
        return text.toString();
    }

    private static int getAgeFromScore(double score) {
        int roundedScore = (int) Math.round(score);
        int years = 6; // if (score == 1)

        switch (roundedScore) {
            case 2:
                years = 7;
                break;
            case 3:
                years = 9;
                break;
            case 4:
                years = 10;
                break;
            case 5:
                years = 11;
                break;
            case 6:
                years = 12;
                break;
            case 7:
                years = 13;
                break;
            case 8:
                years = 14;
                break;
            case 9:
                years = 15;
                break;
            case 10:
                years = 16;
                break;
            case 11:
                years = 17;
                break;
            case 12:
                years = 18;
                break;
            case 13:
            case 14: // 24+ years
                years = 24;
                break;
        }

        return years;
    }

}
