
import femto.Image;
import femto.mode.HiRes16Color;

import images.MePropertyImage;
import images.MePropertyLitImage;
import images.GoalPropertyImage;
import images.GoalPropertyLitImage;
import images.EndPropertyImage;
import images.EndPropertyLitImage;
import images.SolidPropertyImage;
import images.SolidPropertyLitImage;
import images.PushPropertyImage;
import images.PushPropertyLitImage;
import images.DeathPropertyImage;
import images.DeathPropertyLitImage;
import images.RazePropertyImage;
import images.RazePropertyLitImage;
import images.WalkPropertyImage;
import images.WalkPropertyLitImage;
import images.HotPropertyImage;
import images.HotPropertyLitImage;
import images.MeltPropertyImage;
import images.MeltPropertyLitImage;
import images.HoverPropertyImage;
import images.HoverPropertyLitImage;

public final class PropertyText extends TextBlock {
    
    public static final int TYPE_ME=1, TYPE_GOAL=2, TYPE_END=3, TYPE_SOLID=4, TYPE_PUSH=5, TYPE_DEATH=6, TYPE_RAZE=7, TYPE_WALK=8, 
        TYPE_HOT=9, TYPE_MELT=10, TYPE_HOVER=11;
        
    private ubyte mType;
    private Image mImage;
    private Image mLitImage;
    
    PropertyText(int type, int x, int y) {
        super(CATEGORY_ATTRIBUTE, x, y);
        mType = type;
        createPropertyImages();
    }
    
    public final void modifyEntityType(EntityType entityType) {
        switch(mType) {
            case TYPE_ME:
                entityType.setPlayer(true);
                break;
            case TYPE_GOAL:
                entityType.setGoal(true);
                break;
            case TYPE_END:
                entityType.setEnd(true);
                break;
            case TYPE_SOLID:
                entityType.setSolid(true);
                break;
            case TYPE_PUSH:
                entityType.setPush(true);
                break;
            case TYPE_DEATH:
                entityType.setDeath(true);
                break;
            case TYPE_RAZE:
                entityType.setRaze(true);
                break;
            case TYPE_WALK:
                entityType.setWalk(true);
                break;
            case TYPE_HOT:
                entityType.setHot(true);
                break;
            case TYPE_MELT:
                entityType.setMelt(true);
                break;
            case TYPE_HOVER:
                entityType.setHover(true);
                break;
        }
    }
    
    public final void draw(HiRes16Color screen, long turnTime) {
        var image = super.isLit() ? mLitImage : mImage;
        super.drawImage(screen, image, turnTime);
    }
    
    private final void createPropertyImages() {
        switch(mType) {
            case TYPE_ME:
                mImage = new MePropertyImage();
                mLitImage = new MePropertyLitImage();
                break;
            case TYPE_GOAL:
                mImage = new GoalPropertyImage();
                mLitImage = new GoalPropertyLitImage();
                break;
            case TYPE_END:
                mImage = new EndPropertyImage();
                mLitImage = new EndPropertyLitImage();
                break;
            case TYPE_SOLID:
                mImage = new SolidPropertyImage();
                mLitImage = new SolidPropertyLitImage();
                break;
            case TYPE_PUSH:
                mImage = new PushPropertyImage();
                mLitImage = new PushPropertyLitImage();
                break;
            case TYPE_DEATH:
                mImage = new DeathPropertyImage();
                mLitImage = new DeathPropertyLitImage();
                break;
            case TYPE_RAZE:
                mImage = new RazePropertyImage();
                mLitImage = new RazePropertyLitImage();
                break;
            case TYPE_WALK:
                mImage = new WalkPropertyImage();
                mLitImage = new WalkPropertyLitImage();
                break;
            case TYPE_HOT:
                mImage = new HotPropertyImage();
                mLitImage = new HotPropertyLitImage();
                break;
            case TYPE_MELT:
                mImage = new MeltPropertyImage();
                mLitImage = new MeltPropertyLitImage();
                break;
            case TYPE_HOVER:
                mImage = new HoverPropertyImage();
                mLitImage = new HoverPropertyLitImage();
                break;
        }
    }
}
