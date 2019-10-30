
public abstract class Action {
    public Action next;
    
    public abstract boolean perform(Stage stage);
    public abstract void revert(Stage stage);
}
