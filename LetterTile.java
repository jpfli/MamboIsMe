
import femto.Sprite;
import femto.mode.HiRes16Color;

import femto.Image;

import images.LetterAImage;
import images.LetterBImage;
import images.LetterEImage;
import images.LetterFImage;
import images.LetterGImage;
import images.LetterIImage;
import images.LetterOImage;
import images.LetterPImage;
import images.LetterRImage;
import images.LetterSImage;
import images.LetterTImage;
import images.LetterUImage;
import images.LetterVImage;
import images.LetterYImage;

class LetterTile extends MovableObject {
    public static final int TYPE_A=1, TYPE_B=2, TYPE_E=3, TYPE_F=4, TYPE_G=5, TYPE_I=6, TYPE_O=7, TYPE_P=8, 
        TYPE_R=9, TYPE_S=10, TYPE_T=11, TYPE_U=12, TYPE_V=13, TYPE_Y=14;
    
    private ubyte mType;
    
    private Image mTileImage;
    private Image mLitTileImage;
    private boolean mIsLit = false;
    
    public LetterTile(int type, int x, int y) {
        super(CATEGORY_LETTERTILE, x, y);
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
            case TYPE_A:
                mTileImage = new LetterAImage();
                break;
            case TYPE_B:
                mTileImage = new LetterBImage();
                break;
            case TYPE_E:
                mTileImage = new LetterEImage();
                break;
            case TYPE_F:
                mTileImage = new LetterFImage();
                break;
            case TYPE_G:
                mTileImage = new LetterGImage();
                break;
            case TYPE_I:
                mTileImage = new LetterIImage();
                break;
            case TYPE_O:
                mTileImage = new LetterOImage();
                break;
            case TYPE_P:
                mTileImage = new LetterPImage();
                break;
            case TYPE_R:
                mTileImage = new LetterRImage();
                break;
            case TYPE_S:
                mTileImage = new LetterSImage();
                break;
            case TYPE_T:
                mTileImage = new LetterTImage();
                break;
            case TYPE_U:
                mTileImage = new LetterUImage();
                break;
            case TYPE_V:
                mTileImage = new LetterVImage();
                break;
            case TYPE_Y:
                mTileImage = new LetterYImage();
                break;
        }
    }
}

