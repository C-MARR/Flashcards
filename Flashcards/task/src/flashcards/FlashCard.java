package flashcards;

import java.util.Objects;
import java.util.Set;

public class FlashCard {

    final String TERM;
    final String DEFINITION;
    int mistakes = 0;

    public FlashCard(String DEFINITION, String FRONT) {
        this.TERM = DEFINITION;
        this.DEFINITION = FRONT;
    }

    public FlashCard(String DEFINITION, String FRONT, int mistakes) {
        this.TERM = DEFINITION;
        this.DEFINITION = FRONT;
        this.mistakes = mistakes;
    }

    public String getTerm() {
        return TERM;
    }

    public String getDefinition() {
        return DEFINITION;
    }

    public void setMistakes(int mistakes) {
        this.mistakes = mistakes;
    }

    public int getMistakes() {
        return mistakes;
    }

    public static FlashCard getFlashCard(String flashCardToFind, Set<FlashCard> deck) {
        for (FlashCard s : deck) {
            if (Objects.equals(s.getTerm(), flashCardToFind) || Objects.equals(s.getDefinition(), flashCardToFind)) {
                return s;
            }
        }
        return null;
    }
}
