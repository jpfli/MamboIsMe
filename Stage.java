
import System.memory.LDRB;

public class Stage {
    public static final int MAP_WIDTH=14, MAP_HEIGHT=11;
    
    private MovableObject[] mObjArray;
    
    public Stage() {
        
    }
    
    public MovableObjectIterator createIterator() {
        return new MovableObjectIterator(mObjArray);
    }
    
    public MovableObjectIterator createIterator(int categoryMask) {
        return new MovableObjectIterator(mObjArray, categoryMask);
    }
    
    public MovableObjectIterator createIterator(int x, int y) {
        return new MovableObjectIterator(mObjArray, x, y);
    }
    
    public MovableObjectIterator createIterator(int x, int y, int categoryMask) {
        return new MovableObjectIterator(mObjArray, x, y, categoryMask);
    }
    
    public void updateRules() {
        EntityType.MAMBO.reset();
        EntityType.DOLL.reset();
        EntityType.VAMPYR.reset();
        EntityType.SNAKE.reset();
        EntityType.ZOMBIE.reset();
        EntityType.GARLIC.reset();
        EntityType.FIRE.reset();
        EntityType.COFFIN.reset();
        EntityType.BRAIN.reset();
        EntityType.SKULL.reset();
        EntityType.MOJO.reset();
        EntityType.WALL.reset();
        EntityType.BOG.reset();
        
        // Set all text blocks unlit
        var iter = createIterator(MovableObject.CATEGORY_VERB | MovableObject.CATEGORY_NOUN | MovableObject.CATEGORY_ATTRIBUTE);
        while(iter.hasMore()) {
            var text = (TextBlock)iter.next();
            if(text.state() != MovableObject.STATE_DELETED) {
                text.setLit(false);
            }
        }
        
        iter = createIterator(MovableObject.CATEGORY_VERB);
        while(iter.hasMore()) {
            var verb = (VerbText)iter.next();
            if(verb.state() != MovableObject.STATE_DELETED) {
                verb.update(this);
            }
        }
        
        iter = createIterator(MovableObject.CATEGORY_VERB);
        while(iter.hasMore()) {
            var verb = (VerbText)iter.next();
            if(verb.state() != MovableObject.STATE_DELETED) {
                verb.litPhrases();
            }
        }
    }
    
    public void readLevel(pointer data) {
        readLevel(data, 0);
    }
    
    public void readLevel(pointer data, int numSpareSlots) {
        mObjArray = new MovableObject[getObjectCount(data)+numSpareSlots];
        
        // Read objects from map data and add to the array in the order of obj id. This determines the z-order when drawing.
        int pos = 0;
        for(int objId=0x3f; objId>0; --objId) {
            for(int tileIdx=0; tileIdx<MAP_WIDTH*MAP_HEIGHT; ++tileIdx) {
                int val = (int) LDRB(data+tileIdx);
                int dir = val>>6;
                int id = val&0x3f;
                if(id == objId) {
                    int x = tileIdx%MAP_WIDTH;
                    int y = tileIdx/MAP_WIDTH;
                    var obj = createObject(id, dir, x, y);
                    if(obj != null) {
                        mObjArray[pos] = obj;
                        ++pos;
                    }
                }
            }
        }
        
        // Clear end of the array
        while(pos<mObjArray.length) {
            mObjArray[pos] = null;
            ++pos;
        }
    }
    
    public boolean addObject(MovableObject obj) {
        for(int idx=0; idx<mObjArray.length; ++idx) {
            if(mObjArray[idx] == null) {
                mObjArray[idx] = obj;
                return true;
            }
        }
        return false;
    }
    
    private int getObjectCount(pointer data) {
        int numObjs = 0;
        for(int tileIdx=0; tileIdx<MAP_WIDTH*MAP_HEIGHT; ++tileIdx) {
            int symbol = (int) LDRB(data+tileIdx);
            int id = symbol&0x3f;
            if(id > 0) {
                ++numObjs;
            }
        }
        return numObjs;
    }
    
    private MovableObject createObject(int objId, int objDir, int x, int y) {
        MovableObject obj;
        switch(objId) {
            // Is verb
            case 1:
                obj = new IsVerb(x, y);
                break;
            
            // Properties
            case 8:
                obj = new PropertyText(PropertyText.TYPE_ME, x, y);
                break;
            case 9:
                obj = new PropertyText(PropertyText.TYPE_GOAL, x, y);
                break;
            case 10:
                obj = new PropertyText(PropertyText.TYPE_END, x, y);
                break;
            case 11:
                obj = new PropertyText(PropertyText.TYPE_SOLID, x, y);
                break;
            case 12:
                obj = new PropertyText(PropertyText.TYPE_PUSH, x, y);
                break;
            case 13:
                obj = new PropertyText(PropertyText.TYPE_DEATH, x, y);
                break;
            case 14:
                obj = new PropertyText(PropertyText.TYPE_RAZE, x, y);
                break;
            case 15:
                obj = new PropertyText(PropertyText.TYPE_WALK, x, y);
                break;
            case 16:
                obj = new PropertyText(PropertyText.TYPE_HOT, x, y);
                break;
            case 17:
                obj = new PropertyText(PropertyText.TYPE_MELT, x, y);
                break;
            case 18:
                obj = new PropertyText(PropertyText.TYPE_HOVER, x, y);
                break;
            
            // Nouns
            case 32:
                obj = new NounText(EntityType.MAMBO, x, y);
                break;
            case 33:
                obj = new NounText(EntityType.DOLL, x, y);
                break;
            case 34:
                obj = new NounText(EntityType.VAMPYR, x, y);
                break;
            case 35:
                obj = new NounText(EntityType.SNAKE, x, y);
                break;
            case 36:
                obj = new NounText(EntityType.ZOMBIE, x, y);
                break;
            case 37:
                obj = new NounText(EntityType.GARLIC, x, y);
                break;
            case 38:
                obj = new NounText(EntityType.FIRE, x, y);
                break;
            case 39:
                obj = new NounText(EntityType.COFFIN, x, y);
                break;
            case 40:
                obj = new NounText(EntityType.BRAIN, x, y);
                break;
            case 41:
                obj = new NounText(EntityType.SKULL, x, y);
                break;
            case 42:
                obj = new NounText(EntityType.MOJO, x, y);
                break;
            case 43:
                obj = new NounText(EntityType.WALL, x, y);
                break;
            case 44:
                obj = new NounText(EntityType.BOG, x, y);
                break;
            
            // Entities
            case 48:
                obj = new Entity(EntityType.MAMBO, x, y);
                break;
            case 49:
                obj = new Entity(EntityType.DOLL, x, y);
                break;
            case 50:
                obj = new Entity(EntityType.VAMPYR, x, y);
                break;
            case 51:
                obj = new Entity(EntityType.SNAKE, x, y);
                break;
            case 52:
                obj = new Entity(EntityType.ZOMBIE, x, y);
                break;
            case 53:
                obj = new Entity(EntityType.GARLIC, x, y);
                break;
            case 54:
                obj = new Entity(EntityType.FIRE, x, y);
                break;
            case 55:
                obj = new Entity(EntityType.COFFIN, x, y);
                break;
            case 56:
                obj = new Entity(EntityType.BRAIN, x, y);
                break;
            case 57:
                obj = new Entity(EntityType.SKULL, x, y);
                break;
            case 58:
                obj = new Entity(EntityType.MOJO, x, y);
                break;
            case 59:
                obj = new Entity(EntityType.WALL, x, y);
                break;
            case 60:
                obj = new Entity(EntityType.BOG, x, y);
                break;
            default:
                break;
        }
        if(obj != null) {
            switch(objDir) {
                case 0:
                    obj.setDirection(MovableObject.DIR_RIGHT);
                    break;
                case 1:
                    obj.setDirection(MovableObject.DIR_UP);
                    break;
                case 2:
                    obj.setDirection(MovableObject.DIR_LEFT);
                    break;
                case 3:
                    obj.setDirection(MovableObject.DIR_DOWN);
                    break;
            }
        }
        return obj;
    }
}
