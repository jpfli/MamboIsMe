
import femto.sound.Procedural;

import sounds.Push1Sound;
import sounds.Push2Sound;
import sounds.SmashSound;
import sounds.Goal1Sound;
import sounds.Goal2Sound;
import sounds.Goal3Sound;
import sounds.UndoSound;
import sounds.UndoFailSound;

public class SfxManager {
    private static Procedural[] mPushSounds = new Procedural[] {new Push1Sound(1), new Push2Sound(1)};
    private static Procedural mSmashSound = new SmashSound(2);
    private static Procedural[] mGoalSounds = new Procedural[] {new Goal1Sound(0), new Goal2Sound(0), new Goal3Sound(0)};
    private static Procedural mUndoSound = new UndoSound(0);
    private static Procedural mUndoFailSound = new UndoFailSound(0);
    
    private static boolean mPushSoundFired;
    private static boolean mSmashSoundFired;
    private static boolean mGoalSoundFired;
    private static boolean mUndoSoundFired;
    private static boolean mUndoFailSoundFired;
    
    public static void reset() {
        mPushSoundFired = false;
        mSmashSoundFired = false;
        mGoalSoundFired = false;
        mUndoSoundFired = false;
        mUndoFailSoundFired = false;
    }
    
    public static void update() {
        if(mPushSoundFired) {
            mPushSounds[Math.random(0, mPushSounds.length)].play();
            mPushSoundFired = false;
        }
        if(mSmashSoundFired) {
            mSmashSound.play();
            mSmashSoundFired = false;
        }
        if(mGoalSoundFired) {
            mGoalSounds[Math.random(0, mGoalSounds.length)].play();
            mGoalSoundFired = false;
        }
        if(mUndoSoundFired) {
            mUndoSound.play();
            mUndoSoundFired = false;
        }
        if(mUndoFailSoundFired) {
            mUndoFailSound.play();
            mUndoFailSoundFired = false;
        }
    }
    
    public static void firePushSound() {
        mPushSoundFired = true;
    }
    
    public static void fireSmashSound() {
        mSmashSoundFired = true;
    }
    
    public static void fireGoalSound() {
        mGoalSoundFired = true;
    }
    
    public static void fireUndoSound() {
        mUndoSoundFired = true;
    }
    
    public static void fireUndoFailSound() {
        mUndoFailSoundFired = true;
    }
}