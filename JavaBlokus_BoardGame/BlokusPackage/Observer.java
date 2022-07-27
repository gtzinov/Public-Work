package BlokusPackage;

// Observer pattern
public interface Observer {
    public enum Event { PIECEPLACED, ROTATE, FLIP, GIVEUP, VICTORY }

    public void update(Event event, String[] args);
}
