
import femto.input.Button;

public class Buttons {
    public static final int BUTTON_NONE = 0, BUTTON_RIGHT = 1, BUTTON_UP = 2, BUTTON_LEFT = 3, BUTTON_DOWN = 4,
        BUTTON_A = 5, BUTTON_B = 6, BUTTON_C = 7;
    
    private static final int REPEAT_DELAY = 300;   // Delay before repeat starts
    private static final int REPEAT_RATE = 180;    // Rate at which buttons are repeated
    
    private static int mHeldButton;
    private static long mTimeNextButtonFired;
    
    // Returns the button that was just pressed. Holding a button down generates repeated button presses.
    // Should be called once every frame. 
    public static int poll() {
        var timeNow = System.currentTimeMillis();
        
        if (Button.Up.justPressed()) {
            mHeldButton = BUTTON_UP;
        }
        else if (Button.Left.justPressed()) {
            mHeldButton = BUTTON_LEFT;
        }
        else if (Button.Down.justPressed()) {
            mHeldButton = BUTTON_DOWN;
        }
        else if (Button.Right.justPressed()) {
            mHeldButton = BUTTON_RIGHT;
        }
        else if (Button.A.justPressed()) {
            mHeldButton = BUTTON_A;
        }
        else if (Button.B.justPressed()) {
            mHeldButton = BUTTON_B;
        }
        else if (Button.C.justPressed()) {
            mHeldButton = BUTTON_C;
        }
        else {
            // Check if the held button has been released
            switch(mHeldButton) {
                case BUTTON_UP:
                    if(!Button.Up.isPressed()) {
                        mHeldButton = BUTTON_NONE;
                    }
                    break;
                case BUTTON_LEFT:
                    if(!Button.Left.isPressed()) {
                        mHeldButton = BUTTON_NONE;
                    }
                    break;
                case BUTTON_DOWN:
                    if(!Button.Down.isPressed()) {
                        mHeldButton = BUTTON_NONE;
                    }
                    break;
                case BUTTON_RIGHT:
                    if(!Button.Right.isPressed()) {
                        mHeldButton = BUTTON_NONE;
                    }
                    break;
                case BUTTON_A:
                    if(!Button.A.isPressed()) {
                        mHeldButton = BUTTON_NONE;
                    }
                    break;
                case BUTTON_B:
                    if(!Button.B.isPressed()) {
                        mHeldButton = BUTTON_NONE;
                    }
                    break;
                case BUTTON_C:
                    if(!Button.C.isPressed()) {
                        mHeldButton = BUTTON_NONE;
                    }
                    break;
            }
            
            if(mHeldButton == BUTTON_NONE) {
                // No button is being held down
                return BUTTON_NONE;
            }
            
            if(timeNow-mTimeNextButtonFired >= 0) {
                //Time has come to fire new repeated button press
                mTimeNextButtonFired = timeNow+REPEAT_RATE;
                return mHeldButton;
            }
            return BUTTON_NONE;
        }
        
        // Button was just pressed so initialize repeat timer
        mTimeNextButtonFired = timeNow+REPEAT_DELAY;
        return mHeldButton;
    }
    
    // Returns true if the given button is being held down
    public static boolean isPressed(int button) {
        switch(button) {
            case BUTTON_RIGHT:
                return Button.Right.isPressed();
            case BUTTON_UP:
                return Button.Up.isPressed();
            case BUTTON_LEFT:
                return Button.Left.isPressed();
            case BUTTON_DOWN:
                return Button.Down.isPressed();
            case BUTTON_A:
                return Button.A.isPressed();
            case BUTTON_B:
                return Button.B.isPressed();
            case BUTTON_C:
                return Button.C.isPressed();
        }
        return false;
    }
    
    // Force stop button repeat
    public static void reset() {
        mHeldButton = BUTTON_NONE;
    }
}
