
import femto.Game;
import femto.State;
import femto.input.Button;

class EndingState extends LevelState {
    private int mNumCompleted;
    
    public EndingState() {
        super(-1);  // Initialize parent with invalid level number
    }
    
    public void init() {
        System.gc();
        
        super.mStage = new Stage();
        super.mStage.readLevel(maps.EndingLevel.map(), 6);
        
        super.mActionStack = new ActionStack();
        
        SfxManager.reset();
        
        super.mState = STATE_TURN;
        super.mTurnEndTime = System.currentTimeMillis()+TURN_DURATION;
        
        mNumCompleted = WorldState.getCompletedLevelsCount();
        if((100 * mNumCompleted) / WorldState.numLevels() < 75) {
            super.mStage.addObject(new LetterTile(LetterTile.TYPE_F, 4, 5));
            super.mStage.addObject(new LetterTile(LetterTile.TYPE_A, 7, 5));
            super.mStage.addObject(new LetterTile(LetterTile.TYPE_I, 8, 8));
            super.mStage.addObject(new LetterTile(LetterTile.TYPE_R, 5, 3));
        }
        else if((100 * mNumCompleted) / WorldState.numLevels() < 85) {
            super.mStage.addObject(new LetterTile(LetterTile.TYPE_G, 4, 5));
            super.mStage.addObject(new LetterTile(LetterTile.TYPE_R, 7, 5));
            super.mStage.addObject(new LetterTile(LetterTile.TYPE_E, 8, 8));
            super.mStage.addObject(new LetterTile(LetterTile.TYPE_A, 5, 3));
            super.mStage.addObject(new LetterTile(LetterTile.TYPE_T, 5, 9));
        }
        else if((100 * mNumCompleted) / WorldState.numLevels() < 95) {
            super.mStage.addObject(new LetterTile(LetterTile.TYPE_G, 4, 5));
            super.mStage.addObject(new LetterTile(LetterTile.TYPE_R, 7, 5));
            super.mStage.addObject(new LetterTile(LetterTile.TYPE_O, 8, 8));
            super.mStage.addObject(new LetterTile(LetterTile.TYPE_O, 5, 3));
            super.mStage.addObject(new LetterTile(LetterTile.TYPE_V, 5, 9));
            super.mStage.addObject(new LetterTile(LetterTile.TYPE_Y, 2, 3));
        }
        else {
            super.mStage.addObject(new LetterTile(LetterTile.TYPE_S, 4, 5));
            super.mStage.addObject(new LetterTile(LetterTile.TYPE_U, 7, 5));
            super.mStage.addObject(new LetterTile(LetterTile.TYPE_P, 8, 8));
            super.mStage.addObject(new LetterTile(LetterTile.TYPE_E, 5, 3));
            super.mStage.addObject(new LetterTile(LetterTile.TYPE_R, 5, 9));
            super.mStage.addObject(new LetterTile(LetterTile.TYPE_B, 2, 3));
        }
    }
    
    public void shutdown() {
        super.mActionStack = null;
        super.mStage = null;
    }
    
    public void update() {
        var timeNow = System.currentTimeMillis();
        
        var btn = Buttons.poll();
        if(btn != Buttons.BUTTON_NONE) {
            super.mPendingButton = btn;
        }
        
        if(timeNow >= super.mTurnEndTime) {
            if(super.mState == super.STATE_TURN) {
                super.turnStateUpdate(timeNow);
            }
            else if(mState == super.STATE_POLL) {
                if(super.mPendingButton == Buttons.BUTTON_A || super.mPendingButton == Buttons.BUTTON_B || super.mPendingButton == Buttons.BUTTON_C) {
                    Game.changeState(new MenuState());
                    return;
                }
                else {
                    MoveAction move = new MoveAction();
                    move.init(0, 0);
                    super.addAction(move);
                    super.performPendingAction();
                    
                    super.mState = STATE_TURN;
                }
                super.mTurnEndTime = timeNow+super.TURN_DURATION;
                
                super.mPendingButton = Buttons.BUTTON_NONE;
            }
        }
        
        SfxManager.update();
        
        draw(timeNow-(super.mTurnEndTime-super.TURN_DURATION));
    }
    
    private void draw(long turnTime) {
        var screen = Main.screen;
        screen.clear(15);
        
        // Draw everything else before player
        var iter = mStage.createIterator();
        while (iter.hasMore()) {
            var obj = iter.next();
            if(obj.state() != MovableObject.STATE_DELETED) {
                obj.draw(screen, turnTime);
            }
        }
        
        screen.setTextColor(5);
        
        drawText("Congratulations!", 110, 14, true);
        drawText("You completed "+mNumCompleted+" of "+WorldState.numLevels()+" levels", 110, 28, true);
        drawText("Rating:", 63, 68, false);
        
        screen.flush();
    }
    
    private void drawText(String text, int x, int y, boolean center) {
        var screen = Main.screen;
        if(center) {
            x = x - screen.textWidth(text)/2;
        }
        
        screen.setTextColor(2);
        screen.setTextPosition(x+1, y);
        screen.println(text);
        screen.setTextPosition(x, y-1);
        screen.println(text);
        screen.setTextPosition(x-1, y);
        screen.println(text);
        screen.setTextPosition(x, y+1);
        screen.println(text);
        
        screen.setTextColor(5);
        screen.setTextPosition(x, y);
        screen.println(text);
    }
}
