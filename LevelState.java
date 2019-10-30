
import femto.Game;
import femto.State;

import images.GoalBannerImage;
import images.EndBannerImage;

public class LevelState extends State {
    protected Stage mStage;
    
    private static final int STATE_POLL=0, STATE_TURN=1, STATE_UNDO=2, STATE_GOAL=3, STATE_END=4;
    protected int mState = STATE_TURN;
    
    public static final int TURN_DURATION = 120;
    public static final int END_DURATION = 1800;
    protected long mTurnEndTime;
    
    protected int mPendingButton = Buttons.BUTTON_NONE;

    private int mLevelIndex;
    private boolean mShowInfo = true;
    
    private Action mCurrAction;
    private Action mPendingAction;
    private DeleteAction mDelAction;
    protected ActionStack mActionStack;
    
    private GoalBannerImage mGoalBannerImage;
    private EndBannerImage mEndBannerImage;
    
    public LevelState(int levelIndex) {
        mLevelIndex = levelIndex;
    }
    
    public void init() {
        System.gc();
        
        mStage = new Stage();
        mStage.readLevel(WorldState.levelData(mLevelIndex));
        
        mActionStack = new ActionStack();
        
        SfxManager.reset();
        
        mGoalBannerImage = new GoalBannerImage();
        mEndBannerImage = new EndBannerImage();
        
        mState = STATE_TURN;
        mTurnEndTime = System.currentTimeMillis()+TURN_DURATION;
    }

    public void shutdown() {
        mGoalBannerImage = null;
        mEndBannerImage = null;
        mActionStack = null;
        mStage = null;
    }
    
    // update is called by femto.Game every frame
    public void update() {
        var timeNow = System.currentTimeMillis();
        
        var btn = Buttons.poll();
        if(btn != Buttons.BUTTON_NONE) {
            mPendingButton = btn;
        }
        
        if(timeNow >= mTurnEndTime) {
            if(mState == STATE_TURN) {
                turnStateUpdate(timeNow);
            }
            else if(mState == STATE_UNDO) {
                undoStateUpdate();
            }
            else if(mState == STATE_POLL) {
                pollStateUpdate(timeNow);
            }
            else if(mState == STATE_END) {
                Game.changeState(new EndingState());
            }
            else if(mState == STATE_GOAL) {
                Game.changeState(new WorldState());
            }
        }
        
        SfxManager.update();
        
        draw(timeNow-(mTurnEndTime-TURN_DURATION));
    }
    
    public void addAction(Action action) {
        // Add action at the end of mPendingAction list
        if(mPendingAction != null) {
            var tail = mPendingAction;
            while(tail.next != null) {
                tail = tail.next;
            }
            tail.next = action;
        }
        else {
            mPendingAction = action;
        }
    }
    
    private void draw(long turnTime) {
        var screen = Main.screen;
        screen.clear(15);
        
        // Draw everything else before player
        var iter = mStage.createIterator();
        while (iter.hasMore()) {
            var obj = iter.next();
            if(obj.state() != MovableObject.STATE_DELETED) {
                if(obj.category() == MovableObject.CATEGORY_ENTITY) {
                    var entity = (Entity)obj;
                    if(entity.type().isPlayer()) {
                        continue;
                    }
                }
                obj.draw(screen, turnTime);
            }
        }
        
        // Draw player on top
        boolean alive;
        iter = mStage.createIterator(MovableObject.CATEGORY_ENTITY);
        while (iter.hasMore()) {
            var entity = (Entity)iter.next();
            if(entity.state() != MovableObject.STATE_DELETED && entity.type().isPlayer()) {
                entity.draw(screen, turnTime);
                alive = true;
            }
        }
        
        if(mState == STATE_GOAL) {
            var w = mGoalBannerImage.width();
            var h = mGoalBannerImage.height();
            mGoalBannerImage.draw(screen, (220-w)/2, (176-h)/2);
        }
        else if(mState == STATE_END) {
            var w = mEndBannerImage.width();
            var h = mEndBannerImage.height();
            mEndBannerImage.draw(screen, (220-w)/2, (176-h)/2);
        }
        else {
            if(!alive | mShowInfo) {
                String text = "(A) is REST   (B) is UNDO   (C) is EXIT";
                int x = 110 - screen.textWidth(text)/2;
                
                screen.setTextPosition(x+1, 170);
                screen.textColor = 2;
                screen.print(text);
                screen.setTextPosition(x, 170-1);
                screen.textColor = 2;
                screen.print(text);
                screen.setTextPosition(x-1, 170);
                screen.textColor = 2;
                screen.print(text);
                screen.setTextPosition(x, 170+1);
                screen.textColor = 2;
                screen.print(text);
                
                screen.setTextPosition(x, 170);
                screen.textColor = 5;
                screen.print(text);
            }
        }
        
        screen.flush();
    }
    
    protected void turnStateUpdate(long timeNow) {
        var iter = mStage.createIterator();
        while(iter.hasMore()) {
            var obj = iter.next();
            if(obj.state() != MovableObject.STATE_DELETED) {
                obj.onTurnEnd();
            }
        }
        
        mStage.updateRules();
        
        iter = mStage.createIterator(MovableObject.CATEGORY_VERB);
        while(iter.hasMore()) {
            var verb = (VerbText)iter.next();
            if(verb.state() != MovableObject.STATE_DELETED) {
                verb.onTurnEnd(this, mStage);
            }
        }
        
        // Perform transform actions before checking interactions between objects
        performPendingAction();
        
        checkKilled();
        checkMelted();
        checkRazed();
        
        if(mDelAction != null) {
            addAction(mDelAction);
            mDelAction = null;
            performPendingAction();
            mStage.updateRules();
        }
        
        if(mCurrAction != null) {
            mActionStack.push(mCurrAction);
            mCurrAction = null;
        }
        
        if(isCompleted()) {
            SfxManager.fireGoalSound();
            WorldState.toggleLevelCompleted(mLevelIndex);
            
            mTurnEndTime = timeNow+END_DURATION;
        }
        else {
            mState = STATE_POLL;
        }
    }
    
    private void pollStateUpdate(long timeNow) {
        if(mPendingButton != Buttons.BUTTON_NONE) {
            mShowInfo = false;
            
            if(mPendingButton == Buttons.BUTTON_C) {
                Game.changeState(new WorldState());
            }
            else if(mPendingButton == Buttons.BUTTON_B) {
                if(undoAction()) {
                    SfxManager.fireUndoSound();
                }
                else {
                    SfxManager.fireUndoFailSound();
                }
                
                mState = STATE_UNDO;
            }
            else {
                int stepX = 0;
                int stepY = 0;
                
                if(mPendingButton == Buttons.BUTTON_RIGHT) {
                    stepX = 1;
                }
                else if(mPendingButton == Buttons.BUTTON_UP) {
                    stepY = -1;
                }
                else if(mPendingButton == Buttons.BUTTON_LEFT) {
                    stepX = -1;
                }
                else if(mPendingButton == Buttons.BUTTON_DOWN) {
                    stepY = 1;
                }
                // else BUTTON_A (rest) was pressed
                
                MoveAction move = new MoveAction();
                move.init(stepX, stepY);
                addAction(move);
                performPendingAction();
                
                mState = STATE_TURN;
            }
            mTurnEndTime = timeNow+TURN_DURATION;
        }
        mPendingButton = Buttons.BUTTON_NONE;
    }
    
    private void undoStateUpdate() {
        var iter = mStage.createIterator();
        while(iter.hasMore()) {
            var obj = iter.next();
            if(obj.state() != MovableObject.STATE_DELETED) {
                obj.onTurnEnd();
            }
        }
        
        mStage.updateRules();
        
        iter = mStage.createIterator(MovableObject.CATEGORY_VERB);
        while(iter.hasMore()) {
            var verb = (VerbText)iter.next();
            if(verb.state() != MovableObject.STATE_DELETED) {
                verb.onTurnEnd(this, mStage);
            }
        }
        mPendingAction = null;
        
        mState = STATE_POLL;
    }
    
    private boolean checkKilled() {
        boolean retVal = false;
        
        var iter1 = mStage.createIterator(MovableObject.CATEGORY_ENTITY);
        while(iter1.hasMore()) {
            var playerObj = (Entity)iter1.next();
            if(playerObj.state() != MovableObject.STATE_DELETED && playerObj.type().isPlayer()) {
                var iter2 = mStage.createIterator(playerObj.x(), playerObj.y(), MovableObject.CATEGORY_ENTITY);
                while(iter2.hasMore()) {
                    var deathObj = (Entity)iter2.next();
                    if(deathObj.state() != MovableObject.STATE_DELETED && deathObj.type().isDeath()) {
                        if(playerObj.type().isHover() == deathObj.type().isHover()) {
                            retVal = true;
                            markObjectDeleted(iter1);
                            break;
                        }
                    }
                }
            }
        }
        if(retVal) {
            SfxManager.fireSmashSound();
            return true;
        }
        return false;
    }

    private boolean checkRazed() {
        boolean retVal = false;
        
        var iter1 = mStage.createIterator(MovableObject.CATEGORY_ENTITY);
        while(iter1.hasMore()) {
            boolean razed = false;
            var razeObj = (Entity)iter1.next();
            if(razeObj.state() != MovableObject.STATE_DELETED && razeObj.type().isRaze()) {
                var iter2 = mStage.createIterator(razeObj.x(), razeObj.y());
                while(iter2.hasMore()) {
                    var subj = iter2.next();
                    if(subj.state() != MovableObject.STATE_DELETED) {
                        if(subj.category() == MovableObject.CATEGORY_ENTITY) {
                            var entity = (Entity) subj;
                            if(entity.type() != razeObj.type() && entity.type().isHover() == razeObj.type().isHover()) {
                                razed = true;
                                markObjectDeleted(iter2);
                            }
                        }
                        else if(!razeObj.type().isHover()) {
                            razed = true;
                            markObjectDeleted(iter2);
                        }
                    }
                }
            }
            if(razed) {
                retVal = true;
                markObjectDeleted(iter1);
            }
        }
        if(retVal) {
            SfxManager.fireSmashSound();
            return true;
        }
        return false;
    }

    private boolean checkMelted() {
        boolean retVal = false;
        
        var iter1 = mStage.createIterator(MovableObject.CATEGORY_ENTITY);
        while(iter1.hasMore()) {
            var meltObj = (Entity)iter1.next();
            if(meltObj.state() != MovableObject.STATE_DELETED && meltObj.type().isMelt()) {
                var iter2 = mStage.createIterator(meltObj.x(), meltObj.y(), MovableObject.CATEGORY_ENTITY);
                while(iter2.hasMore()) {
                    var hotObj = (Entity)iter2.next();
                    if(hotObj.state() != MovableObject.STATE_DELETED && hotObj.type().isHot()) {
                        if(meltObj.type().isHover() == hotObj.type().isHover()) {
                            retVal = true;
                            markObjectDeleted(iter1);
                            break;
                        }
                    }
                }
            }
        }
        if(retVal) {
            SfxManager.fireSmashSound();
            return true;
        }
        return false;
    }

    private boolean isCompleted() {
        var iter1 = mStage.createIterator(MovableObject.CATEGORY_ENTITY);
        while(iter1.hasMore()) {
            var me = (Entity)iter1.next();
            if(me.state() != MovableObject.STATE_DELETED && me.type().isPlayer()) {
                var iter2 = mStage.createIterator(me.x(), me.y(), MovableObject.CATEGORY_ENTITY);
                while(iter2.hasMore()) {
                    var ent = (Entity)iter2.next();
                    if(ent.state() != MovableObject.STATE_DELETED && me.type().isHover() == ent.type().isHover()) {
                        if(ent.type().isEnd()) {
                            mState = STATE_END;
                            return true;
                        } else if(ent.type().isGoal()) {
                            mState = STATE_GOAL;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private void performPendingAction() {
        // Perform all actions in the mPendingAction list
        Action parent = null;
        Action action = mPendingAction;
        while(action != null) {
            if(action.perform(mStage)) {
                parent = action;
            }
            else {
                // Action failed to perform or didn't change anything
                if(parent != null) {
                    // Detach the failed action from it's parent
                    parent.next = action.next;
                }
                else {
                    // Remove the failed action from the beginning of mPendingAction list
                    mPendingAction = action.next;
                }
            }
            action = action.next;
        }
        
        // Add successfully performed actions at the end of mCurrAction list
        if(mCurrAction != null) {
            var tail = mCurrAction;
            while(tail.next != null) {
                tail = tail.next;
            }
            tail.next = mPendingAction;
        }
        else {
            mCurrAction = mPendingAction;
        }
        
        mPendingAction = null;
    }
    
    private boolean undoAction() {
        if(mActionStack.length() > 0) {
            Action action = mActionStack.pop();
            do {
                action.revert(mStage);
                action = action.next;
            } while(action != null);
            return true;
        }
        return false;
    }
    
    private void markObjectDeleted(MovableObjectIterator iter) {
        if(mDelAction == null) {
            mDelAction = new DeleteAction();
        }
        mDelAction.markAsDeleted(iter.position());
    }
}
