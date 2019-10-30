
public class ActionStack {
    public static final int MAX_SIZE = 12;
    
    private Action mArray[];
    private int mLength;
    
    public ActionStack() {
        mArray = new Action[MAX_SIZE];
    }
    
    public int length() {
        return mLength;
    }
    
    public void push(Action action) {
        for(int idx=mArray.length-1; idx>0; --idx) {
            mArray[idx] = mArray[idx-1];
        }
        mArray[0] = action;
        
        if(mLength < mArray.length) {
            ++mLength;
        }
    }
    
    public Action pop() {
        Action action;
        if(mLength > 0) {
            action = mArray[0];
            for(int idx=1; idx<mArray.length; ++idx) {
                mArray[idx-1] = mArray[idx];
            }
            mArray[mArray.length-1] = null;
            
            --mLength;
        }
        return action;
    }
}

private class StackItem {
    Action action;
    StackItem next;
    
    public StackItem(Action a) {
        action = a;
    }
}
