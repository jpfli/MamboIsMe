
public class MessageBox {
    int mW, mH;
    int mTextColor;
    int mBgColor;
    String mText;
    
    public MessageBox(int w, int h, int textColor, int bgColor) {
        mW = w;
        mH = h;
        mTextColor = textColor;
        mBgColor = bgColor;
    }
    
    public void setText(String text) {
        mText = text;
    }
    
    public void draw()
    {
        var screen = Main.screen;
        
        final int boxX = (screen.width()-mW)/2;
        final int boxY = (screen.height()-mH)/2;
        screen.fillRect(boxX, boxY, mW, mH, mBgColor);
        screen.drawRect(boxX+2, boxY+2, mW-5, mH-5, mTextColor);
        
        if(mText == null) {
            return;
        }
        
        screen.textColor = mTextColor;
        screen.setTextPosition(boxX+4, boxY+4);
        
        int lineNum = 0;
        int cursorIdx = 0;
        int lastSpaceIdx = 0;
        int idx=0;
        while(idx < mText.length()) {
            if(mText[idx] == ' ' || mText[idx] == '\n') {
                lastSpaceIdx = idx;
            }
            
            if(6*(idx-cursorIdx) > mW-7 || mText[idx] == '\n') {
                printTextSubstring(cursorIdx, lastSpaceIdx);
                cursorIdx = lastSpaceIdx+1;
                
                ++lineNum;
                screen.setTextPosition(boxX+4, boxY+4+7*lineNum);
            }
            
            ++idx;
        }
        printTextSubstring(cursorIdx, idx);
    }
    
    private void printTextSubstring(int start, int end) {
        var screen = Main.screen;
        
        int idx = start;
        while(idx < end) {
            screen.putchar(mText[idx]);
            ++idx;
        }
    }
}
