
import femto.Game;
import femto.State;
import femto.Image;

import images.MenuCirclesImage;
import images.MenuTitleImage;
import images.MenuMamboImage;
import sounds.MenuSound;

class MenuState extends State {
    private static final int STATE_MAIN = 0;
    private static final int STATE_HELP = 1;
    private int mState = STATE_MAIN;
    
    private long mInitMillis;
    
    private ScalableImage mBgPattern;
    private ScalableImage mTitleImage;
    private ScalableImage mMamboImage;
    
    private MessageBox mHelpMessage;
    
    public void init() {
        System.gc();
        
        mBgPattern = new ScalableImage(new MenuCirclesImage(), 0);
        mTitleImage = new ScalableImage(new MenuTitleImage(), 0);
        mMamboImage = new ScalableImage(new MenuMamboImage(), 0);
        
        mHelpMessage = new MessageBox(200, 150, 5, 14);
        mHelpMessage.setText(
            "   Game logic follows rules that are made up of movable noun, verb and property blocks."+
            " E.g. rule 'MAMBO is ME' means you control the Mambo character.\n"+
            "   In each level, you must rearrange the blocks to create new rules"+
            " that help you complete the level, which means reaching any object defined as GOAL.\n"+
            "   Rules can only run from left to right or top to bottom,"+
            " but they can overlap so that one block is part of multiple rules.\n"+
            "\n"+
            "Game Controls:\n"+
            " D-PAD is MOVE\n"+
            " (A) is REST\n"+
            " (B) is UNDO\n"+
            " (C) is EXIT");
        
        mInitMillis = System.currentTimeMillis();
    }
    
    public void shutdown() {
        mHelpMessage = null;
        mMamboImage = null;
        mTitleImage = null;
        mBgPattern = null;
    }
    
    public void update() {
        var btn = Buttons.poll();
        
        switch(mState) {
            case STATE_MAIN:
                if(btn == Buttons.BUTTON_A) {
                    if(WorldState.getCompletedLevelsCount() > 0) {
                        Game.changeState(new WorldState());
                    }
                    else {
                        int levelIndex = 0;
                        Game.changeState(new LevelState(levelIndex));
                    }
                    return;
                } else if(btn == Buttons.BUTTON_C) {
                    mState = STATE_HELP;
                }
                // else if(btn == Buttons.BUTTON_B) {
                //     Game.changeState(new EndingState());
                // }
                break;
            case STATE_HELP:
                if(btn == Buttons.BUTTON_A || btn == Buttons.BUTTON_B || btn == Buttons.BUTTON_C) {
                    mState = STATE_MAIN;
                }
                break;
        }
        
        draw();
    }
    
    private void draw() {
        var screen = Main.screen;
        
        // Fill the screen using backgroud pattern
        int w_patt = 2*mBgPattern.width();
        int h_patt = 2*mBgPattern.height();
        for (int y = 0; y < 176; y += h_patt) {
            for (int x = 0; x < 220; x += w_patt) {
                mBgPattern.draw(screen, x, y, w_patt, h_patt);
            }
        }
        
        if(mState == STATE_MAIN) {
            var currentMillis = System.currentTimeMillis();
            
            final int x_start=0, y_start=176, x_end=110, y_end=47;
            final int duration = 600;
            int x, y, w, h;
            if(duration > currentMillis-mInitMillis) {
                w = 2*mTitleImage.width()*(currentMillis-mInitMillis)/duration;
                h = 2*mTitleImage.height()*(currentMillis-mInitMillis)/duration;
                x = x_start-(x_start-x_end)*(currentMillis-mInitMillis)/duration;
                y = y_start-(y_start-y_end)*(currentMillis-mInitMillis)/duration;
            }
            else {
                w = 2*mTitleImage.width();
                h = 2*mTitleImage.height();
                x = x_end;
                y = y_end;
            }
            
            mTitleImage.draw(screen, x-w/2, y-h/2, w, h);
            
            mMamboImage.draw(screen, 44, 76, 2*mMamboImage.width(), 2*mMamboImage.height());
            
            String text = "(A) is PLAY                  (C) is HELP";
            x = 110 - screen.textWidth(text)/2;
            screen.setTextPosition(x+1, 170);
            screen.textColor = 2;
            screen.print(text);
            screen.setTextPosition(x, 170-1);
            screen.textColor = 2;
            screen.print(text);
            screen.setTextPosition(x-1, 170);
            screen.textColor = 2;
            screen.print(text);
            screen.setTextPosition(x, 170-1);
            screen.textColor = 2;
            screen.print(text);
            
            screen.setTextPosition(x, 170);
            screen.textColor = 5;
            screen.print(text);
        } else if(mState == STATE_HELP) {
            mHelpMessage.draw();
        }
        screen.flush();
    }
}
