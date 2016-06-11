package screens;

/**
 * Created by brett on 6/11/2016.
 */
public class PlayerHUD implements Screen, ProfileObserver {

    //...

    private Stage _stage;
    private Viewport _viewport;

    public PlayerHUD(Camera camera, Entity player){
        _viewport = new ScreenViewport(_camera);
        _stage = new Stage(_viewport);

        _statusUI = new StatusUI();
        _inventoryUI = new InventoryUI();

        _stage.addActor(_statusUI);
        _stage.addActor(_inventoryUI);

        //...
    }

    @Override
    public void render(float delta) {
        _stage.act(delta);
        _stage.draw();
    }
}
