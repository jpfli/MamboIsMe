
public class TransformAction extends Action {
    
    EntityType mFromType;
    EntityType mToType;
    
    // One bit for each (max 160) MovableObject indicating whether it's moving (1) or not (0)
    int mIsTransformed1, mIsTransformed2, mIsTransformed3, mIsTransformed4, mIsTransformed5;
    
    public TransformAction() {
        
    }
    
    public void init(Stage stage, EntityType fromType, EntityType toType) {
        mFromType = fromType;
        mToType = toType;
        
        if(mFromType.isTransformable()) {
            var iter = stage.createIterator(MovableObject.CATEGORY_ENTITY);
            while(iter.hasMore()) {
                var entity = (Entity)iter.next();
                if(entity.state() != MovableObject.STATE_DELETED && entity.type() == mFromType) {
                    markAsTransformed(iter.position());
                }
            }
        }
    }
    
    public boolean perform(Stage stage) {
        boolean retVal = false;
        
        var iter = stage.createIterator(MovableObject.CATEGORY_ENTITY);
        while(iter.hasMore()) {
            var entity = (Entity)iter.next();
            if(entity.state() != MovableObject.STATE_DELETED && isMarkedAsTransformed(iter.position())) {
                entity.setType(mToType);
                retVal = true;
            }
        }
        
        return retVal;
    }
    
    public void revert(Stage stage) {
        var iter = stage.createIterator(MovableObject.CATEGORY_ENTITY);
        while(iter.hasMore()) {
            var entity = (Entity)iter.next();
            if(isMarkedAsTransformed(iter.position())) {
                entity.setType(mFromType);
            }
        }
    }
    
    private void markAsTransformed(int idx) {
        int bit = 1<<(idx%32);
        int slot = idx/32;
        switch(slot) {
            case 0:
                mIsTransformed1 |= bit;
                break;
            case 1:
                mIsTransformed2 |= bit;
                break;
            case 2:
                mIsTransformed3 |= bit;
                break;
            case 3:
                mIsTransformed4 |= bit;
                break;
            case 4:
                mIsTransformed5 |= bit;
                break;
        }
    }
    
    private boolean isMarkedAsTransformed(int idx) {
        int mask = 1<<(idx%32);
        int slot = idx/32;
        switch(slot) {
            case 0:
                return (mIsTransformed1&mask) != 0;
            case 1:
                return (mIsTransformed2&mask) != 0;
            case 2:
                return (mIsTransformed3&mask) != 0;
            case 3:
                return (mIsTransformed4&mask) != 0;
            case 4:
                return (mIsTransformed5&mask) != 0;
        }
        return false;
    }
}
