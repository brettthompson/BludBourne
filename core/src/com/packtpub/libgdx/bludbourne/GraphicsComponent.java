package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

public abstract class GraphicsComponent implements Component {
    protected TextureRegion _currentFrame = null;
    protected float _frameTime = 0f;
    protected Entity.State _currentState;
    protected Entity.Direction _currentDirection;
    protected Json _json;
    protected Vector2 _currentPosition;

    protected Hashtable<Entity.AnimationType, Animation>
    _animations;
    protected ShapeRenderer _shapeRenderer;


    protected GraphicsComponent(){
        _currentPosition = new Vector2(0,0);
        _currentState = Entity.State.WALKING;
        _currentDirection = Entity.Direction.DOWN;
        _json = new Json();
        _animations = new Hashtable<Entity.AnimationType, Animation>();
        _shapeRenderer = new ShapeRenderer();
    }

    public abstract void update(Entity entity, MapManager mapManager, Batch batch, float delta);

    protected void updateAnimations(float delta){
        //want to avoid overflow
        _frameTime = (_frameTime + delta)%5;

        //look into the appropriate variable when changing position
        switch(_currentDirection){
            case DOWN:
                if (_currentState == Entity.State.WALKING){
                    Animation animation = _animations.get(Entity.AnimationType.WALK_DOWN);
                    if(animation ==  null ) return;
                    _currentFrame = animation.getKeyFrame(_frameTime);
                }else if(_currentState == Entity.State.IDLE){
                    Animation animation = _animations.get(Entity.AnimationType.WALK_DOWN);
                    if( animation == null) return;
                    _currentFrame = animation.getKeyFrames()[0];
                }else if (_currentState == Entity.State.IMMOBILE){
                    Animation animation = _animation.get(Entity.AnimationType.IMMOBILE);
                    if(animation == null ) return;
                    _currentFrame = animation.getKeyFrame(_frameTime);
                }

                break;
            case LEFT:
                if (_currentState == Entity.State.WALKING) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_LEFT);
                    if( animation == null ) return;
                    _currentFrame = animation.getKeyFrame(_frameTime);
                } else if(_currentState == Entity.State.IDLE) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_LEFT);
                    if( animation == null ) return;
                    _currentFrame = animation.getKeyFrames()[0];
                } else if(_currentState == Entity.State.IMMOBILE) {
                    Animation animation = _animations.get(Entity.AnimationType.IMMOBILE);
                    if( animation == null ) return;
                    _currentFrame = animation.getKeyFrame(_frameTime);
                }
                break;
            case UP:
                if (_currentState == Entity.State.WALKING) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_UP);
                    if( animation == null ) return;
                    _currentFrame = animation.getKeyFrame(_frameTime);
                } else if(_currentState == Entity.State.IDLE) {
                    Animation animation = _animations.get(Entity.AnimationType.WALK_UP);
                    if( animation == null ) return;
                    _currentFrame = animation.getKeyFrames()[0];
                } else if(_currentState == Entity.State.IMMOBILE) {
                    Animation animation = _animations.get(Entity.AnimationType.IMMOBILE);
                    if( animation == null ) return;
                    _currentFrame = animation.getKeyFrame(_frameTime);
                }
                break;
            Case RIGHT:
            if (_currentState == Entity.State.WALKING) {
                Animation animation = _animations.get(Entity.AnimationType.WALK_RIGHT);
                if( animation == null ) return;
                _currentFrame = animation.getKeyFrame(_frameTime);
            } else if(_currentState == Entity.State.IDLE) {
                Animation animation = _animations.get(Entity.AnimationType.WALK_RIGHT);
                if( animation == null ) return;
                _currentFrame = animation.getKeyFrames()[0];
            } else if(_currentState == Entity.State.IMMOBILE) {
                Animation animation = _animations.get(Entity.AnimationType.IMMOBILE);
                if( animation == null ) return;
                _currentFrame = animation.getKeyFrame(_frameTime);
            }
                break;
            default:
                break;
        }
    }

    //specific to two frame animations where each frame is stored in a seperate texture
    protected Animation loadAnimation(String firstTexture, String secondTexture, Array<GridPoint2> points, float frameDuration){
        Utility.loadTextureAsset(firstTexture);
        Texture texturel = Utility.getTextureAsset(firstTexture);

        Utility.loadTextureAsset(secondTexture);

        Texture texture2 = Utility.getTextureAsset(secondTexture);

        TextureRegion[][] textureFrames = TextureRegion.split(
                texturel,
                Entity.FRAME_WIDTH,
                Entity.FRAME_HEIGHT);
        TextureRegion[][] texture2Frames = TextureRegion.split(
                texture2,
                Enttity.FRAME_WIDTH,
                Entity.FRAME_HEIGHT);

        Array<TextureRegion> animationKeyFrames = new
                Array<TextureRegion>(2);

        GridPoint2 point = points.first();

        animation.KeyFrames.add(texture1Frames[point.x][point.y]);
        animation.KeyFrames.add(texture2Frames[point.x][point.y]);

        return new Animation(frameDuration, animationKeyFrames, Animation.PlayMode.LOOP);
    }

    protected Animation loadAnimation(String textureName, Array <GridPoint2> points, float frameDuration){
        Utility.loadTextureAsset(textureName);
        Texture texture = Utility.getTextureAsset(textureName);

        TextureRegion[][] textureFrames = TextureRegion.split(
                texture,
                Entity.FRAME_WIDTH,
                Entity.FRAME_HEIGHT);

        Array<TextureRegion> animationKeyFrames = new
                Array<TextureRegion>(points.size);

        for(GridPoint2 point : points){
            animationKeyFrames.add(textureFrames[point.x][point.y]);
        }

        return new Animation(frameDuration, animationKeyFrames, Animation.PlayMode.LOOP);
    }
}
