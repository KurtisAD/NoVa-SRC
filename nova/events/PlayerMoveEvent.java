package nova.events;

/**
 * Created by Skeleton Man on 1/19/2017.
 */
public class PlayerMoveEvent {
    double[] movePointer;

    public PlayerMoveEvent(double[] movePointer) {
        this.movePointer = movePointer;
    }

    public double[] getMovePointer() {
        return movePointer;
    }
}
