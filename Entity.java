
import femto.mode.HiRes16Color;

public final class Entity extends MovableObject {
    private EntityType mType;
    private boolean mMirrored;

    public Entity(EntityType type, int x, int y) {
        super(CATEGORY_ENTITY, x, y);
        this.mType = type;
    }
    
    public void setDirection(int dir) {
        super.setDirection(dir);
        
        if(dir == MovableObject.DIR_LEFT) {
            mMirrored = true;
        } else if(dir == MovableObject.DIR_RIGHT) {
            mMirrored = false;
        }
    }
    
    public EntityType type() {
        return this.mType;
    }
    
    public void setType(EntityType type) {
        this.mType = type;
    }
    
    public void draw(HiRes16Color screen, long turnTime) {
        var sprite = mType.getEntitySprite();
        sprite.setMirrored(mMirrored);
        super.drawSprite(screen, sprite, turnTime);
    }
}
