
import femto.Sprite;
import femto.mode.HiRes16Color;

class NounText extends TextBlock {
    private EntityType mEntityType;
    
    public NounText(EntityType entityType, int x, int y) {
        super(CATEGORY_NOUN, x, y);
        mEntityType = entityType;
    }
    
    public void draw(HiRes16Color screen, long turnTime)
    {
        var image = super.isLit() ? mEntityType.getNounLitImage() : mEntityType.getNounImage();
        super.drawImage(screen, image, turnTime);
    }
    
    public EntityType entityType() {
        return mEntityType;
    }
    
    public void setEntityType(EntityType entityType) {
        mEntityType = entityType;
    }
}
