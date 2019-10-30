
public class MoveAction extends Action {
    
    // Two bits for each (max 160) MovableObject indicating it's original direction
    int mPrevDirs1, mPrevDirs2, mPrevDirs3, mPrevDirs4, mPrevDirs5,
        mPrevDirs6, mPrevDirs7, mPrevDirs8, mPrevDirs9, mPrevDirs10;
    
    // Entity's stepX and stepY combined in one byte
    byte mPlayerStep;
    // One bit for each (max 160) MovableObject indicating whether it's moving (1) or not (0)
    int mIsMoved1, mIsMoved2, mIsMoved3, mIsMoved4, mIsMoved5;
    // Two bits for each (max 160) MovableObject indicating it's move direction
    int mMoveDirs1, mMoveDirs2, mMoveDirs3, mMoveDirs4, mMoveDirs5,
        mMoveDirs6, mMoveDirs7, mMoveDirs8, mMoveDirs9, mMoveDirs10;
    
    public MoveAction() {
        
    }
    
    public void init(int playerStepX, int playerStepY) {
        mPlayerStep = (playerStepX&0xf) | (playerStepY<<4);
        mIsMoved1 = 0;
        mIsMoved2 = 0;
        mIsMoved3 = 0;
        mIsMoved4 = 0;
        mIsMoved5 = 0;
    }
    
    public boolean perform(Stage stage) {
        savePreviousDirections(stage);
        
        var retVal = moveWalkingEntities(stage);
        retVal |= movePlayerEntities(stage);
        
        return retVal;
    }
    
    public void revert(Stage stage) {
        var iter = stage.createIterator();
        while(iter.hasMore()) {
            var obj = iter.next();
            var idx = iter.position();
            
            boolean needRevert = false;
            int moveMask = 1<<(idx%32);
            int moveSlot = idx/32;
            switch(moveSlot) {
                case 0:
                    needRevert = (mIsMoved1&moveMask) != 0;
                    break;
                case 1:
                    needRevert = (mIsMoved2&moveMask) != 0;
                    break;
                case 2:
                    needRevert = (mIsMoved3&moveMask) != 0;
                    break;
                case 3:
                    needRevert = (mIsMoved4&moveMask) != 0;
                    break;
                case 4:
                    needRevert = (mIsMoved5&moveMask) != 0;
                    break;
            }
            
            if(needRevert) {
                int dir;
                int dirSlot = idx/16;
                int dirShift = (2*idx)%32;
                
                switch(dirSlot) {
                    case 0:
                        dir = (mMoveDirs1>>dirShift)&3;
                        break;
                    case 1:
                        dir = (mMoveDirs2>>dirShift)&3;
                        break;
                    case 2:
                        dir = (mMoveDirs3>>dirShift)&3;
                        break;
                    case 3:
                        dir = (mMoveDirs4>>dirShift)&3;
                        break;
                    case 4:
                        dir = (mMoveDirs5>>dirShift)&3;
                        break;
                    case 5:
                        dir = (mMoveDirs6>>dirShift)&3;
                        break;
                    case 6:
                        dir = (mMoveDirs7>>dirShift)&3;
                        break;
                    case 7:
                        dir = (mMoveDirs8>>dirShift)&3;
                        break;
                    case 8:
                        dir = (mMoveDirs9>>dirShift)&3;
                        break;
                    case 9:
                        dir = (mMoveDirs10>>dirShift)&3;
                        break;
                }
                
                int stepX, stepY;
                if(dir == MovableObject.DIR_RIGHT) {
                    stepX = 1;
                } else if(dir == MovableObject.DIR_UP) {
                    stepY = -1;
                } else if(dir == MovableObject.DIR_LEFT) {
                    stepX = -1;
                } else if(dir == MovableObject.DIR_DOWN) {
                    stepY = 1;
                }
                
                obj.jump(-stepX, -stepY);
                
                switch(dirSlot) {
                    case 0:
                        dir = (mPrevDirs1>>dirShift)&3;
                        break;
                    case 1:
                        dir = (mPrevDirs2>>dirShift)&3;
                        break;
                    case 2:
                        dir = (mPrevDirs3>>dirShift)&3;
                        break;
                    case 3:
                        dir = (mPrevDirs4>>dirShift)&3;
                        break;
                    case 4:
                        dir = (mPrevDirs5>>dirShift)&3;
                        break;
                    case 5:
                        dir = (mPrevDirs6>>dirShift)&3;
                        break;
                    case 6:
                        dir = (mPrevDirs7>>dirShift)&3;
                        break;
                    case 7:
                        dir = (mPrevDirs8>>dirShift)&3;
                        break;
                    case 8:
                        dir = (mPrevDirs9>>dirShift)&3;
                        break;
                    case 9:
                        dir = (mPrevDirs10>>dirShift)&3;
                        break;
                }
                
                obj.setDirection(dir);
            }
        }
    }
    
    private boolean movePlayerEntities(Stage stage) {
        if(mPlayerStep == 0) {
            return false;
        }
        
        int stepX = (mPlayerStep<<28)>>28;
        int stepY = mPlayerStep>>4;
        
        boolean retVal = false;
        
        var iter = stage.createIterator(MovableObject.CATEGORY_ENTITY);
        while(iter.hasMore()) {
            var entity = (Entity)iter.next();
            if(entity.state() != MovableObject.STATE_DELETED && entity.type().isPlayer()) {
                if(moveEntity(stage, entity, iter.position(), stepX, stepY)) {
                    retVal = true;
                } else {
                    entity.bump(stepX, stepY);
                }
            }
        }
        return retVal;
    }
    
    private boolean moveWalkingEntities(Stage stage) {
        boolean retVal = false;
        
        var iter = stage.createIterator(MovableObject.CATEGORY_ENTITY);
        while(iter.hasMore()) {
            var entity = (Entity)iter.next();
            if(entity.state() != MovableObject.STATE_DELETED && entity.type().isWalk() && !entity.type().isPlayer()) {
                int stepX = 0;
                int stepY = 0;
                var dir = entity.direction();
                if(dir == MovableObject.DIR_RIGHT) {
                    stepX = 1;
                } else if(dir == MovableObject.DIR_UP) {
                    stepY = -1;
                } else if(dir == MovableObject.DIR_LEFT) {
                    stepX = -1;
                } else if(dir == MovableObject.DIR_DOWN) {
                    stepY = 1;
                }
                
                var idx = iter.position();
                if(moveEntity(stage, entity, idx, stepX, stepY) || moveEntity(stage, entity, idx, -stepX, -stepY)) {
                    retVal = true;
                }
            }
        }
        
        return retVal;
    }
    
    private boolean moveEntity(Stage stage, Entity entity, int idx, int stepX, int stepY) {
        int numSteps = isBlocked(stage, entity, stepX, stepY);
        if(numSteps >= 0) {
            if(numSteps > 0) {
                pushBlocks(stage, entity, stepX, stepY, numSteps);
                SfxManager.firePushSound();
            }
        
            markAsMoved(idx, stepX, stepY);
            
            entity.walk(stepX, stepY);
            return true;
        }
        return false;
    }
    
    private int isBlocked(Stage stage, Entity entity, int stepX, int stepY) {
        int destX = entity.x();
        int destY = entity.y();
        
        int numSteps = -1;
        boolean repeat;
        do {
            repeat = false;
            
            destX += stepX;
            destY += stepY;
            if(destX < 0 || destX >= stage.MAP_WIDTH) {
                return -1;
            }
            if(destY < 0 || destY >= stage.MAP_HEIGHT) {
                return -1;
            }
            
            var iter = stage.createIterator(destX, destY);
            while(iter.hasMore()) {
                var obj = iter.next();
                if(obj.state() != MovableObject.STATE_DELETED) {
                    if(obj.category() == MovableObject.CATEGORY_ENTITY) {
                        var other = (Entity)obj;
                        if(other.type() != entity.type()) {
                            if(other.type().isPush()) {
                                repeat = true;
                            } else if(other.type().isSolid()) {
                                return -1;
                            }
                        }
                    } else {
                        repeat = true;
                    }
                }
            }
            ++numSteps;
        } while(repeat);
        
        return numSteps;
    }
    
    private void pushBlocks(Stage stage, Entity entity, int stepX, int stepY, int numSteps) {
        int destX = entity.x();
        int destY = entity.y();
        
        while(numSteps > 0) {
            destX += stepX;
            destY += stepY;
            
            var iter = stage.createIterator(destX, destY);
            while(iter.hasMore()) {
                var obj = iter.next();
                if(obj.state() != MovableObject.STATE_DELETED) {
                    if(obj.category() == MovableObject.CATEGORY_ENTITY) {
                        var entity = (Entity)obj;
                        if(!entity.type().isPush()) {
                            continue;
                        }
                    }
                    var idx = iter.position();
                    markAsMoved(idx, stepX, stepY);
                    obj.walk(stepX, stepY);
                }
            }
            --numSteps;
        }
    }

    private void savePreviousDirections(Stage stage) {
        var iter = stage.createIterator();
        while(iter.hasMore()) {
            var obj = iter.next();
            
            var dir = obj.direction();
            var idx = iter.position();
            int bits = dir<<((2*idx)%32);
            int slot = idx/16;
            switch(slot) {
                case 0:
                    mPrevDirs1 |= bits;
                    break;
                case 1:
                    mPrevDirs2 |= bits;
                    break;
                case 2:
                    mPrevDirs3 |= bits;
                    break;
                case 3:
                    mPrevDirs4 |= bits;
                    break;
                case 4:
                    mPrevDirs5 |= bits;
                    break;
                case 5:
                    mPrevDirs6 |= bits;
                    break;
                case 6:
                    mPrevDirs7 |= bits;
                    break;
                case 7:
                    mPrevDirs8 |= bits;
                    break;
                case 8:
                    mPrevDirs9 |= bits;
                    break;
                case 9:
                    mPrevDirs10 |= bits;
                    break;
                default:
                    break;
            }
        }
    }
    
    private void markAsMoved(int idx, int stepX, int stepY) {
        int bit = 1<<(idx%32);
        int slot = idx/32;
        switch(slot) {
            case 0:
                mIsMoved1 |= bit;
                break;
            case 1:
                mIsMoved2 |= bit;
                break;
            case 2:
                mIsMoved3 |= bit;
                break;
            case 3:
                mIsMoved4 |= bit;
                break;
            case 4:
                mIsMoved5 |= bit;
                break;
            default:
                break;
        }
        
        int objDir;
        if(stepX > 0) {
            objDir = MovableObject.DIR_RIGHT;
        } else if(stepY < 0) {
            objDir = MovableObject.DIR_UP;
        } else if(stepX < 0) {
            objDir = MovableObject.DIR_LEFT;
        } else if(stepY > 0) {
            objDir = MovableObject.DIR_DOWN;
        }
        
        int clearBits = 3<<(2*(idx%16));
        int dirBits = objDir<<(2*(idx%16));
        slot = idx/16;
        switch(slot) {
            case 0:
                mMoveDirs1 &= ~clearBits;
                mMoveDirs1 |= dirBits;
                break;
            case 1:
                mMoveDirs2 &= ~clearBits;
                mMoveDirs2 |= dirBits;
                break;
            case 2:
                mMoveDirs3 &= ~clearBits;
                mMoveDirs3 |= dirBits;
                break;
            case 3:
                mMoveDirs4 &= ~clearBits;
                mMoveDirs4 |= dirBits;
                break;
            case 4:
                mMoveDirs5 &= ~clearBits;
                mMoveDirs5 |= dirBits;
                break;
            case 5:
                mMoveDirs6 &= ~clearBits;
                mMoveDirs6 |= dirBits;
                break;
            case 6:
                mMoveDirs7 &= ~clearBits;
                mMoveDirs7 |= dirBits;
                break;
            case 7:
                mMoveDirs8 &= ~clearBits;
                mMoveDirs8 |= dirBits;
                break;
            case 8:
                mMoveDirs9 &= ~clearBits;
                mMoveDirs9 |= dirBits;
                break;
            case 9:
                mMoveDirs10 &= ~clearBits;
                mMoveDirs10 |= dirBits;
                break;
        }
    }
}
