
import femto.mode.HiRes16Color;
import System.memory.LDRB;

import femto.Image;

public class ScalableImage {
    private int mTranspColor = -1;
    private Image mImage;
    
    public ScalableImage(Image image) {
        mImage = image;
    }
    
    public ScalableImage(Image image, int transpColor) {
        mTranspColor = transpColor;
        mImage = image;
    }
    
    public int width() {
        return mImage.width();
    }
    
    public int height() {
        return mImage.height();
    }
    
    public void draw(HiRes16Color screen, int x, int y, int w_dest, int h_dest) {
        pointer data = mImage.getImageDataForScreen(screen)+2;
        final int w_src = mImage.width();
        final int h_src = mImage.height();
        final float h_scale = ((float)w_src)/w_dest;
        final float v_scale = ((float)h_src)/h_dest;
        
        for (int y_dest=0; y_dest<h_dest; ++y_dest) {
            for (int x_dest=0; x_dest<w_dest; ++x_dest) {
                int x_src = (int)(x_dest*h_scale);
                int y_src = (int)(y_dest*v_scale);
                var color = (ubyte) LDRB(data+(y_src*w_src+x_src)/2);
                color = (x_src&1) == 0 ? color >>= 4 : color &= 0xf;
                
                if(color != mTranspColor) {
                    screen.setPixel(x+x_dest, y+y_dest, color);
                }
            }                
        }  
    }
}
