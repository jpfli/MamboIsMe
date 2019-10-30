
import femto.Game;
import femto.mode.HiRes16Color;
import femto.sound.Mixer;

import femto.palette.Na16;
import femto.font.TIC80;

public class Main {
    public static final HiRes16Color screen = new HiRes16Color(Na16.palette(), TIC80.font());
    
    // Start the game using MenuState as the initial state
    public static void main(String[] args) {
        Mixer.init();
        Game.run(TIC80.font(), new MenuState());
    }
}
