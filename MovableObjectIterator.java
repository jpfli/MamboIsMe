
import MovableObject;

class MovableObjectIterator {
    private MovableObject[] mobjList;
    private boolean anyLocation;
    private int x, y;
    private int categoryMask = -1;
    private int idx = 0;
    private int nextIdx = 0;
    
    public MovableObjectIterator(MovableObject[] mobjList) {
        this.mobjList = mobjList;
        this.anyLocation = true;
        
        seekNext();
    }
    
    public MovableObjectIterator(MovableObject[] mobjList, int categoryMask) {
        this.mobjList = mobjList;
        this.anyLocation = true;
        this.categoryMask = categoryMask;
        
        seekNext();
    }
    
    public MovableObjectIterator(MovableObject[] mobjList, int x, int y) {
        this.mobjList = mobjList;
        this.anyLocation = false;
        this.x = x;
        this.y = y;
        
        seekNext();
    }
    
    public MovableObjectIterator(MovableObject[] mobjList, int x, int y, int categoryMask) {
        this.mobjList = mobjList;
        this.anyLocation = false;
        this.x = x;
        this.y = y;
        this.categoryMask = categoryMask;
        
        seekNext();
    }
    
    public MovableObject next() {
        this.idx = this.nextIdx;
        if(this.idx < mobjList.length) {
            MovableObject mobj = this.mobjList[this.idx];
            ++this.nextIdx;
            seekNext();
            return mobj;
        }
        return null;
    }
    
    public boolean hasMore() {
        if(this.nextIdx < mobjList.length) {
            if(this.mobjList[this.nextIdx] == null) {
                return seekNext();
            }
            return true;
        }
        return false;
    }
    
    public int position() {
        return this.idx;
    }
    
    private boolean seekNext() {
        while(this.nextIdx < mobjList.length) {
            MovableObject obj = this.mobjList[this.nextIdx];
            if(obj != null) {
                if((obj.category() & this.categoryMask) != 0) {
                    if(this.anyLocation || (this.x == obj.x() && this.y == obj.y())) {
                        return true;
                    }
                }
            }
            ++this.nextIdx;
        }
        return false;
    }
}
