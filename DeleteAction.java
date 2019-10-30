
public class DeleteAction extends Action {
    
    // One bit for each (max 160) MovableObject indicating whether it's moving (1) or not (0)
    int mIsDeleted1, mIsDeleted2, mIsDeleted3, mIsDeleted4, mIsDeleted5;
    
    public DeleteAction() {
        
    }
    
    public boolean perform(Stage stage) {
        boolean retVal = false;
        
        var iter = stage.createIterator();
        while(iter.hasMore()) {
            var obj = iter.next();
            var idx = iter.position();
            if(isMarkedAsDeleted(idx)) {
                obj.setState(MovableObject.STATE_DELETED);
                retVal = true;
            }
        }
        
        return retVal;
    }
    
    public void revert(Stage stage) {
        var iter = stage.createIterator();
        while(iter.hasMore()) {
            var obj = iter.next();
            var idx = iter.position();
            if(isMarkedAsDeleted(idx)) {
                // Resurrect the object
                obj.setState(MovableObject.STATE_IDLE);
            }
        }
    }
    
    public void markAsDeleted(int idx) {
        int bit = 1<<(idx%32);
        int slot = idx/32;
        switch(slot) {
            case 0:
                mIsDeleted1 |= bit;
                break;
            case 1:
                mIsDeleted2 |= bit;
                break;
            case 2:
                mIsDeleted3 |= bit;
                break;
            case 3:
                mIsDeleted4 |= bit;
                break;
            case 4:
                mIsDeleted5 |= bit;
                break;
        }
    }
    
    public boolean isMarkedAsDeleted(int idx) {
        int mask = 1<<(idx%32);
        int slot = idx/32;
        switch(slot) {
            case 0:
                return (mIsDeleted1&mask) != 0;
            case 1:
                return (mIsDeleted2&mask) != 0;
            case 2:
                return (mIsDeleted3&mask) != 0;
            case 3:
                return (mIsDeleted4&mask) != 0;
            case 4:
                return (mIsDeleted5&mask) != 0;
        }
        return false;
    }
}
