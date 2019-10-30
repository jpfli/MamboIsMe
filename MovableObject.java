
import femto.mode.HiRes16Color;
import femto.Image;
import femto.Sprite;

abstract class MovableObject {
    public static final int CATEGORY_ENTITY=1<<0, CATEGORY_VERB=1<<1, CATEGORY_NOUN=1<<2, CATEGORY_ATTRIBUTE=1<<3, 
        CATEGORY_MAPTILE=1<<4, CATEGORY_LETTERTILE=1<<5;
    private ubyte mCategory;
    
    public static final int DIR_RIGHT=0, DIR_UP=1, DIR_LEFT=2, DIR_DOWN=3;
    private ubyte mStepAndDir; // Upper nibble is direction, lower nibble is dx (lower 2 bits) and dy (higher 2 bits)
    public ubyte mPosXY; // Position in tile units, lower nibble is x, upper nibble is y
    
    private static final int STATE_IDLE=0, STATE_WALK=1, STATE_BUMP=2, STATE_DELETED=3;
    private ubyte mState = STATE_IDLE;
    
    public MovableObject(int category, int x, int y) {
        mCategory = category;
        mPosXY = (x&0xf) | (y<<4);
    }
    
    public abstract void draw(HiRes16Color screen, long turnTime);
    
    public void drawImage(HiRes16Color screen, Image image, long turnTime) {
        int signX = 0;
        int signY = 0;
        int direction = mStepAndDir>>4;
        switch(direction) {
            case MovableObject.DIR_RIGHT:
                signX = 1;
                break;
            case MovableObject.DIR_UP:
                signY = -1;
                break;
            case MovableObject.DIR_LEFT:
                signX = -1;
                break;
            case MovableObject.DIR_DOWN:
                signY = 1;
                break;
        }
        
        var offset = 16*turnTime/LevelState.TURN_DURATION;
        if(offset > 16) {
            offset = 16;
        }
        
        int dx = (mStepAndDir<<(32-2)) >> 32-2;
        int dy = (mStepAndDir<<(32-4)) >> 32-2;
        int imgX = 16*(this.x()+dx)-2;
        int imgY = 16*(this.y()+dy);
        if(mState== STATE_WALK) {
            imgX -= signX*(16-offset);
            imgY -= signY*(16-offset);
        } else if(mState == STATE_BUMP) {
            if(offset > 8) {
                offset = 16-offset;
            }
            imgX += signX*offset/2;
            imgY += signY*offset/2;
        }
        
        image.draw(screen, imgX, imgY);
    }
    
    public void drawSprite(HiRes16Color screen, Sprite sprite, long turnTime) {
        int signX = 0;
        int signY = 0;
        int direction = mStepAndDir>>4;
        switch(direction) {
            case MovableObject.DIR_RIGHT:
                signX = 1;
                break;
            case MovableObject.DIR_UP:
                signY = -1;
                break;
            case MovableObject.DIR_LEFT:
                signX = -1;
                break;
            case MovableObject.DIR_DOWN:
                signY = 1;
                break;
        }
        
        var offset = 16*turnTime/LevelState.TURN_DURATION;
        if(offset > 16) {
            offset = 16;
        }
        
        int dx = (mStepAndDir<<(32-2)) >> 32-2;
        int dy = (mStepAndDir<<(32-4)) >> 32-2;
        int sprX = 16*(this.x()+dx)-2;
        int sprY = 16*(this.y()+dy);
        if(mState== STATE_WALK) {
            sprX -= signX*(16-offset);
            sprY -= signY*(16-offset);
        } else if(mState == STATE_BUMP) {
            if(offset > 4) {
                offset = (16-offset)/3;
            }
            sprX += signX*offset;
            sprY += signY*offset;
        }
        
        sprite.draw(screen, sprX, sprY);
    }
    
    public final int category() {
        return mCategory;
    }
    
    public final int x() {
        return mPosXY&0xf;
    }
    
    public final int y() {
        return mPosXY>>4;
    }
    
    public final int direction() {
        return mStepAndDir>>4;
    }
    
    public void setDirection(int dir) {
        mStepAndDir = (mStepAndDir&0xf) | (dir<<4);
    }
    
    public final int state() {
        return mState;
    }
    
    public final void setState(int state) {
        mState = state;
    }
    
    public final void walk(int stepX, int stepY) {
        mStepAndDir = (mStepAndDir&0xf0) | (stepX&0x3) | ((stepY&0x3)<<2);
        
        if(stepX > 0) {
            setDirection(DIR_RIGHT);
        } else if(stepY < 0) {
            setDirection(DIR_UP);
        } else if(stepX < 0) {
            setDirection(DIR_LEFT);
        } else if(stepY > 0) {
            setDirection(DIR_DOWN);
        }
        
        mState = STATE_WALK;
    }
    
    public final void jump(int stepX, int stepY) {
        mStepAndDir = (mStepAndDir&0xf0) | (stepX&0x3) | ((stepY&0x3)<<2);
        
        mState = STATE_IDLE;
    }
    
    public final void bump(int stepX, int stepY) {
        mStepAndDir = mStepAndDir&0xf0;
        
        if(stepX > 0) {
            setDirection(DIR_RIGHT);
        } else if(stepY < 0) {
            setDirection(DIR_UP);
        } else if(stepX < 0) {
            setDirection(DIR_LEFT);
        } else if(stepY > 0) {
            setDirection(DIR_DOWN);
        }
        
        mState = STATE_BUMP;
    }
    
    public final void onTurnEnd() {
        mState = STATE_IDLE;
        
        int dx = (mStepAndDir<<(32-2)) >> 32-2;
        int dy = (mStepAndDir<<(32-4)) >> 32-2;
        int x = this.x() + dx;
        int y = this.y() + dy;
        mPosXY = (x&0xf) | (y<<4);
        
        mStepAndDir &= 0xf0;
    }
}
