/**
 * Created by brett on 6/1/2016.
 */

package com.packtpub.libgdx.bludbourne.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.

OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.packtub.libgdx.bludbourne.Entity;
import com.packtub.libgdx.bludbourne.MapManager;
import com.packtub.libgdx.blubourne.PlayerController;

public class MainGameScreen implements Screen {
    private static final String TAG =
            MainGameScreen.class.getSimpleName();

    private static class VIEWPORT {
        static float viewportWidth;
        static float viewportHeight;
        static float virtualWidth;
        static float virtualHeight;
        static float physicalWidth;
        static float physicalHeight;
        static float aspectRatio;
    }

    private PlayerController _controller;
    private TextureRegion _currentPlayerFrame;
    private Sprite _currentPlayerSprite;

    private OrthogonalTiledMapRenderer _mapRenderer = null;
    private OrthographicCamera _camera = null;
    private static MapManager _mapMgr;

    public MainGameScreen(){
        _mapMgr = new MapManager();
    }

    private static Entity _player;

    @Override
    public void show() {
        //camera setup
        setupViewport(10, 10);

        //get the current size
        _camera = new OrthographicCamera();
        _camera.setToOrtho(false, VIEWPORT.viewportWidth,
                VIEWPORT.viewportHeight);

        _mapRenderer = new OrthogonalTiledMapRenderer
                (_mapMgr.getCurrentMap(), MapManager.UNIT_SCALE);
        _mapRenderer.setView(_camera);

        Gdx.app.debug(TAG, 'UnitScale value is: ' + _mapRenderer.getUnitScale());

        _player = new Entity();
        _player.init(_mapMgr.getPlayerStartUnitScaled().x,
                mapMgr.getPlayerStartUnitScaled().y);

        _currentPlayerSprite = _player.getFrameSprite();

        _controller = new PlayerController(_player);
        Gdx.input.setInputProcesor(_controller);
    }

    @Override
    public void hide(){
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // preferable to lock and center the _camera to the player's position

        _camera.position.set(_currentPlayerSprite.getX(),
                _currentPlayerSprite.getY(), Of);

        _camera.update();

        _player.update(delta);
        _currentPlayerFrame = _player.getFrame();

        updatePortalLayerActivation(_player.boundingBox);

        if (!isCollisionWithMapLayer(_player.boundingBox)) {
            _player.setNextPositionToCurrent();
        }
        _controller.update(delta);

        _mapRenderer.setView(_camera);
        _mapRenderer.render();

        _mapRenderer.getBatch().begin();
        _mapRenderer.getBatch().draw(_currentPlayerFrame, _currentPlayerSprite.getX(), _currentPlayerSprite.getY(), 1, 1);
        _mapRenderer.getBatch().end();
    }

        @Override
                public void resize(int width, int height){
        }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        _player.dispose();
        _controller.dispose();
        Gdx.input.setInputProcessor(null);
        _mapRenderer.dispose();
    }

private void setupViewport(int width, int height){
    // make the viewport a percentage of the total display area
    VIEWPORT.virtualWidth = width;
    VIEWPORT.virtualHeight = height;

    // current dimensions
    VIEWPORT.virtualWidth = VIEWPORT.virtualWidth;
    VIEWPORT.virtualHeight = VIEWPORT.virtualHeight;

    //pixel dimensions of display
    VIEWPORT.physicalWidth = Gdx.graphics.getWidth();
    VIEWPORT.physicalHeight = Gdx.graphics.getHeight();

    //aspect ratio for current viewport
    VIEWPORT.aspectRatio = (VIEWPORT.virtualWidth / VIEWPORT.virtualHeight);

    //update viewport if there could be skewing
    if( VIEWPORT.physicalWidth / VIEWPORT.virtualHeight >= VIEWPORT.aspectRatio) {

        //letter box left and right
        VIEWPORT.viewportWidth = VIEWPORT.viewportHeight *
                (VIEWPORT.physicalWidth / VIEWPORT.physicalHeight);
        VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
    }else{
    //letterbox above and below
        VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
        VIEWPORT.viewportHeight = VIEWPORT.viewportWidth *
                (VIEWPORT.physicalHeight/VIEWPORT.physicalWidth);
    }

    Gdx.app.debug(TAG, "WorldRenderer: virtual: (" +
    VIEWPORT.virtualWidth + "," + VIEWPORT.virtualHeight + ")" );

    Gdx.app.debug(TAG, "WorldRenderer: viewport: (" +
    VIEWPORT.viewportWidth + "," + VIEWPORT.viewportHeight + ")");

    Gdx.app.debug(TAG, "WorldRender: physical: (" +
    VIEWPORT.physicalWidth + "," + VIEWPORT.physicalHeight + ")");
    }
}




