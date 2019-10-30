
import femto.Sprite;

public abstract class TextBlock extends MovableObject {
    private boolean mIsLit;
    
    public TextBlock(int category, int x, int y) {
        super(category, x, y);
    }
    
    public void setLit(boolean lit) {
        mIsLit = lit;
    }
    
    public boolean isLit() {
        return mIsLit;
    }
}
