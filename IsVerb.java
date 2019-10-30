
import femto.Image;
import femto.mode.HiRes16Color;

import images.IsVerbImage;
import images.IsVerbLitImage;

class IsVerb extends VerbText {
    private boolean mIsLit;
    
    private Image mImage;
    private Image mLitImage;
    
    public IsVerb(int x, int y) {
        super(x, y);
        mImage = new IsVerbImage();
        mLitImage = new IsVerbLitImage();
    }
    
    public void setLit(boolean lit) {
        mIsLit = lit;
    }
    
    public void draw(HiRes16Color screen, long turnTime) {
        Image image = mIsLit ? mLitImage : mImage;
        super.drawImage(screen, image, turnTime);
    }
    
    public void update(Stage stage) {
        
        super.buildHorizontalPhrase(stage);
        super.buildVerticalPhrase(stage);
        
        setTransformLocks();
    }
    
    public void litPhrases() {
    
        if(super.mSubjectH != null && super.mPredicativeH != null) {
            if(super.mPredicativeH.category() == MovableObject.CATEGORY_NOUN) {
                var rhs = (NounText) super.mPredicativeH;
                if(rhs.entityType() == mSubjectH.entityType() || mSubjectH.entityType().isTransformable()) {
                    setLit(true);
                    super.mSubjectH.setLit(true);
                    super.mPredicativeH.setLit(true);
                }
            } else {
                setLit(true);
                super.mSubjectH.setLit(true);
                super.mPredicativeH.setLit(true);
                
                var rhs = (PropertyText) super.mPredicativeH;
                rhs.modifyEntityType(super.mSubjectH.entityType());
            }
        }
        
        if(super.mSubjectV != null && super.mPredicativeV != null) {
            if(super.mPredicativeV.category() == MovableObject.CATEGORY_NOUN) {
                var rhs = (NounText) super.mPredicativeV;
                if(rhs.entityType() == mSubjectV.entityType() || mSubjectV.entityType().isTransformable()) {
                    setLit(true);
                    super.mSubjectV.setLit(true);
                    super.mPredicativeV.setLit(true);
                }
            } else {
                setLit(true);
                super.mSubjectV.setLit(true);
                super.mPredicativeV.setLit(true);
                
                var rhs = (PropertyText) super.mPredicativeV;
                rhs.modifyEntityType(super.mSubjectV.entityType());
            }
        }
    }
    
    public void onTurnEnd(LevelState level, Stage stage) {
        
        executeTransforms(level, stage);
    }
    
    private void executeTransforms(LevelState level, Stage stage) {
    
        if(super.mSubjectH != null && super.mPredicativeH != null) {
            if(super.mPredicativeH.category() == MovableObject.CATEGORY_NOUN) {
                var rhs = (NounText) super.mPredicativeH;
                
                if(rhs.entityType() == mSubjectH.entityType() || mSubjectH.entityType().isTransformable()) {
                    if(rhs.entityType() != mSubjectH.entityType()) {
                        var action = new TransformAction();
                        action.init(stage, super.mSubjectH.entityType(), rhs.entityType());
                        level.addAction(action);
                    }
                }
            }
        }
        
        if(super.mSubjectV != null && super.mPredicativeV != null) {
            if(super.mPredicativeV.category() == MovableObject.CATEGORY_NOUN) {
                var rhs = (NounText) super.mPredicativeV;
                
                if(rhs.entityType() == mSubjectV.entityType() || mSubjectV.entityType().isTransformable()) {
                    if(rhs.entityType() != mSubjectV.entityType()) {
                        var action = new TransformAction();
                        action.init(stage, super.mSubjectV.entityType(), rhs.entityType());
                        level.addAction(action);
                    }
                }
            }
        }
    }
    
    private void setTransformLocks() {
        if(super.mSubjectH != null && super.mPredicativeH != null) {
            if(super.mPredicativeH.category() == MovableObject.CATEGORY_NOUN) {
                var noun = (NounText) super.mPredicativeH;
                if(super.mSubjectH.entityType() == noun.entityType()) {
                    super.mSubjectH.entityType().setTransformable(false);
                }
            }
        }
        
        if(super.mSubjectV != null && super.mPredicativeV != null) {
            if(super.mPredicativeV.category() == MovableObject.CATEGORY_NOUN) {
                var noun = (NounText) super.mPredicativeV;
                if(super.mSubjectV.entityType() == noun.entityType()) {
                    super.mSubjectV.entityType().setTransformable(false);
                }
            }
        }
    }
}
