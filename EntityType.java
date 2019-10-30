
import femto.Sprite;
import femto.Image;

// Sprites
import sprites.MamboSprite;
import sprites.MojoSprite;
import sprites.WallSprite;
import sprites.SkullSprite;
import sprites.DollSprite;
import sprites.ZombieSprite;
import sprites.VampyrSprite;
import sprites.SnakeSprite;
import sprites.FireSprite;
import sprites.BogSprite;
import sprites.GarlicSprite;
import sprites.BrainSprite;
import sprites.CoffinSprite;

// Images
import images.MamboNounImage;
import images.MamboNounLitImage;
import images.MojoNounImage;
import images.MojoNounLitImage;
import images.WallNounImage;
import images.WallNounLitImage;
import images.SkullNounImage;
import images.SkullNounLitImage;
import images.DollNounImage;
import images.DollNounLitImage;
import images.ZombieNounImage;
import images.ZombieNounLitImage;
import images.VampyrNounImage;
import images.VampyrNounLitImage;
import images.SnakeNounImage;
import images.SnakeNounLitImage;
import images.FireNounImage;
import images.FireNounLitImage;
import images.BogNounImage;
import images.BogNounLitImage;
import images.GarlicNounImage;
import images.GarlicNounLitImage;
import images.BrainNounImage;
import images.BrainNounLitImage;
import images.CoffinNounImage;
import images.CoffinNounLitImage;

public abstract class EntityType {
    public static final EntityType MAMBO = new MamboEntityType();
    public static final EntityType DOLL = new DollEntityType();
    public static final EntityType VAMPYR = new VampyrEntityType();
    public static final EntityType SNAKE = new SnakeEntityType();
    public static final EntityType ZOMBIE = new ZombieEntityType();
    public static final EntityType GARLIC = new GarlicEntityType();
    public static final EntityType FIRE = new FireEntityType();
    public static final EntityType COFFIN = new CoffinEntityType();
    public static final EntityType BRAIN = new BrainEntityType();
    public static final EntityType SKULL = new SkullEntityType();
    public static final EntityType MOJO = new MojoEntityType();
    public static final EntityType WALL = new WallEntityType();
    public static final EntityType BOG = new BogEntityType();
    
    public static final int FLAG_PLAYER=1<<0, FLAG_GOAL=1<<1, FLAG_END=1<<2, FLAG_SOLID=1<<3, FLAG_PUSH=1<<4,
        FLAG_DEATH=1<<5, FLAG_RAZE=1<<6, FLAG_WALK=1<<7, FLAG_HOT=1<<8, FLAG_MELT=1<<9, FLAG_HOVER=1<<10;
    public int mRules = 0;
    
    public boolean mIsTransformable;
    private EntityType mTransformTo;
    
    private Sprite mEntitySprite;
    private Image mNounImage;
    private Image mNounLitImage;
    
    public EntityType(Sprite entitySprite, Image nounImage, Image nounLitImage) {
        mEntitySprite = entitySprite;
        mNounImage = nounImage;
        mNounLitImage = nounLitImage;
    }
    
    public final void reset() {
        mRules = 0;
        
        mIsTransformable = true;
        mTransformTo = null;
    }
    
    public boolean isTransformable() {
        return mIsTransformable;
    }
    
    public void setTransformable(boolean val) {
        mIsTransformable = val;
    }
    
    public boolean isPlayer() {
        return (mRules&FLAG_PLAYER) != 0;
    }
    
    public void setPlayer(boolean val) {
        if(val) {
            mRules |= FLAG_PLAYER;
        }
        else {
            mRules &= ~FLAG_PLAYER;
        }
    }
    
    public boolean isGoal() {
        return (mRules&FLAG_GOAL) != 0;
    }
    
    public void setGoal(boolean val) {
        if(val) {
            mRules |= FLAG_GOAL;
        }
        else {
            mRules &= ~FLAG_GOAL;
        }
    }
    
    public boolean isEnd() {
        return (mRules&FLAG_END) != 0;
    }
    
    public void setEnd(boolean val) {
        if(val) {
            mRules |= FLAG_END;
        }
        else {
            mRules &= ~FLAG_END;
        }
    }
    
    public boolean isSolid() {
        return (mRules&FLAG_SOLID) != 0;
    }
    
    public void setSolid(boolean val) {
        if(val) {
            mRules |= FLAG_SOLID;
        }
        else {
            mRules &= ~FLAG_SOLID;
        }
    }
    
    public boolean isPush() {
        return (mRules&FLAG_PUSH) != 0;
    }
    
    public void setPush(boolean val) {
        if(val) {
            mRules |= FLAG_PUSH;
        }
        else {
            mRules &= ~FLAG_PUSH;
        }
    }
    
    public boolean isDeath() {
        return (mRules&FLAG_DEATH) != 0;
    }
    
    public void setDeath(boolean val) {
        if(val) {
            mRules |= FLAG_DEATH;
        }
        else {
            mRules &= ~FLAG_DEATH;
        }
    }
    
    public boolean isRaze() {
        return (mRules&FLAG_RAZE) != 0;
    }
    
    public void setRaze(boolean val) {
        if(val) {
            mRules |= FLAG_RAZE;
        }
        else {
            mRules &= ~FLAG_RAZE;
        }
    }
    
    public boolean isWalk() {
        return (mRules&FLAG_WALK) != 0;
    }
    
    public void setWalk(boolean val) {
        if(val) {
            mRules |= FLAG_WALK;
        }
        else {
            mRules &= ~FLAG_WALK;
        }
    }
    
    public boolean isHot() {
        return (mRules&FLAG_HOT) != 0;
    }
    
    public void setHot(boolean val) {
        if(val) {
            mRules |= FLAG_HOT;
        }
        else {
            mRules &= ~FLAG_HOT;
        }
    }
    
    public boolean isMelt() {
        return (mRules&FLAG_MELT) != 0;
    }
    
    public void setMelt(boolean val) {
        if(val) {
            mRules |= FLAG_MELT;
        }
        else {
            mRules &= ~FLAG_MELT;
        }
    }
    
    public boolean isHover() {
        return (mRules&FLAG_HOVER) != 0;
    }
    
    public void setHover(boolean val) {
        if(val) {
            mRules |= FLAG_HOVER;
        }
        else {
            mRules &= ~FLAG_HOVER;
        }
    }
    
    public final EntityType getTransformTo() {
        return mTransformTo;
    }
    
    public final void setTransformTo(EntityType target) {
        mTransformTo = target;
    }

    public final Sprite getEntitySprite() {
        return mEntitySprite;
    }
    
    public final Image getNounImage() {
        return mNounImage;
    }
    
    public final Image getNounLitImage() {
        return mNounLitImage;
    }
}

final class MamboEntityType extends EntityType {
    public MamboEntityType() {
        super(new MamboSprite(), new MamboNounImage(), new MamboNounLitImage());
        ((MamboSprite) super.mEntitySprite).idle();
    }
}

final class MojoEntityType extends EntityType {
    public MojoEntityType() {
        super(new MojoSprite(), new MojoNounImage(), new MojoNounLitImage());
        ((MojoSprite) super.mEntitySprite).idle();
    }
}

final class WallEntityType extends EntityType {
    public WallEntityType() {
        super(new WallSprite(), new WallNounImage(), new WallNounLitImage());
        ((WallSprite) super.mEntitySprite).idle();
    }
}

final class SkullEntityType extends EntityType {
    public SkullEntityType() {
        super(new SkullSprite(), new SkullNounImage(), new SkullNounLitImage());
        ((SkullSprite) super.mEntitySprite).idle();
    }
}

final class DollEntityType extends EntityType {
    public DollEntityType() {
        super(new DollSprite(), new DollNounImage(), new DollNounLitImage());
        ((DollSprite) super.mEntitySprite).idle();
    }
}

final class ZombieEntityType extends EntityType {
    public ZombieEntityType() {
        super(new ZombieSprite(), new ZombieNounImage(), new ZombieNounLitImage());
        ((ZombieSprite) super.mEntitySprite).idle();
    }
}

final class VampyrEntityType extends EntityType {
    public VampyrEntityType() {
        super(new VampyrSprite(), new VampyrNounImage(), new VampyrNounLitImage());
        ((VampyrSprite) super.mEntitySprite).idle();
    }
}

final class SnakeEntityType extends EntityType {
    public SnakeEntityType() {
        super(new SnakeSprite(), new SnakeNounImage(), new SnakeNounLitImage());
        ((SnakeSprite) super.mEntitySprite).idle();
    }
}

final class FireEntityType extends EntityType {
    public FireEntityType() {
        super(new FireSprite(), new FireNounImage(), new FireNounLitImage());
        ((FireSprite) super.mEntitySprite).idle();
    }
}

final class BogEntityType extends EntityType {
    public BogEntityType() {
        super(new BogSprite(), new BogNounImage(), new BogNounLitImage());
        ((BogSprite) super.mEntitySprite).idle();
    }
}

final class GarlicEntityType extends EntityType {
    public GarlicEntityType() {
        super(new GarlicSprite(), new GarlicNounImage(), new GarlicNounLitImage());
        ((GarlicSprite) super.mEntitySprite).idle();
    }
}

final class BrainEntityType extends EntityType {
    public BrainEntityType() {
        super(new BrainSprite(), new BrainNounImage(), new BrainNounLitImage());
        ((BrainSprite) super.mEntitySprite).idle();
    }
}

final class CoffinEntityType extends EntityType {
    public CoffinEntityType() {
        super(new CoffinSprite(), new CoffinNounImage(), new CoffinNounLitImage());
        ((CoffinSprite) super.mEntitySprite).idle();
    }
}
