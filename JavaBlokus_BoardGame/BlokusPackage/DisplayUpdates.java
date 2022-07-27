package BlokusPackage;

//Psuedo command pattern that isn't as involved as when typically seen  but we have our JComponents that contain other components implement this interface, having initalize and update methods to
//be able to easily initialize and update all when needed was quite handy.

public interface DisplayUpdates {
    public void initialize(Game game);
    public void update(Game game);
}
