package nova.event.events;

import nova.event.Event;

/**
 * @author Kurt Dee
 * @since 5/19/2017
 */
public class PlayerMoveEvent implements Event {
    double[] movePointer;

    public PlayerMoveEvent(double[] movePointer) {
        this.movePointer = movePointer;
    }

    public double[] getMovePointer() {
        return movePointer;
    }
}
