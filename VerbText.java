
import femto.Sprite;

abstract class VerbText extends TextBlock {
    public NounText mSubjectH, mSubjectV;
    public TextBlock mPredicativeH, mPredicativeV;
    
    public VerbText(int x, int y) {
        super(CATEGORY_VERB, x, y);
    }
    
    abstract public void update(Stage stage);
    abstract public void litPhrases();
    abstract public void onTurnEnd(LevelState level, Stage stage);
    
    public void buildHorizontalPhrase(Stage stage) {
        mSubjectH = null;
        var subjIter = stage.createIterator(x()-1, y(), MovableObject.CATEGORY_NOUN);
        if(subjIter.hasMore()) {
            var noun = (NounText)subjIter.next();
            if(noun.state() != MovableObject.STATE_DELETED) {
                mSubjectH = noun;
            }
        }
        
        mPredicativeH = null;
        var predIter = stage.createIterator(x()+1, y(), MovableObject.CATEGORY_NOUN | MovableObject.CATEGORY_ATTRIBUTE);
        if(predIter.hasMore()) {
            var textBlock = (TextBlock)predIter.next();
            if(textBlock.state() != MovableObject.STATE_DELETED) {
                mPredicativeH = textBlock;
            }
        }
    }
    
    private void buildVerticalPhrase(Stage stage) {
        mSubjectV = null;
        var subjIter = stage.createIterator(x(), y()-1, MovableObject.CATEGORY_NOUN);
        if(subjIter.hasMore()) {
            var noun = (NounText)subjIter.next();
            if(noun.state() != MovableObject.STATE_DELETED) {
                mSubjectV = noun;
            }
        }
        
        mPredicativeV = null;
        var predIter = stage.createIterator(x(), y()+1, MovableObject.CATEGORY_NOUN | MovableObject.CATEGORY_ATTRIBUTE);
        if(predIter.hasMore()) {
            var textBlock = (TextBlock)predIter.next();
            if(textBlock.state() != MovableObject.STATE_DELETED) {
                mPredicativeV = textBlock;
            }
        }
    }
}
