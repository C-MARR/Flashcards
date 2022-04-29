package flashcards;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static StringBuilder logText = new StringBuilder("\n");
    public static String askForFileName = "File name:\n";
    public static void main(String[] args) {
        ArrayList<String> options = new ArrayList<>(List.of(args));
        String argImport = options.contains("-import") ? args[options.indexOf("-import") + 1] : "";
        String argExport = options.contains("-export") ? args[options.indexOf("-export") + 1] : "";
        Set<FlashCard> deck = new HashSet();
        if(!argImport.isEmpty()) {
            importDeck(deck, argImport);
        }
        String menuOptions = "Input the action " +
                "(add, remove, import, export, ask, exit, log, hardest card, reset stats):\n";
        String exitMessage = "Bye bye!\n";
        boolean endProgram = false;
        while (!endProgram) {
            Scanner scanner = new Scanner(System.in);
            System.out.print(menuOptions);
            logText.append(menuOptions);
            String option = scanner.nextLine();
            logText.append(option).append("\n");
            switch (option.toLowerCase()) {
                case "add":
                    createCard(deck);
                    break;
                case "remove":
                    removeCardFromDeck(deck);
                    break;
                case "import":
                    importDeck(deck, argImport);
                    break;
                case "export":
                    exportDeck(deck, argExport);
                    break;
                case "ask":
                    studyTime(deck);
                    break;
                case "exit":
                    endProgram = true;
                    System.out.print(exitMessage);
                    logText.append(exitMessage).append("\n");
                    if(!argExport.isEmpty()) {
                        exportDeck(deck, argExport);
                    }
                    break;
                case "log":
                    log();
                    break;
                case "hardest card":
                    findHardestCards(deck);
                    break;
                case "reset stats":
                    resetStats(deck);
                    break;
                }
            }
            System.out.println();
            logText.append("\n");
        }

    private static void resetStats(Set<FlashCard> deck) {
        deck.forEach(flashCard -> flashCard.setMistakes(0));
        String statisticsReset = "Card statistics have been reset.\n";
        System.out.print(statisticsReset);
        logText.append(statisticsReset);
    }

    private static void findHardestCards(Set<FlashCard> deck) {
        String noMistakes = "There are no cards with errors.\n";
        StringBuilder mostMistakes = new StringBuilder("The hardest card");
        LinkedList<FlashCard> mistakeDeck = new LinkedList<>();
        int highest = 0;
        for (FlashCard flashCard : deck) {
            if (highest <= flashCard.getMistakes()) {
                highest = flashCard.getMistakes();
            }
        }
        for (FlashCard flashCard : deck) {
            if (flashCard.getMistakes() == highest) {
                mistakeDeck.add(flashCard);
            }
        }
        if (highest == 0) {
            System.out.print(noMistakes);
            logText.append(noMistakes);
            return;
        } else if (mistakeDeck.size() == 1) {
            mostMistakes.append(String.format(" is \"%s\". You have %d errors answering it.\n",
                    mistakeDeck.get(0).TERM, highest));
        } else {
            mostMistakes.append("s are ");
            mistakeDeck.forEach(flashCard -> mostMistakes.append(String.format("\"%s\", ", flashCard.getTerm())));
            mostMistakes.replace(mostMistakes.length() - 2, mostMistakes.length(), "");
            mostMistakes.append(String.format(". You have %d errors answering them.\n", highest));
        }
        System.out.print(mostMistakes);
        logText.append(mostMistakes);
    }

    private static void log() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(askForFileName);
        logText.append(askForFileName);
        File file = new File(scanner.nextLine());
        logText.append(file.getName()).append("\n");
        try (FileWriter writer = new FileWriter(file)) {
            String logSaved = "The log has been saved.\n";
            System.out.print(logSaved);
            logText.append(logSaved);
            for (char charc : logText.toString().toCharArray()) {
                writer.write(charc);
            }
        } catch (IOException e) {
            System.out.print("Log Write Error");
        }
    }



    private static void studyTime(Set<FlashCard> deck) {
        String askHowManyTimes = "How many times to ask?\n";
        String answerMessage = "";
        Scanner scanner = new Scanner(System.in);
        scanner.reset();
        System.out.print(askHowManyTimes);
        logText.append(askHowManyTimes);
        String timesToAsk = scanner.nextLine();
        logText.append(timesToAsk).append("\n");
        int times = Integer.parseInt(timesToAsk);
        for (int i = 0; i < times; i++) {
            FlashCard card = deck.stream().findAny().get();
            String askForDefinition = String.format("Print the definition of \"%s\":\n", card.getTerm());
            System.out.print(askForDefinition);
            logText.append(askForDefinition);
            String guess = scanner.nextLine();
            logText.append(guess).append("\n");
            if (Objects.equals(guess, card.getDefinition())) {
                answerMessage = "Correct!\n";
                System.out.print(answerMessage);
                logText.append(answerMessage);
            } else {
                FlashCard wrongCard = FlashCard.getFlashCard(guess, deck);
                if (Objects.equals(null, wrongCard)) {
                    card.setMistakes(card.getMistakes() + 1);
                    answerMessage = String.format("Wrong. The right answer is \"%s\".\n", card.getDefinition());
                    System.out.print(answerMessage);
                    logText.append(answerMessage);
                } else {
                    card.setMistakes(card.getMistakes() + 1);
                    answerMessage = String.format("Wrong. The right answer is \"%s\", but your definition " +
                            "is correct for \"%s\".\n", card.getDefinition() , wrongCard.getTerm());
                    System.out.print(answerMessage);
                    logText.append(answerMessage);
                }
            }
        }
    }

    protected static void createCard(Set<FlashCard> deck) {
        Scanner scanner = new Scanner(System.in);
        String term = null;
        String definition = null;
        String askForCardTerm = "The Card:\n";
        System.out.print(askForCardTerm);
        logText.append(askForCardTerm);
        term = scanner.nextLine();
        logText.append(term).append("\n");
        if (term.isEmpty()) {
            return;
        }
        for (FlashCard card : deck) {
            if (Objects.equals(card.getTerm(), term)) {
                String cardExists = String.format("The card \"%s\" already exists.\n", term);
                System.out.print(cardExists);
                logText.append(cardExists);
                return;
            }
        }
        String askForCardDefinition = "The definition of the card:\n";
        System.out.print(askForCardDefinition);
        logText.append(askForCardDefinition);
        definition = scanner.nextLine();
        logText.append(definition).append("\n");
        if (definition.isEmpty()) {
            return;
        }
        for (FlashCard card : deck) {
            if (Objects.equals(card.getDefinition(), definition)) {
                String definitionExists = String.format("The definition \"%s\" already exists.\n", definition);
                System.out.print(definitionExists);
                logText.append(definitionExists);
                return;
            }
        }
        String pairAdded = String.format("The pair (\"%s\":\"%s\") has been added.\n", term, definition);
        System.out.print(pairAdded);
        logText.append(pairAdded);
        deck.add(new FlashCard(term, definition));
    }

    protected static void removeCardFromDeck(Set<FlashCard> deck) {
        Scanner scanner = new Scanner(System.in);
        String askWhichCard = "Which card?\n";
        System.out.print(askWhichCard);
        logText.append(askWhichCard);
        String input = scanner.nextLine();
        logText.append(input).append("\n");
        FlashCard card = FlashCard.getFlashCard(input, deck);
        if (Objects.equals(null, card)) {
            String cantRemove = String.format("Can't remove \"%s\": there is no such card.\n", input);
            System.out.print(cantRemove);
            logText.append(cantRemove);
            return;
        }
        String cardRemoved = "The card has been removed.\n";
        System.out.print(cardRemoved);
        logText.append(cardRemoved);
        deck.remove(card);
    }

    protected static void exportDeck(Set<FlashCard> deck, String argExport) {
        Scanner scanner = new Scanner(System.in);
        File file;
        if (argExport.isEmpty()) {
            System.out.print(askForFileName);
            logText.append(askForFileName);
            file = new File(scanner.nextLine());
            logText.append(file.getName()).append("\n");
        } else {
            file = new File(argExport);
        }
        try (FileWriter writer = new FileWriter(file)) {
            for (FlashCard flashCard : deck) {
                writer.write(String.format("%s :: %s :: %d\n",
                        flashCard.getTerm(), flashCard.getDefinition(), flashCard.getMistakes()));
            }
        } catch (IOException e) {
            System.out.print("File not found.\n");
            logText.append("File not found.\n");
            return;
        }
        String cardsSaved = String.format("%d cards have been saved.\n", deck.size());
        System.out.print(cardsSaved);
        logText.append(cardsSaved);
    }

    protected static void importDeck(Set<FlashCard> deck, String argImport) {
        Scanner scanner = new Scanner(System.in);
        File file;
        if (argImport.isEmpty()) {
            System.out.print(askForFileName);
            logText.append(askForFileName);
            file = new File(scanner.nextLine());
            logText.append(file.getName()).append("\n");
        } else {
            file = new File(argImport);
        }
        Set<FlashCard> importedDeck = new HashSet();
        Set<FlashCard> cardsToRemove = new HashSet();
        StringBuilder readFile = new StringBuilder();
        try (FileReader reader = new FileReader(file)) {
            int charc = reader.read();
            while (charc != -1) {
                readFile.append((char) charc);
                charc = reader.read();
            }
            Set<String> importedCards = readFile.toString()
                    .lines()
                    .collect(Collectors.toSet());
            for (String imports : importedCards) {
                String[] card = imports.split(" :: ");
                importedDeck.add(new FlashCard(card[0], card[1], Integer.parseInt(card[2])));
                FlashCard oldCard = FlashCard.getFlashCard(card[0], deck);
                if (!Objects.isNull(oldCard)) {
                    cardsToRemove.add(oldCard);
                }
            }
        } catch (IOException e) {
            System.out.print("File not found.\n");
            logText.append("File not found.\n");
            return;
        }
        deck.removeAll(cardsToRemove);
        deck.addAll(importedDeck);
        String cardsLoaded = String.format("%d cards have been loaded.\n", importedDeck.size());
        System.out.print(cardsLoaded);
        logText.append(cardsLoaded);
    }

}
