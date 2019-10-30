
import femto.Sprite;
import femto.mode.HiRes16Color;

import femto.Image;

import images.OneTileImage;
import images.OneLitTileImage;
import images.TwoTileImage;
import images.TwoLitTileImage;
import images.ThreeTileImage;
import images.ThreeLitTileImage;
import images.FourTileImage;
import images.FourLitTileImage;
import images.FiveTileImage;
import images.FiveLitTileImage;
import images.SixTileImage;
import images.SixLitTileImage;
import images.SevenTileImage;
import images.SevenLitTileImage;
import images.EightTileImage;
import images.EightLitTileImage;
import images.NineTileImage;
import images.NineLitTileImage;
import images.FloorTileImage;

class MapTile extends MovableObject {
    public static final int TYPE_ZERO = 0;
    public static final int TYPE_ONE = 1;
    public static final int TYPE_TWO = 2;
    public static final int TYPE_THREE = 3;
    public static final int TYPE_FOUR = 4;
    public static final int TYPE_FIVE = 5;
    public static final int TYPE_SIX = 6;
    public static final int TYPE_SEVEN = 7;
    public static final int TYPE_EIGHT = 8;
    public static final int TYPE_NINE = 9;
    public static final int TYPE_FLOOR = 10;
    
    private ubyte mType;
    
    private Image mTileImage;
    private Image mLitTileImage;
    private boolean mIsLit = false;
    
    public MapTile(int type, int x, int y) {
        super(CATEGORY_MAPTILE, x, y);
        mType = type;
        createTileImages();
    }
    
    public void draw(HiRes16Color screen, long turnTime) {
        var image = mIsLit ? mLitTileImage : mTileImage;
        super.drawImage(screen, image, turnTime);
    }
    
    public void setLit(boolean lit) {
        mIsLit = lit;
    }
    
    private void createTileImages() {
        switch(mType) {
            case TYPE_ONE:
                mTileImage = new OneTileImage();
                mLitTileImage = new OneLitTileImage();
                break;
            case TYPE_TWO:
                mTileImage = new TwoTileImage();
                mLitTileImage = new TwoLitTileImage();
                break;
            case TYPE_THREE:
                mTileImage = new ThreeTileImage();
                mLitTileImage = new ThreeLitTileImage();
                break;
            case TYPE_FOUR:
                mTileImage = new FourTileImage();
                mLitTileImage = new FourLitTileImage();
                break;
            case TYPE_FIVE:
                mTileImage = new FiveTileImage();
                mLitTileImage = new FiveLitTileImage();
                break;
            case TYPE_SIX:
                mTileImage = new SixTileImage();
                mLitTileImage = new SixLitTileImage();
                break;
            case TYPE_SEVEN:
                mTileImage = new SevenTileImage();
                mLitTileImage = new SevenLitTileImage();
                break;
            case TYPE_EIGHT:
                mTileImage = new EightTileImage();
                mLitTileImage = new EightLitTileImage();
                break;
            case TYPE_NINE:
                mTileImage = new NineTileImage();
                mLitTileImage = new NineLitTileImage();
                break;
            case TYPE_FLOOR:
                mTileImage = new FloorTileImage();
                mLitTileImage = mTileImage;
                break;
        }
    }
}
