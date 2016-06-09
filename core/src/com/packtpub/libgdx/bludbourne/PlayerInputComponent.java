/**
 * Created by brett on 6/9/2016.
 */
public class PlayerInputComponent extends InputComponent implements InputProcessor {

    private final static String TAG = PlayerInputComponent.class.getSimpleName();
    private Vector3 _lastMouseCoordinates;

    public PlayerInputComponent(){
        this._lastMouseCoordinates = new Vector3();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void receiveMessage(String message){
        String[] string = message.split(MESSAGE_TOKEN);

        if(string.length == 0 ) return;

        //specifically for messages with 1 object payload
        if(string.length == 2){
            if (string[0].equalsIgnoreCase(
                    MESSAGE.CURRENT_DIRECTION.toString())) {
                _currentDirection = _json.fromJson(Entity.Direction.class, string[1]);
            }
        }
    }

    @Override
    public void dispose(){
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void update(Entity entity, float delta){
        //keyboard input
        if (keys.get(Keys.LEFT)){
            entity.sendMessage(MESSAGE.CURRENT_STATE,
                    _json.toJson(Entity.State.WALKING));
            entity.sendMessage(MESSAGE.CURRENT_DIRECTION,
                    _json.toJson(Entity.Direction.LEFT));

        }else if (keys.get(Keys.RIGHT)){
            entity.sendMessage(MESSAGE.CURRENT_STATE,
                    _json.toJson(Entity.State.WALKING));
            entity.sendMessage(MESSAGE.CURRENT_DIRECTION,
                    _json.toJson(Entity.Direction.RIGHT));
        } else if (keys.get(Keys.UP)){
            entity.sendMessage(MESSAGE.CURRENT_STATE,
                    _json.toJson(Entity.State.WALKING));
            entity.sendMessage(MESSAGE.CURRENT_DIRECTION,
                    _json.toJson(Entity.Direction.UP));
        }else if(keys.get(Keys.DOWN)){
            entity.sendMessage(MESSAGE.CURRENT_STATE,
                    _json.toJson(Entity.State.WALKING));
            entity.sendMessage(MESSAGE.DIRECTION,
                    _json.toJson(Entity.Direction.DOWN));
        }else if (keys.get(Keys.QUIT)){
            Gdx.app.exit();
        }else{
            entity.sendMessage(MESSAGE.CURRENT_STATE,
                    _json.toJson(Entity.State.IDLE));
            if(_currentDirection == null ){
                entity.sendMessage(MESSAGE.CURRENT_DIRECTION,
                        _json.toJson(Entity.Direction.DOWN));
            }
        }

        //mouse input
        if (mouseButtons.get(Mouse.SELECT)){
            entity.sendMessage(MESSAGE.INIT_SELECT_ENTITY,
                    _json.toJson(_lastMouseCoordinates));
            mouseButtons.put(Mouse.SELECT, false);
        }
    }
    //...
}
