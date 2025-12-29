package pepse.world.avatar;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;


public class Avatar extends GameObject {

    private static final Vector2 AVATAR_SIZE = new Vector2(50, 50);
    private static final float OFFSET_GROUND_START = 20f;
    private static final float GRAVITY = 1000f;
    private static final String AVATAR_TAG = "avatar";
    private static final float WALK_SPEED = 300f;




    private final Vector2 topleftCorner;
    private final UserInputListener inputListener;
    private final ImageReader imageReader;


    public Avatar(Vector2 topLeftCorner,
                  UserInputListener inputListener,
                  ImageReader imageReader){
        super(new Vector2(topLeftCorner.x(),
                topLeftCorner.y() - AVATAR_SIZE.y() - OFFSET_GROUND_START) , AVATAR_SIZE, imageReader.readImage("assets/idle_0.png", true));
        this.topleftCorner = topLeftCorner;
        this.inputListener = inputListener;
        this.imageReader = imageReader;

        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        
        setTag(AVATAR_TAG);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        left_right_movment_handler();

    }

    private void left_right_movment_handler() {
        float velocityX = 0;

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            velocityX = -WALK_SPEED;
        }
        else if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            velocityX = WALK_SPEED;
        }

        transform().setVelocityX(velocityX);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        if (collision.getNormal().y() < 0) {
            transform().setVelocityY(0);
        }
    }
}
