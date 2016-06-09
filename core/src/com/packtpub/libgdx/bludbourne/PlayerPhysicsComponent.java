/**
 * Created by brett on 6/9/2016.
 */
package com.packtpub.libgdx.bludbourne;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;

public class PlayerPhysicsComponent extends PhysicsComponent{

    private static final String TAG = PlayerPhysicsComponent.class.getSimpleName();

    private Vector3 _mouseSelectCoordinates;
    private boolean _isMouseSelectEnabled = false;
    private Ray _selectionRay;
    private float _selectRayMaximumDistance = 32.0f;

    public PlayerPhysicsComponent(){
        _mouseSelectCoordinates = new Vector3(0,0,0);
        _selectionRay = new Ray(new Vector3(), new Vector());
    }

    @Override
    public void dispose(){
    }

    @Override
    public void receiveMessage(String message){
        String[] string = message.split(Component.MESSAGE_TOKEN);

        if(string.length == 0) return;

        //specifically for messages with 1 object payload
        if(string.length == 2 ){
            if (string[0].equalsIgnoreCase(MESSAGE.INIT_START_POSITION.toString())) {
                _currentEntityPosition = _json.fromJson(Vector2.class, string[1]);
                _nextEntityPosition.set(_currentEntityPosition.x, _currentEntityPosition.y);
            }else if (string[0].equalsIgnoreCase(MESSAGE.CURRENT_STATE.toString())){

                state = _json.fromJson(Entity.State.class, string[1]);
            }else if (string[0].equalsIgnoreCase(MESSAAGE.CURRENT_DIRECTION.toString())){
                _currentDirection = _json.fromJson(Entity.Direction.class, string[1]);
            }else if (string[0].equalsIgnoreCase(MESSAGE.INIT_SELECT_ENTITY.toString())){
                _mouseSelectCoordinates = _json.fromJson(Vector3.class, string[1]);
                _isMouseSelectEnabled = true;
            }
        }
    }

    private void selectMapEntityCandidate(MapManager mapMgr){
        Array<Entity> currentEntities = mapMgr.getCurrentMapEntities();

        //convert screen coordinates to world coordinates, then to unit scale coordinates
        mapMgr.getCamera().unproject(_mouseSelectCoordinates);
        _mouseSelectCoordinates.x /= Map.UNIT_SCALE;
        _mouseSelectCoordinates.y /= Map.UNIT_SCALE;

        for (Entity mapEntity : CurrentEntities ){
            //dont break, reset all entities
            mapEntity.sendMessage(MESSAGE.ENTITY_DESELECTED);
            Rectangle mapEntityBoundingBox = mapEntity.getCurrentBoundingBox();

            if (mapEntity.getCurrentBoundingBox().contains(
                    _mouseSelectCoordinates.x,
                    _mouseSelectCoordinates.y)){
                //check distance
                _selectionRay.set(_boundingBox.x, boundingBox.y,
                        0.0f, mapEntityBoundingBox.y, 0.0f);
                float distance = _slectionRay.origin.dst(_selectionRay.direction);

                if(distance <= _selectRayMaximumDistance ){
                    //we have a valid entity slection picked/selected
                    Gdx.app.debug(TAG, "Selected Entity! " + mapEntity.getEntityConfig().getEntityID());
                    mapEntity.sendMessage(MESSAGE.ENTITY_SELECTED);
                }
            }
        }
    }

    @Override
    public void update(Enity entity, MapManager mapMgr, float delta) {
        //we want the hit box to be at the feet for a better feel
        updateBoundingBoxPosition(_nextEntityPosition);
        updatePortalLayerActivation(mapMgr);

        if(_isMouseSelectEnabled){
            selectMapEntityCandidate(mapMgr);
            _isMouseSelectEnabled = false;
        }

        if( !isCollisionWithMapLayer(entity, mapMgr) &&
                !isCollisionWithMapEntities(entity, mapMgr) &&
                _state == Entity.State.WALKING){
            setNextPositionToCurrent(entity);

            Camera camera = mapMgr.getCamera();
            camera.position.set(_currentEntityPosition.x,
                    _currentEntityPosition.y, 0f);
            camera.update();
        }else{
            updateBoundingBoxPosition(_currentEntityPosition);
        }

        calculateNextPosition(delta);
    }

    private boolean updatePortalLayerActivation(MapManager mapMgr){
        MapLayer mapPortalLayer =  mapMgr.getPortalLayer();

        if( mapPortalLayer == null ){
            Gdx.app.debug(TAG, "Portal Layer doesn't exist!");
            return false;
        }

        Rectangle rectangle = null;

        for( MapObject object: mapPortalLayer.getObjects()){
            if(object instanceof RectangleMapObject) {
                rectangle = ((RectangleMapObject)object).getRectangle();

                if (_boundingBox.overlaps(rectangle) ){
                    String mapName = object.getName();
                    if( mapName == null ) {
                        return false;
                    }

                    mapMgr.setClosestStartPositionFromScaledUnits(_currentEntityPosition);
                    mapMgr.loadMap(MapFactory.MapType.valueOf(mapName));

                    _currentEntityPosition.x = mapMgr.getPlayerStartUnitScaled().x;
                    _currentEntityPosition.y = mapMgr.getPlayerStartUnitScaled().y;
                    _nextEntityPosition.x = mapMgr.getPlayerStartUnitScaled().x;
                    _nextEntityPosition.y = mapMgr.getPlayerStartUnitScaled().y;

                    Gdx.app.debug(TAG, "Portal Activated");
                    return true;
                }
            }
        }
        return false;
    }
}
