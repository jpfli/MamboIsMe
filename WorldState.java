
import femto.Game;
import femto.State;
import femto.Cookie;
import System.memory.LDRB;

import sprites.CursorSprite;

public class WorldState extends State {

    private static final int MAP_WIDTH=14, MAP_HEIGHT=11;
    private static final int AREA_PASS_PERCENTAGE = 60;

    private static final int STATE_MAP = 0, STATE_HINT = 1;
    private int mState = STATE_MAP;

    public static final pointer[] mLevelData = {
        maps.WelcomeLevel.map(), maps.GoalSettingLevel.map(), maps.ChainReactionLevel.map(), maps.MoatLevel.map(), maps.ExpendableLevel.map(), maps.BoneyardLevel.map(), 
        
        maps.BoggyBrainLevel.map(), maps.SolidWallLevel.map(), maps.WallOfFireLevel.map(), maps.ZombieBlockadeLevel.map(), 
        
        maps.BiteNiteLevel.map(), maps.GarlicPrisonLevel.map(), maps.ImmutableLevel.map(), maps.SnakesLairLevel.map(), maps.HauntedHouseLevel.map(), 
        
        maps.SacrificeLevel.map(), maps.SoulStealerLevel.map(), maps.WalkingDeadLevel.map(), maps.SynchronicityLevel.map(), maps.TrappedLevel.map(), maps.RiotControlLevel.map(), 

        maps.HotFlamesLevel.map(), maps.LittleHelperLevel.map(), maps.GarlicInTheAirLevel.map(), maps.ImpassableSwampLevel.map(), maps.UndeadDuoLevel.map(), 
        
        maps.MinionsLevel.map(), maps.IntermittentLevel.map(), maps.FirefightingLevel.map(), maps.ResurrectionLevel.map(), 
        
        maps.GrandFinaleLevel.map()
    };

    private MapTile[] mLevelTiles;
    private MapTile[] mFloorTiles;

    private CursorSprite mCursorSprite;
    private static int mCurX = 6, mCurY = 5;

    private static var mSolvedLevels = new SolvedLevels();
    // static {
    //     for(int idx=0; idx<mLevelData.length-8; ++idx) {
    //         mSolvedLevels.setLevelSolved(idx, true);
    //     }
    // }

    private String mHintText;
    private MessageBox mHintMessage;

    public WorldState() {

    }

    public static int numLevels() {
        return mLevelData.length;
    }

    public static pointer levelData(int levelIndex) {
        return mLevelData[levelIndex];
    }

    public static void toggleLevelCompleted(int levelIdx) {
        mSolvedLevels.setLevelSolved(levelIdx, true);
    }

    public static int getCompletedLevelsCount() {
        return mSolvedLevels.getSolvedCount();
    }

    int getLevelTileCount(pointer data) {
        int numTiles = 0;
        for (int idx = 0; idx < MAP_HEIGHT * MAP_WIDTH; ++idx) {
            var val = (ubyte) LDRB(data + idx);
            int level = (val & 0xf);
            if (level != 0) {
                ++numTiles;
            }
        }
        return numTiles;
    }

    int getFloorTileCount(pointer data) {
        int numTiles = 0;
        for (int idx = 0; idx < MAP_HEIGHT * MAP_WIDTH; ++idx) {
            var val = (ubyte) LDRB(data + idx);
            int area = (val >> 4);
            int level = (val & 0xf);
            if (area != 0 && level == 0) {
                ++numTiles;
            }
        }
        return numTiles;
    }

    // Returns tile index if found, else -1
    int findLevelTile(pointer data, int areaIndex, int levelNum) {
        for (int idx = 0; idx < MAP_HEIGHT * MAP_WIDTH; ++idx) {
            var val = (ubyte) LDRB(data + idx);
            int area = (val >> 4);
            int level = (val & 0xf);
            if (area == areaIndex && level == levelNum) {
                return idx;
            }
        }
        return -1;
    }

    MapTile addLevelTile(int pos, int levelNum, int x, int y) {
        MapTile tile = null;
        switch (levelNum) {
            case 1:
                tile = new MapTile(MapTile.TYPE_ONE, x, y);
                break;
            case 2:
                tile = new MapTile(MapTile.TYPE_TWO, x, y);
                break;
            case 3:
                tile = new MapTile(MapTile.TYPE_THREE, x, y);
                break;
            case 4:
                tile = new MapTile(MapTile.TYPE_FOUR, x, y);
                break;
            case 5:
                tile = new MapTile(MapTile.TYPE_FIVE, x, y);
                break;
            case 6:
                tile = new MapTile(MapTile.TYPE_SIX, x, y);
                break;
            case 7:
                tile = new MapTile(MapTile.TYPE_SEVEN, x, y);
                break;
            case 8:
                tile = new MapTile(MapTile.TYPE_EIGHT, x, y);
                break;
            case 9:
                tile = new MapTile(MapTile.TYPE_NINE, x, y);
                break;
        }
        if (tile != null) {
            mLevelTiles[pos] = tile;
        }
        return tile;
    }

    public void init() {
        System.gc();

        var data = maps.World.map();

        mLevelTiles = new MapTile[getLevelTileCount(data)];

        int numSolved = 0;
        int levelIdxMark = 0;
        int areaIndex = 0;
        for (int levelIdx = 0; levelIdx < mLevelData.length; ++levelIdx) {
            int levelNum = levelIdx - levelIdxMark + 1;
            int tileIdx = findLevelTile(data, areaIndex, levelNum);
            if (tileIdx < 0) {
                if (levelNum <= 1 || 100 * numSolved / (levelNum - 1) < AREA_PASS_PERCENTAGE) {
                    break;
                }

                tileIdx = findLevelTile(data, areaIndex+1, 1);
                if (tileIdx < 0) {
                    break;
                }
                numSolved = 0;
                levelIdxMark = levelIdx;
                ++areaIndex;
                levelNum = 1;
            }
            int x = tileIdx % MAP_WIDTH;
            int y = tileIdx / MAP_WIDTH;
            addLevelTile(levelIdx, levelNum, x, y);
            if (mSolvedLevels.isLevelSolved(levelIdx)) {
                ++numSolved;
            }
        }

        mFloorTiles = new MapTile[getFloorTileCount(data)];

        int floorIdx = 0;
        for (int tileIdx = 0; tileIdx < MAP_HEIGHT * MAP_WIDTH; ++tileIdx) {
            var val = (ubyte) LDRB(data + tileIdx);
            int area = val >> 4;
            int level = val & 0xf;
            if (area > 0 && area <= areaIndex && level == 0) {
                int x = tileIdx % MAP_WIDTH;
                int y = tileIdx / MAP_WIDTH;
                var tile = new MapTile(MapTile.TYPE_FLOOR, x, y);
                mFloorTiles[floorIdx] = tile;
                ++floorIdx;
            }
        }

        mCursorSprite = new CursorSprite();
        mCursorSprite.idle();

        mHintMessage = new MessageBox(120, 90, 5, 14);
    }

    public void shutdown() {
        mHintMessage = null;
        mCursorSprite = null;
        mFloorTiles = null;
        mLevelTiles = null;
    }

    public void update() {
        var btn = Buttons.poll();

        int selectedLevelIndex = getSelectedLevelIndex();

        if (mState == STATE_MAP) {
            if (btn == Buttons.BUTTON_A) {
                if (selectedLevelIndex >= 0 && selectedLevelIndex < mLevelData.length) {
                    Game.changeState(new LevelState(selectedLevelIndex));
                    return;
                }
            } else if (btn == Buttons.BUTTON_B) {
                if (selectedLevelIndex >= 0 && selectedLevelIndex < mLevelData.length) {
                    mState = STATE_HINT;

                    var data = mLevelData[selectedLevelIndex];
                    // SKip over the level name
                    int idx = MAP_WIDTH * MAP_HEIGHT;
                    var c = (char) LDRB(data + idx);
                    while (c != '\0') {
                        ++idx;
                        c = (char) LDRB(data + idx);
                    }
                    // Read level hint into a string
                    String text = "";
                    ++idx;
                    c = (char) LDRB(data + idx);
                    while (c != '\0') {
                        text = text + c;
                        ++idx;
                        c = (char) LDRB(data + idx);
                    }
                    mHintText = text;
                }
            } else if (btn == Buttons.BUTTON_C) {
                Game.changeState(new MenuState());
                return;
            }

            int dx, dy;
            if (btn == Buttons.BUTTON_UP) {
                dy = -1;
            } else if (btn == Buttons.BUTTON_DOWN) {
                dy = 1;
            } else if (btn == Buttons.BUTTON_LEFT) {
                dx = -1;
            } else if (btn == Buttons.BUTTON_RIGHT) {
                dx = 1;
            }

            if (mCurX + dx >= 0 && mCurX + dx < MAP_WIDTH) {
                mCurX += dx;
            }
            if (mCurY + dy >= 0 && mCurY + dy < MAP_HEIGHT) {
                mCurY += dy;
            }
        } else if (mState == STATE_HINT) {
            if (btn == Buttons.BUTTON_A || btn == Buttons.BUTTON_B || btn == Buttons.BUTTON_C) {
                mState = STATE_MAP;
            }
        }

        draw(selectedLevelIndex);
    }

    private void draw(int selectedLevelIndex) {
        var screen = Main.screen;
        screen.clear(15);

        if (selectedLevelIndex >= 0 && selectedLevelIndex < mLevelData.length) {
            screen.textColor = 5;
            screen.setTextPosition(1, 1);

            var data = mLevelData[selectedLevelIndex];
            int idx = MAP_WIDTH * MAP_HEIGHT;
            var c = (char) LDRB(data + idx);
            while (c != '\0') {
                screen.print("" + c);
                ++idx;
                c = (char) LDRB(data + idx);
            }

        }

        drawTiles();

        if (mState == STATE_MAP) {
            String text = "(A) is PLAY   (B) is HINT   (C) is BACK";
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

            mCursorSprite.setPosition(mCurX * 16 - 2, mCurY * 16);
            mCursorSprite.draw(screen);
        } else if (mState == STATE_HINT) {
            if (Buttons.isPressed(Buttons.BUTTON_DOWN)) {
                mHintMessage.setText(mHintText);
            } else {
                mHintMessage.setText("HINT is HIDDEN\n\nDOWN is UNHIDE");
            }
            mHintMessage.draw();
        }

        screen.flush();
    }
    
    private int getSelectedLevelIndex() {
        for (int idx = 0; idx < mLevelTiles.length; ++idx) {
            var tile = mLevelTiles[idx];
            if (tile != null && mCurX == tile.x() && mCurY == tile.y()) {
                return idx;
            }
        }
        return -1;
    }

    private void drawTiles() {
        for(var tile: mFloorTiles) {
            if(tile != null) {
                tile.draw(Main.screen, 0);
            }
        }

        for (int idx = 0; idx < mLevelTiles.length; ++idx) {
            var tile = mLevelTiles[idx];
            if (tile != null) {
                if (mSolvedLevels.isLevelSolved(idx)) {
                    tile.setLit(false);
                } else {
                    tile.setLit(true);
                }
                tile.draw(Main.screen, 0);
            }
        }
    }
}

private class SolvedLevels extends Cookie {

    private int mSolved; // bit array

    public SolvedLevels() {
        super.begin("MAMBOISM"); // name of the cookie, up to 8 chars
    }

    public boolean isLevelSolved(int index) {
        int bit = 1 << index;
        return (mSolved & bit) != 0;
    }

    public void setLevelSolved(int index, boolean val) {
        int bit = 1 << index;
        if (val) {
            if ((mSolved & bit) == 0) {
                // This is the first time the level is solved
                mSolved |= bit;
                super.saveCookie();
            }
        } else {
            mSolved &= ~bit;
        }
    }

    public int getSolvedCount() {
        int sum = 0;
        for (int idx = 0; 32 > idx; ++idx) {
            if ((mSolved & (1 << idx)) != 0) {
                ++sum;
            }
        }
        return sum;
    }
}
